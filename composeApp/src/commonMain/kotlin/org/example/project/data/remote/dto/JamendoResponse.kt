package org.example.project.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JamendoResponse(
    @SerialName("headers")
    val headers: JamendoHeaders,

    @SerialName("results")
    val results: List<JamendoTrackDto>
)
