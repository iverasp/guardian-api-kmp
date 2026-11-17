package com.visma.guardian

public data class GuardianConfig(
    val guardianBaseUrl: String = "https://api.guardian.mobile.barnehage.visma.no/",
    val identityBaseUrl: String = "https://connect.visma.com/connect/",
    val statusBaseUrl: String = "https://zvpknzcr6znl.statuspage.io/api/",
    val clientId: String = "mykindergartenguardian",
    val redirectUri: String = "com.visma.mykindergarten.guardian://redirect",
    val scopes: List<String> =
        listOf(
            "nnin",
            "openid",
            "offline_access",
            "flytkindergartenguardianmobileapi:all",
        ),
    val userAgent: String = "GuardianApiKmp/0.1.0",
)
