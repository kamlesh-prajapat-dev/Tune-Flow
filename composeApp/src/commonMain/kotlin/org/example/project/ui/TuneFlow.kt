package org.example.project.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.player.AudioPlayer
import org.example.project.ui.home.HomeScreen

@Composable
fun TuneFlow() {
    Navigator(screen = HomeScreen())
}