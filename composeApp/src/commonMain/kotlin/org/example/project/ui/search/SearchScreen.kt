package org.example.project.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.delay
import org.example.project.ui.home.HomeViewModel
import org.example.project.ui.home.SongItem

class SearchScreen(
    val viewModel: HomeViewModel
) : Screen {
    @Composable
    override fun Content() {

        val query by viewModel.searchQuery.collectAsState()
        val filteredList by viewModel.filteredList.collectAsState()

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(top = 20.dp)
        ) {

            val focusRequester = remember { FocusRequester() }

            LaunchedEffect(Unit) {
                delay(300)
                focusRequester.requestFocus()
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth()
                    .focusRequester(focusRequester),
                value = query,
                onValueChange = {
                    viewModel.onSearchQueryChange(it)
                },
                leadingIcon = {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Arrow Back"
                        )
                    }
                },
                placeholder = {
                    Text(
                        text = "Search by title...",
                        color = Color.Gray
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                )
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(filteredList) {it ->
                    SongItem(songItem = it, filteredList)
                }
            }
        }
    }
}