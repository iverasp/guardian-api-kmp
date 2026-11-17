package com.visma.guardian

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.head
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess

public class AttachmentApi(
    private val httpClient: HttpClient,
) {
    public suspend fun download(url: String): ByteArray {
        val response = httpClient.get(url)
        if (!response.status.isSuccess()) throw GuardianException.Http(response.status.value, response.body())
        return response.body()
    }

    public suspend fun contentType(url: String): String? {
        val response = httpClient.head(url)
        if (!response.status.isSuccess()) throw GuardianException.Http(response.status.value, response.body())
        return response.headers[HttpHeaders.ContentType]
    }
}
