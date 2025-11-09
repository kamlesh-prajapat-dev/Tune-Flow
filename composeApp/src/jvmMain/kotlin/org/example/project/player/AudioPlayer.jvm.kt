package org.example.project.player

import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

actual class AudioPlayer actual constructor() {

    private var player: MediaPlayer? = null
    private val _state = MutableStateFlow(SimplePlayerState.Idle)
    actual val state: StateFlow<SimplePlayerState> = _state.asStateFlow()

    private val _position = MutableStateFlow(0L)
    actual val positionMs: StateFlow<Long> = _position.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    actual val durationMs: StateFlow<Long> = _duration.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    actual val error: StateFlow<String?> = _error.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        // Start JavaFX toolkit once
        Platform.startup {}
    }

    actual fun play(url: String) {
        try {
            player?.dispose()
            val media = Media(url)
            player = MediaPlayer(media)

            player?.setOnReady {
                _duration.value = player?.totalDuration?.toMillis()?.toLong() ?: 0L
                player?.play()
                _state.value = SimplePlayerState.Playing
                startPolling()
            }

        } catch (e: Exception) {
            _error.value = e.message
            _state.value = SimplePlayerState.Error
        }
    }

    private fun startPolling() {
        scope.launch {
            while (player != null) {
                _position.value = player?.currentTime?.toMillis()?.toLong() ?: 0L
                delay(300)
            }
        }
    }

    actual fun pause() {
        player?.pause()
        _state.value = SimplePlayerState.Paused
    }

    actual fun stop() {
        player?.stop()
        _state.value = SimplePlayerState.Stopped
    }

    actual fun release() {
        player?.dispose()
        player = null
        scope.cancel()
    }
}