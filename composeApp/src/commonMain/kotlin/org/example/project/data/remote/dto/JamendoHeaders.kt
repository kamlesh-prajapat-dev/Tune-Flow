package org.example.project.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JamendoHeaders(
    @SerialName("status")
    val status: String,

    @SerialName("code")
    val code: Int,

    @SerialName("error_message")
    val errorMessage: String,

    @SerialName("warnings")
    val warnings: String,

    @SerialName("results_count")
    val resultsCount: Int,

    @SerialName("next")
    val next: String? = null
)
