package com.visma.guardian

import com.visma.guardian.model.ComponentsResponse
import com.visma.guardian.model.IncidentsResponse
import com.visma.guardian.model.MaintenancesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess

public class StatusApi(
    private val httpClient: HttpClient,
    private val config: GuardianConfig = GuardianConfig(),
) {
    public suspend fun maintenances(): MaintenancesResponse = get("v2/scheduled-maintenances.json")

    public suspend fun incidents(): IncidentsResponse = get("v2/incidents.json")

    public suspend fun components(): ComponentsResponse = get("v2/components.json")

    private suspend inline fun <reified T> get(path: String): T {
        val response = httpClient.get(config.statusBaseUrl + path)
        if (!response.status.isSuccess()) throw GuardianException.Http(response.status.value, response.body())
        return response.body()
    }
}
