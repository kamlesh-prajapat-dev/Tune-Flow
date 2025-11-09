package org.example.project.player

import android.media.MediaPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

actual class AudioPlayer actual constructor() {
    private val _state = MutableStateFlow(SimplePlayerState.Idle)
    actual val state: StateFlow<SimplePlayerState> = _state.asStateFlow()

    private val _position = MutableStateFlow(0L)
    actual val positionMs: StateFlow<Long> = _position.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    actual val durationMs: StateFlow<Long> = _duration.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    actual val error: StateFlow<String?> = _error.asStateFlow()

    private var player: MediaPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    actual fun play(url: String) {
        try {
            if (player == null) player = MediaPlayer()

            player?.apply {
                reset()
                setDataSource(url)
                setOnPreparedListener {
                    _duration.value = it.duration.toLong()
                    it.start()
                    _state.value = SimplePlayerState.Playing
                    pollPosition()
                }
                setOnCompletionListener {
                    _state.value = SimplePlayerState.Stopped
                }
                prepareAsync()
            }

        } catch (e: Exception) {
            _state.value = SimplePlayerState.Error
            _error.value = e.message
        }
    }

    private fun pollPosition() {
        scope.launch {
            while (true) {
                val p = player ?: break
                _position.value = p.currentPosition.toLong()
                delay(500)
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
        player?.release()
        player = null
        scope.cancel()
    }
}