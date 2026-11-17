package com.visma.guardian

public sealed class GuardianException(
    message: String,
) : Exception(message) {
    public class Http(
        public val status: Int,
        public val responseBody: String,
    ) : GuardianException("Guardian request failed with HTTP $status: $responseBody")

    public class Authentication(
        message: String,
    ) : GuardianException(message)
}
