package org.example.project.data.remote.client

import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.SerializationException
import org.example.project.utils.ApiResult

suspend inline fun <reified T> safeApiCall(
    crossinline block: suspend () -> HttpResponse
): ApiResult<T> {
    return try {
        val response = block()

        if (response.status.isSuccess()) {
            val data = response.body<T>()
            ApiResult.Success(data)
        } else {
            ApiResult.Error(
                message = "Server error: ${response.status}",
                code = response.status.value
            )
        }
    } catch (e: SerializationException) {
        ApiResult.Error("Data parsing error: ${e.message}")
    } catch (e: Exception) {
        ApiResult.Error("Network error: ${e.message}")
    }
}
