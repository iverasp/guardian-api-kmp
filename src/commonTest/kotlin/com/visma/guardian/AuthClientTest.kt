package com.visma.guardian

import com.visma.guardian.model.InMemoryCredentialStore
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class AuthClientTest {
    private val client = AuthClient(HttpClient(MockEngine { respondOk() }), InMemoryCredentialStore())

    @Test
    fun authorizationRequestContainsPkceAndExpectedOAuthParameters() {
        val request = client.createAuthorizationRequest("municipality", SecurityLevel.LEVEL3.acrValues)

        assertTrue(request.url.startsWith("https://connect.visma.com/connect/authorize?"))
        assertTrue(request.url.contains("client_id=mykindergartenguardian"))
        assertTrue(request.url.contains("response_type=code"))
        assertTrue(request.url.contains("code_challenge_method=S256"))
        assertTrue(request.url.contains("code_challenge="))
        assertTrue(request.url.contains("client_context=municipality"))
        assertTrue(request.url.contains("acr_values=urn%3Aidp%3Aid-porten+urn%3Aidp%3Avismaconnect%3Amethod%3Apwdless"))
        assertEquals(43, request.state.length)
        assertEquals(43, request.nonce.length)
    }

    @Test
    fun securityLevelMapsMunicipalityLevelAndDefaultsToLevel4() {
        assertEquals(SecurityLevel.LEVEL3, SecurityLevel.fromValue(3))
        assertEquals(SecurityLevel.LEVEL4, SecurityLevel.fromValue(null))
        assertEquals(SecurityLevel.LEVEL4, SecurityLevel.fromValue(99))
    }

    @Test
    fun authorizationRequestsUseFreshSecurityValues() {
        val first = client.createAuthorizationRequest(acrValues = "level")
        val second = client.createAuthorizationRequest(acrValues = "level")

        assertNotEquals(first.state, second.state)
        assertNotEquals(first.nonce, second.nonce)
        assertNotEquals(first.url, second.url)
    }
}
