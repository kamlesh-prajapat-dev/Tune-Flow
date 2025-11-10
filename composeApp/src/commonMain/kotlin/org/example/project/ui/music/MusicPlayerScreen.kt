package org.example.project.ui.music

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.datetime.LocalTime
import org.example.project.domain.model.Song
import org.example.project.player.AudioPlayer
import org.example.project.player.SimplePlayerState
import kotlin.time.ExperimentalTime

class MusicPlayerScreen(
    val viewModel: MusicViewModel
) : Screen {

    @OptIn(ExperimentalTime::class)
    @Composable
    override fun Content() {

        // 1) Create / remember AudioPlayer instance for this screen:
        val player = remember { AudioPlayer() }

        // 2) Ensure player released when screen leaves
        DisposableEffect(Unit) {
            onDispose {
                player.release()
            }
        }

        // 4) Observe player and model state for UI
        val playerState by player.state.collectAsState()
        val position by player.positionMs.collectAsState()
        val duration by player.durationMs.collectAsState()
        val error by player.error.collectAsState()

        val icon by viewModel.icon.collectAsState()

        val progress = if (duration > 0) position.toFloat() / duration.toFloat() else 0f
        val isBuffering by viewModel.isBuffering.collectAsState()

        val songItem by viewModel.songItem.collectAsState()

        // 5) Auto-sync buffering state if player emits Buffering
        LaunchedEffect(playerState) {
            viewModel.onChangeBuffering(playerState == SimplePlayerState.Buffering)
            viewModel.onChangeIcon(playerState == SimplePlayerState.Playing)
        }

        // 6) Build UI (example minimal controls)
        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) {

            Box(
                modifier = Modifier.fillMaxSize().padding(it)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = songItem.title,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = songItem.artist,
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier.size(300.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.LightGray.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        KamelImage(
                            modifier = Modifier.fillMaxSize(),
                            resource = asyncPainterResource(
                                data = songItem.image ?: Icons.Default.Image
                            ),
                            contentDescription = songItem.title,
                            contentScale = ContentScale.Crop
                        )

                        if (isBuffering) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.4f)),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(48.dp),
                                    strokeWidth = 4.dp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Current Time - Problem
                        Text(
                            text = "${(position / 60000).toInt().toString().padStart(2, '0')}:${((position / 1000) % 60).toInt().toString().padStart(2, '0')}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                        )

                        LinearProgressIndicator(
                            progress = {
                                progress
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = ProgressIndicatorDefaults.linearColor,
                            trackColor = ProgressIndicatorDefaults.linearTrackColor,
                            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                        )

                        // Total Time
                        Text(
                            text = songItem.duration,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .height(100.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        IconButton(
                            onClick = {
                                viewModel.previousSong(songItem = songItem)
                                player.release()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipPrevious,
                                contentDescription = "Skip Previous"
                            )
                        }

                        // Play/Pause button with optimistic UI behaviour
                        IconButton(
                            modifier = Modifier.size(100.dp),
                            onClick = {
                                when (playerState) {
                                    SimplePlayerState.Paused, SimplePlayerState.Idle, SimplePlayerState.Stopped -> {
                                        player.play(url = songItem.audio)
                                        viewModel.onChangeIcon(true)
                                    }

                                    SimplePlayerState.Playing -> {
                                        player.pause()
                                        viewModel.onChangeIcon(false)
                                    }

                                    SimplePlayerState.Error -> {
                                        viewModel.onChangeBuffering(false)
                                        viewModel.onChangeIcon(false)
                                    }

                                    SimplePlayerState.Buffering -> {  }
                                }
                            }
                        ) {
                            Crossfade(targetState = isBuffering || playerState == SimplePlayerState.Buffering, label = "") { buffering ->
                                if (buffering) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(60.dp),
                                        strokeWidth = 5.dp
                                    )
                                } else {
                                    Icon(
                                        modifier = Modifier.size(100.dp),
                                        imageVector = if (!icon) Icons.Default.PlayCircle else Icons.Default.PauseCircle,
                                        contentDescription = "Play Circle / Pause Circle"
                                    )
                                }
                            }
                        }


                        IconButton(
                            onClick = {
                                viewModel.nextSong(songItem)
                                player.release()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipNext,
                                contentDescription = "Skip Next"
                            )
                        }

                        if (error != null) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Error: $error",
                                    color = Color.Red,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 8.dp),
                                    fontSize = 20.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}