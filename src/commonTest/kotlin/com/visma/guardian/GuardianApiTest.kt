package com.visma.guardian

import com.visma.guardian.model.Credentials
import com.visma.guardian.model.InMemoryCredentialStore
import com.visma.guardian.model.Token
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class GuardianApiTest {
    @Test
    fun authenticatedRequestsIncludeBearerSessionAndLanguageHeaders() =
        runTest {
            val engine =
                MockEngine { request ->
                    assertEquals("Bearer access", request.headers[HttpHeaders.Authorization])
                    assertEquals("session", request.headers["SessionId"])
                    assertEquals("nb", request.headers[HttpHeaders.AcceptLanguage])
                    respond(
                        content = "{\"firstName\":\"Test\"}",
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                    )
                }
            val httpClient =
                HttpClient(engine) {
                    install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
                }
            val store =
                InMemoryCredentialStore(
                    Credentials(Token("id", "access", 3600, "Bearer", "refresh"), "user", "session"),
                )
            val auth = AuthClient(httpClient, store)
            val api = GuardianApi(httpClient, store, auth)

            assertEquals("Test", api.session().firstName)
        }
}
