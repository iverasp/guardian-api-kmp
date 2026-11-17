package com.visma.guardian.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Token(
    @SerialName("id_token") val idToken: String,
    @SerialName("access_token") val accessToken: String,
    @SerialName("expires_in") val expiresIn: Int,
    @SerialName("token_type") val tokenType: String,
    @SerialName("refresh_token") val refreshToken: String,
)

@Serializable public data class CreateSessionResponse(
    val sessionId: String? = null,
    val username: String? = null,
)

public data class Credentials(
    val token: Token,
    val userId: String,
    val sessionId: String? = null,
)

public interface CredentialStore {
    public suspend fun load(): Credentials?

    public suspend fun save(credentials: Credentials)

    public suspend fun clear()
}

public class InMemoryCredentialStore(
    initial: Credentials? = null,
) : CredentialStore {
    private var credentials: Credentials? = initial

    override suspend fun load(): Credentials? = credentials

    override suspend fun save(credentials: Credentials) {
        this.credentials = credentials
    }

    override suspend fun clear() {
        credentials = null
    }
}
