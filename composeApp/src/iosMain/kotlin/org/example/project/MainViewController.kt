package org.example.project

import androidx.compose.ui.window.ComposeUIViewController
import org.example.project.player.AudioPlayer


fun MainViewController() = ComposeUIViewController { App(player = AudioPlayer()) }