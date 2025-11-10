package org.example.project.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.example.project.domain.model.Song
import org.example.project.ui.music.MusicPlayerScreen
import org.example.project.ui.music.MusicViewModel
import org.example.project.ui.search.SearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayList(viewModel: HomeViewModel) {

    val isDropDown by viewModel.isDropDown.collectAsState()
    val sortedList by viewModel.sortedList.collectAsState()
    val navigator = LocalNavigator.currentOrThrow

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(

                title = {
                    Text(
                        text = "Tune Flow",
                        fontWeight = FontWeight.Black
                    )

                },

                actions = {
                    IconButton(
                        onClick = {
                            navigator.push(SearchScreen(
                                viewModel
                            ))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.Black
                        )
                    }

                }
            )

        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Songs",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )

                IconButton(
                    onClick = {
                        viewModel.onChangeDD(true)
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Sort,
                        contentDescription = "Sort"
                    )

                    if (isDropDown) {
                        Box(
                            modifier = Modifier.fillMaxSize().padding(top = 50.dp).wrapContentSize(Alignment.TopEnd)
                        ) {
                            DropdownMenu(
                                modifier = Modifier.width(150.dp),
                                expanded = isDropDown,
                                onDismissRequest = { viewModel.onChangeDD(false) }
                            ) {
                                Text(
                                    modifier = Modifier.padding(start = 10.dp),
                                    text = "Sort",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                DropdownMenuItem(
                                    text = { Text(text = "By song name", fontSize = 16.sp) },
                                    onClick = { /* Handle sort by song name! */
                                        viewModel.sortByName()
                                        viewModel.onChangeDD(false)
                                    }
                                )

                                DropdownMenuItem(
                                    modifier = Modifier.height(30.dp),
                                    text = { Text(text = "By time duration", fontSize = 16.sp) },
                                    onClick = {
                                        /* Handle sort by time duration! */
                                        viewModel.sortByDuration()
                                        viewModel.onChangeDD(false)
                                    },
                                )
                            }
                        }
                    }
                }

            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(sortedList) {it ->
                    SongItem(songItem = it, sortedList)
                }
            }
        }
    }
}

@Composable
fun SongItem(
   songItem: Song, list: List<Song>
) {
    val navigator = LocalNavigator.currentOrThrow

    val viewModel: MusicViewModel = viewModel()

    Card(
        modifier = Modifier.fillMaxWidth()
            .clickable(
                onClick = {
                    viewModel.initializeList(list)
                    viewModel.onChangeSongItem(songItem)

                    navigator.push(MusicPlayerScreen(
                        viewModel
                    ))
                }
            ),
        shape = RectangleShape,
        border = BorderStroke(
            width = 0.5.dp,
            color = Color.Gray
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KamelImage(
                modifier = Modifier.size(70.dp),
                resource = asyncPainterResource(songItem.albumImage),
                contentDescription = songItem.title
            )

            Column {
                Text(
                    modifier = Modifier.padding(
                        top = 0.dp,
                        start = 5.dp,
                        end = 5.dp,
                        bottom = 5.dp
                    ),
                    text = songItem.title
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = songItem.artist
                    )

                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        text = songItem.duration
                    )
                }
            }
        }
    }
}