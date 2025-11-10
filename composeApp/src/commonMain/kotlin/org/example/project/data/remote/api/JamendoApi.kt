package org.example.project.data.remote.api

import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.example.project.data.remote.client.HttpClientProvider
import org.example.project.data.remote.client.safeApiCall
import org.example.project.data.remote.dto.JamendoResponse
import org.example.project.utils.ApiResult

object JamendoApi {
    private const val BASE_URL = "https://api.jamendo.com/v3.0"
    private const val CLIENT_ID = "792cc03a"
    private val client get() = HttpClientProvider.client
    suspend fun getTracks(limit: Int = 20): ApiResult<JamendoResponse> = safeApiCall {

        client.get("$BASE_URL/tracks") {
            parameter("client_id", CLIENT_ID)
            parameter("format", "json")
            parameter("limit", limit)
            }
    }
}