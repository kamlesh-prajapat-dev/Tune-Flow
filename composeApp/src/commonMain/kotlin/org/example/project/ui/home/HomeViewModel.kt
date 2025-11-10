package org.example.project.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.data.repository.TrackRepositoryImpl
import org.example.project.domain.model.Song
import org.example.project.domain.repository.TrackRepository
import org.example.project.utils.ApiResult

class HomeViewModel(
    private val repository: TrackRepository = TrackRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ApiResult<List<Song>>>(ApiResult.Loading)
    val uiState: StateFlow<ApiResult<List<Song>>> = _uiState.asStateFlow()


    init {
        loadPlayList()
    }

    fun loadPlayList() {
        viewModelScope.launch {
            _uiState.value = ApiResult.Loading

            try {
                _uiState.value = repository.getTracks()
            } catch (e: Exception) {
                _uiState.value = ApiResult.Error(message = e.message
                    ?: ("A network error occurred. " +
                            "Please check your connection and try again."), code = 0)
            }
        }
    }

    private val _isDropDown = MutableStateFlow(false)
    val isDropDown : StateFlow<Boolean> = _isDropDown.asStateFlow()

    fun onChangeDD(flag: Boolean) {
        _isDropDown.value = flag
    }

    private val _sortedList = MutableStateFlow<List<Song>>(emptyList())
    val sortedList : StateFlow<List<Song>> = _sortedList.asStateFlow()

    fun initialize(list: List<Song>) {
        _sortedList.update {
            list
        }
    }

    fun sortByName() {
        _sortedList.update {
            sortedList.value.sortedBy { it.title }
        }
    }

    fun sortByDuration() {
        _sortedList.update {
            sortedList.value.sortedBy {
                it.duration
            }
        }
    }

    private val _filteredLists = MutableStateFlow<List<Song>>(emptyList())
    val filteredList: StateFlow<List<Song>> = _filteredLists.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        filterNotes()
    }

    private fun filterNotes() {
        val query = _searchQuery.value.lowercase()
        if (query.isBlank()) return

        val filteredList = _sortedList.value.filter { noteEntry ->
            noteEntry.title.lowercase().contains(query)
            noteEntry.title.lowercase().contains(query)
        }

        _filteredLists.update {
            filteredList
        }
    }

}