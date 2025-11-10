package org.example.project.ui.music

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.example.project.domain.model.Song


class MusicViewModel : ViewModel() {


    private val _list = MutableStateFlow<List<Song>>(emptyList())

    fun initializeList(list: List<Song>) {
        _list.update {
            list
        }
    }

    private val _songItem = MutableStateFlow(
        Song(
            title = "",
            artist = "",
            duration = "00:00",
            albumImage = "",
            audio = "",
            image = ""
        )
    )
    val songItem : StateFlow<Song> = _songItem.asStateFlow()

    fun onChangeSongItem(songItem: Song) {
        _songItem.update {
            songItem
        }
    }

    // ⏭️ Go to next song
    fun nextSong(songItem: Song) {
        _isBuffering.value = true
        if (_list.value.isEmpty()) {
            _isBuffering.value = false
            return
        }

        _songItem.update {
            var index = _list.value.indexOf(songItem)

            if (index == _list.value.size - 1) {
                index = 0
                _list.value[index]
            } else {
                _list.value[index + 1]
            }
        }

        _isBuffering.value = false
    }

    // ⏮️ Go to previous song
    fun previousSong(songItem: Song) {
        _isBuffering.value = true
        if (_list.value.isEmpty()) {
            _isBuffering.value = false
            return
        }

        _songItem.update {
            var index = _list.value.indexOf(songItem)
            if (index == 0) {
                index = _list.value.size - 1
                _list.value[index]
            } else {
                _list.value[index - 1]
            }
        }

        _isBuffering.value = false
    }

    private val _icon = MutableStateFlow(false)
    val icon: StateFlow<Boolean> = _icon.asStateFlow()

    private val _isBuffering = MutableStateFlow(false)
    val isBuffering: StateFlow<Boolean> = _isBuffering.asStateFlow()

    fun onChangeBuffering(flag: Boolean) {
        _isBuffering.value = flag
    }

    fun onChangeIcon(flag: Boolean) {
        _icon.value = flag
    }
}