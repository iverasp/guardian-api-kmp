package com.visma.guardian

import com.visma.guardian.internal.secureRandomBytes
import com.visma.guardian.model.CredentialStore
import com.visma.guardian.model.Credentials
import com.visma.guardian.model.Token
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okio.ByteString.Companion.decodeBase64
import okio.ByteString.Companion.toByteString

public data class AuthorizationRequest(
    val url: String,
    val state: String,
    val nonce: String,
    internal val codeVerifier: String,
)

public class AuthClient(
    private val httpClient: HttpClient,
    private val credentialStore: CredentialStore,
    private val config: GuardianConfig = GuardianConfig(),
    private val json: Json = Json { ignoreUnknownKeys = true },
) {
    public fun createAuthorizationRequest(
        clientContext: String = "",
        acrValues: String,
    ): AuthorizationRequest {
        val verifier = randomBase64Url(32)
        val state = randomBase64Url(32)
        val nonce = randomBase64Url(32)
        val challenge =
            verifier
                .encodeToByteArray()
                .toByteString()
                .sha256()
                .base64Url()
                .trimEnd('=')
        val url =
            URLBuilder(config.identityBaseUrl + "authorize")
                .apply {
                    parameters.append("client_id", config.clientId)
                    parameters.append("redirect_uri", config.redirectUri)
                    parameters.append("response_mode", "query")
                    parameters.append("response_type", "code")
                    parameters.append("scope", config.scopes.joinToString(" "))
                    parameters.append("client_context", clientContext)
                    parameters.append("acr_values", acrValues)
                    parameters.append("nonce", nonce)
                    parameters.append("state", state)
                    parameters.append("code_challenge_method", "S256")
                    parameters.append("code_challenge", challenge)
                }.buildString()
        return AuthorizationRequest(url, state, nonce, verifier)
    }

    public suspend fun exchangeRedirect(
        authorizationRequest: AuthorizationRequest,
        redirectUrl: String,
    ): Credentials {
        val redirect = Url(redirectUrl)
        if (!redirectUrl.startsWith(config.redirectUri)) {
            throw GuardianException.Authentication("Unexpected OAuth redirect URI")
        }
        if (redirect.parameters["state"] != authorizationRequest.state) {
            throw GuardianException.Authentication("OAuth state verification failed")
        }
        redirect.parameters["error"]?.let {
            throw GuardianException.Authentication("OAuth error: $it")
        }
        val code =
            redirect.parameters["code"]
                ?: throw GuardianException.Authentication("OAuth redirect has no authorization code")
        val token =
            tokenRequest(
                Parameters.build {
                    append("client_id", config.clientId)
                    append("redirect_uri", config.redirectUri)
                    append("grant_type", "authorization_code")
                    append("code", code)
                    append("code_verifier", authorizationRequest.codeVerifier)
                },
            )
        validateToken(token, authorizationRequest.nonce)
        return Credentials(token, token.idToken.claim("sub")).also { credentialStore.save(it) }
    }

    public suspend fun refresh(refreshToken: String): Token {
        val token =
            tokenRequest(
                Parameters.build {
                    append("client_id", config.clientId)
                    append("grant_type", "refresh_token")
                    append("refresh_token", refreshToken)
                },
            )
        validateToken(token)
        return token
    }

    public suspend fun revoke(
        token: String,
        tokenTypeHint: String,
    ) {
        val response =
            httpClient.submitForm(
                url = config.identityBaseUrl + "revocation",
                formParameters =
                    Parameters.build {
                        append("client_id", config.clientId)
                        append("token_type_hint", tokenTypeHint)
                        append("token", token)
                    },
            )
        if (!response.status.isSuccess()) throw GuardianException.Http(response.status.value, response.body())
    }

    public suspend fun logout() {
        val credentials = credentialStore.load()
        if (credentials != null) {
            runCatching { revoke(credentials.token.accessToken, "access_token") }
            runCatching { revoke(credentials.token.refreshToken, "refresh_token") }
        }
        credentialStore.clear()
    }

    internal suspend fun refreshStored(expectedAccessToken: String): Credentials {
        val current =
            credentialStore.load()
                ?: throw GuardianException.Authentication("No stored credentials")
        if (current.token.accessToken != expectedAccessToken) return current
        val refreshed = current.copy(token = refresh(current.token.refreshToken))
        credentialStore.save(refreshed)
        return refreshed
    }

    private suspend fun tokenRequest(parameters: Parameters): Token {
        val response = httpClient.submitForm(config.identityBaseUrl + "token", parameters)
        if (!response.status.isSuccess()) throw GuardianException.Http(response.status.value, response.body())
        return response.body()
    }

    private fun validateToken(
        token: Token,
        expectedNonce: String? = null,
    ) {
        if (token.tokenType != "Bearer" || token.expiresIn <= 0 || token.accessToken.isBlank() || token.refreshToken.isBlank()) {
            throw GuardianException.Authentication("Visma Connect returned an invalid token")
        }
        token.idToken.claim("sub")
        if (expectedNonce != null && token.idToken.claim("nonce") != expectedNonce) {
            throw GuardianException.Authentication("ID token nonce verification failed")
        }
    }

    private fun String.claim(name: String): String {
        val payload =
            split('.').getOrNull(1)?.decodeBase64()?.utf8()
                ?: throw GuardianException.Authentication("Invalid ID token")
        return json
            .parseToJsonElement(payload)
            .jsonObject[name]
            ?.jsonPrimitive
            ?.content
            ?: throw GuardianException.Authentication("ID token has no $name claim")
    }

    private fun randomBase64Url(size: Int): String = secureRandomBytes(size).toByteString().base64Url().trimEnd('=')
}
