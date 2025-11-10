package org.example.project.player

import android.media.MediaPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
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

    private var positionJob: Job? = null

    private var currentUrl: String? = null
    private var lastPosition: Int = 0


    actual fun play(url: String) {
        try {
            if (player == null) player = MediaPlayer()

            // ✅ If same song is paused → resume
            if (_state.value == SimplePlayerState.Paused && url == currentUrl) {
                player?.seekTo(lastPosition)
                player?.start()
                _state.value = SimplePlayerState.Playing
                startPositionPolling()
                return
            }

            // ✅ If new song or stopped → prepare again
            _state.value = SimplePlayerState.Buffering

            player?.apply {
                reset()
                currentUrl = url
                setDataSource(url)

                setOnPreparedListener {
                    _duration.value = it.duration.toLong()
                    it.start()
                    _state.value = SimplePlayerState.Playing
                    startPositionPolling()
                }

                setOnCompletionListener {
                    stopPositionPolling()
                    _state.value = SimplePlayerState.Stopped
                }

                setOnErrorListener { _, what, extra ->
                    stopPositionPolling()
                    _state.value = SimplePlayerState.Error
                    _error.value = "Error code: $what ($extra)"
                    true
                }

                prepareAsync()
            }

        } catch (e: Exception) {
            stopPositionPolling()
            _state.value = SimplePlayerState.Error
            _error.value = e.message
        }
    }

//    private fun pollPosition() {
//        scope.launch {
//            while (true) {
//                val p = player ?: break
//                _position.value = p.currentPosition.toLong()
//                delay(500)
//            }
//        }
//    }

    actual fun pause() {    
        player?.takeIf { it.isPlaying }?.let {
            lastPosition = it.currentPosition
            it.pause()
            _state.value = SimplePlayerState.Paused
            stopPositionPolling()
        }
    }

    actual fun stop() {
        player?.stop()
        _state.value = SimplePlayerState.Stopped
        stopPositionPolling()
        lastPosition = 0
        currentUrl = null
    }

    actual fun release() {
        stopPositionPolling()
        player?.release()
        player = null
        currentUrl = null
        lastPosition = 0
        scope.cancel()

        _state.value = SimplePlayerState.Stopped
    }

    private fun startPositionPolling() {
        stopPositionPolling()
        positionJob = scope.launch {
            while (isActive && player != null && player!!.isPlaying) {
                _position.value = player!!.currentPosition.toLong()
                delay(500)
            }
        }
    }

    private fun stopPositionPolling() {
        positionJob?.cancel()
        positionJob = null
    }
}