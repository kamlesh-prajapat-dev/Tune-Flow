package org.example.project.ui.music

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex

class MusicViewModel : ViewModel() {

    private val _icon = MutableStateFlow(false)
    val icon: StateFlow<Boolean> = _icon.asStateFlow()

    fun onChangePlaying(flag: Boolean) {
        _icon.value = flag
    }
}