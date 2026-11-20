package com.visma.guardian

import com.visma.guardian.model.Municipality

/** Visma Connect `acr_values` used by the Guardian app for each municipality security level. */
public enum class SecurityLevel(
    public val value: Int,
    public val acrValues: String,
) {
    LEVEL2(2, "urn:idp:id-porten urn:idp:vismaconnect urn:idp:google"),
    LEVEL3(3, "urn:idp:id-porten urn:idp:vismaconnect:method:pwdless"),
    LEVEL4(4, "urn:idp:id-porten:level:4"),
    ;

    public companion object {
        /** The app falls back to level 4 when the municipality has no stored level. */
        public val DEFAULT: SecurityLevel = LEVEL4

        public fun fromValue(value: Int?): SecurityLevel = entries.firstOrNull { it.value == value } ?: DEFAULT
    }
}

public val Municipality.securityLevel: SecurityLevel
    get() = SecurityLevel.fromValue(minSecurityLevel)
