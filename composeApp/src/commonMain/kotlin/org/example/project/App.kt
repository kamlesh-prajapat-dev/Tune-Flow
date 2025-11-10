package org.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.example.project.player.AudioPlayer
import org.example.project.ui.TuneFlow

@Composable
fun App() {
    MaterialTheme {
        TuneFlow()
    }
}