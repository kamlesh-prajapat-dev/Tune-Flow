package org.example.project.domain.model

import kotlinx.datetime.LocalTime

data class Song(
    val title: String,
    val artist: String,
    val duration: LocalTime,
    val albumImage: String,
    val audio: String,
    val image: String?
)