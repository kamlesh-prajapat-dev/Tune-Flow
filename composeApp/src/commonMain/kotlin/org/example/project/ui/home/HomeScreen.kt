package org.example.project.ui.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import org.example.project.player.AudioPlayer
import org.example.project.ui.components.ErrorView
import org.example.project.ui.components.LoadingView
import org.example.project.ui.music.MusicViewModel
import org.example.project.utils.ApiResult

class HomeScreen(
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val viewModel: HomeViewModel = viewModel()


        val state by viewModel.uiState.collectAsState()

        when (state) {
            is ApiResult.Loading -> LoadingView()
            is ApiResult.Error -> {
                val error = (state as ApiResult.Error)
                ErrorView(
                    message = error.message,
                    onRetry = {
                        viewModel.loadPlayList()
                    }
                )
            }

            is ApiResult.Success -> {
                val tracks = (state as ApiResult.Success).data
                viewModel.initialize(tracks)
                PlayList(viewModel)
            }
        }
    }
}