# Guardian API Kotlin Multiplatform client

Unofficial Kotlin Multiplatform client reconstructed from the Visma Min Barnehage Foresatt Android application version 1.2.26.

Targets:

- JVM/Android-compatible bytecode
- Linux x64 Kotlin/Native

The common implementation uses Ktor and kotlinx.serialization. It includes OAuth 2.0 Authorization Code with PKCE, token refresh, Guardian session handling, Statuspage access, attachment downloads, and the API data classes found in the APK.

## Usage

```kotlin
val httpClient = guardianHttpClient()
val store: CredentialStore = MySecureCredentialStore()
val auth = AuthClient(httpClient, store)
val api = GuardianApi(httpClient, store, auth)

val authorization = auth.createAuthorizationRequest(
    clientContext = municipality.vismaConnectClientContext.orEmpty(),
    acrValues = municipality.securityLevel.acrValues,
)

// Open authorization.url in a browser and receive the custom-scheme redirect.
auth.exchangeRedirect(authorization, redirectUrl)

// deviceId corresponds to the Firebase installation ID used by the app.
api.establishSession(
    clientContext = municipality.vismaConnectClientContext.orEmpty(),
    deviceId = deviceId,
)

val session = api.session()
val messages = api.messages(fromMessageId = "", directionType = 0)
```

`CredentialStore` is intentionally injected. Applications should implement it with platform-secure storage such as Android Keystore-backed preferences or Apple Keychain. `InMemoryCredentialStore` is provided for tests and short-lived command-line use only.

## Authentication flow

1. Fetch municipalities with `api.municipalities()`.
2. Create a PKCE request with `AuthClient.createAuthorizationRequest()`.
3. Open the returned URL and capture `com.visma.mykindergarten.guardian://redirect`.
4. Call `exchangeRedirect()`. This checks OAuth state and the ID-token nonce.
5. Call `establishSession()` to obtain the separate Guardian `SessionId`.
6. API calls send the access token and session ID. A `401` triggers one synchronized refresh and retry.

The app only parses ID-token claims and checks the nonce; this library mirrors that behavior and does not verify the JWT signature. A security-sensitive consumer should additionally validate the token against the issuer's metadata/JWKS.

## Build

```shell
./gradlew build
```

## Limitations

- Model names are cleaned up, but their JSON fields match the decompiled DTOs.
- Date/time fields remain strings because their exact server formats vary by endpoint.
- Unknown numeric enum mappings remain `Int` values.
- Certificate pinning from the Android app is not enabled automatically because pinning configuration is engine/platform-specific.
- This is an unofficial reverse-engineered client. API compatibility and authorization to use the service are not guaranteed.
