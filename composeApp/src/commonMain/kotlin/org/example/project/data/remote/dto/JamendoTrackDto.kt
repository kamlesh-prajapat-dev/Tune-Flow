package org.example.project.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JamendoTrackDto(
    @SerialName("id")
    val id: String,

    @SerialName("name")
    val title: String,

    @SerialName("duration")
    val duration: Int,

    @SerialName("artist_id")
    val artistId: String,

    @SerialName("artist_name")
    val artistName: String,

    @SerialName("album_name")
    val albumName: String,

    @SerialName("album_id")
    val albumId: String,

    @SerialName("license_ccurl")
    val licenseUrl: String,

    @SerialName("position")
    val position: Int,

    @SerialName("releasedate")
    val releaseDate: String,

    @SerialName("album_image")
    val albumImage: String,

    @SerialName("audio")
    val audio: String,

    @SerialName("audiodownload")
    val audioDownload: String,

    @SerialName("prourl")
    val prourl: String? = null,

    @SerialName("shorturl")
    val shorturl: String,

    @SerialName("shareurl")
    val shareurl: String,

    @SerialName("image")
    val image: String? = null,

    @SerialName("audiodownload_allowed")
    val audioDownloadAllowed: Boolean,

    @SerialName("content_id_free")
    val contentIdFree: Boolean
)
