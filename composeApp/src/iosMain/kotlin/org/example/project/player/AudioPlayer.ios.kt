package org.example.project.player

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.currentItem
import platform.AVFoundation.currentTime
import platform.AVFoundation.duration
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.Foundation.NSURL

actual class AudioPlayer actual constructor() {
    private var player: AVPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    private val _state = MutableStateFlow(SimplePlayerState.Idle)
    actual val state: StateFlow<SimplePlayerState> = _state.asStateFlow()

    private val _position = MutableStateFlow(0L)
    actual val positionMs: StateFlow<Long> = _position.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    actual val durationMs: StateFlow<Long> = _duration.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    actual val error: StateFlow<String?> = _error.asStateFlow()

    actual fun play(url: String) {
        try {
            val item = AVPlayerItem(uRL = NSURL(string = url))
            player = AVPlayer(playerItem = item)
            _state.value = SimplePlayerState.Playing

            poll()

            player?.play()
        } catch (e: Exception) {
            _error.value = e.message
            _state.value = SimplePlayerState.Error
        }
    }


    @OptIn(ExperimentalForeignApi::class)
    private fun poll() {
        scope.launch {
            while (true) {
                val p = player ?: break
                val t = p.currentTime().value / 1000L
                _position.value = t
                _duration.value = p.currentItem?.duration?.value?.div(1000L) ?: 0L
                delay(500)
            }
        }
    }

    actual fun pause() {
        player?.pause()
        _state.value = SimplePlayerState.Paused
    }

    actual fun stop() {
        player?.pause()
        _state.value = SimplePlayerState.Stopped
    }

    actual fun release() {
        player = null
        scope.cancel()
    }
}