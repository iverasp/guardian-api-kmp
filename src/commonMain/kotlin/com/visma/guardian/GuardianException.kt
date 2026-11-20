package com.visma.guardian

public sealed class GuardianException(
    message: String,
) : Exception(message) {
    public class Http(
        status: Int,
        responseBody: String,
    ) : GuardianException("Guardian request failed with HTTP $status: $responseBody")

    public class Authentication(
        message: String,
    ) : GuardianException(message)
}
