package org.example.project.ui.music

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.datetime.LocalTime
import org.example.project.domain.model.Song
import org.example.project.player.AudioPlayer
import org.example.project.player.SimplePlayerState


class MusicPlayerScreen(
    val songItem: Song,
    val player: AudioPlayer,
    val viewModel: MusicViewModel
) : Screen {

    @Composable
    override fun Content() {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) {

            val state by player.state.collectAsState()
            val pos by player.positionMs.collectAsState()
            val dur by player.durationMs.collectAsState()
            val icon by viewModel.icon.collectAsState()
            val progress = if (dur > 0) pos.toFloat() / dur.toFloat() else 0f

            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = songItem.title
                    )

                    Text(
                        text = songItem.artist
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier.size(400.dp)
                ) {
                    KamelImage(
                        modifier = Modifier.fillMaxSize(),
                        resource = asyncPainterResource(
                            data = songItem.image ?: Icons.Default.Image
                        ),
                        contentDescription = songItem.title,
                        contentScale = ContentScale.FillHeight
                    )
                }

                Row(
                    modifier = Modifier.height(50.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Current Time
                    Text(
                        text = "${LocalTime.fromMillisecondOfDay(pos.toInt())}",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                    )

                    LinearProgressIndicator(
                        progress = {
                           progress
                        },
                        color = ProgressIndicatorDefaults.linearColor,
                        trackColor = ProgressIndicatorDefaults.linearTrackColor,
                        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                    )

                    // Total Time
                    Text(
                        text = "${LocalTime.fromMillisecondOfDay(dur.toInt())}",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .height(100.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = ""
                        )
                    }

                    IconButton(
                        onClick = {
                            when(state) {
                                SimplePlayerState.Paused, SimplePlayerState.Idle -> {
                                    player.play(url = songItem.audio)
                                    viewModel.onChangePlaying(true)
                                }
                                SimplePlayerState.Playing -> {
                                    player.pause()
                                    viewModel.onChangePlaying(false)
                                }
                                SimplePlayerState.Stopped -> {}
                                SimplePlayerState.Error -> {}
                            }
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(100.dp),
                            imageVector = if (!icon) Icons.Default.PlayCircle else Icons.Default.PauseCircle,
                            contentDescription = ""
                        )
                    }


                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = ""
                        )
                    }
                }
            }
        }
    }

}