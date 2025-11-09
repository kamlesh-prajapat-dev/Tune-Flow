package org.example.project

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.project.player.AudioPlayer

fun main() = application {

    val player = remember { AudioPlayer() }

    Window(
        onCloseRequest = ::exitApplication,
        title = "TuneFlow",
    ) {
        App(player)
    }

}