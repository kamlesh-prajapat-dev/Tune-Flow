package org.example.project.player

import kotlinx.coroutines.flow.StateFlow

expect class AudioPlayer() {
    val state: StateFlow<SimplePlayerState>
    val positionMs: StateFlow<Long>
    val durationMs: StateFlow<Long>

    val error: StateFlow<String?>

    fun play(url: String)
    fun pause()

    fun stop()
    fun release()
}

enum class SimplePlayerState { Idle, Buffering, Playing, Paused, Stopped, Error }