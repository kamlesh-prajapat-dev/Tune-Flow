package org.example.project.player

import kotlinx.coroutines.flow.Flow

actual class AudioPlayer actual constructor() {
    actual val state: Flow<SimplePlayerState>
        get() = TODO("Not yet implemented")
    actual val positionMs: Flow<Long>
        get() = TODO("Not yet implemented")
    actual val durationMs: Flow<Long>
        get() = TODO("Not yet implemented")
    actual val error: Flow<String?>
        get() = TODO("Not yet implemented")

    actual fun play(url: String) {
    }

    actual fun pause() {
    }

    actual fun stop() {
    }

    actual fun release() {
    }
}