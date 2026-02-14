package com.example.githubrepositorysearch.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubrepositorysearch.domain.usecase.SearchRepositoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepositoriesUseCase: SearchRepositoriesUseCase
) : ViewModel() {

    var searchQuery by mutableStateOf("")
        private set

    var uiState by mutableStateOf<SearchUiState>(SearchUiState.Idle)
        private set

    private var searchJob: Job? = null

    fun onQueryChange(query: String) {
        searchQuery = query
        searchJob?.cancel()
        if (query.isBlank()) {
            uiState = SearchUiState.Idle
            return
        }
        searchJob = viewModelScope.launch {
            delay(500)
            search(query)
        }
    }

    fun onSearch(query: String) {
        searchJob?.cancel()
        viewModelScope.launch { search(query) }
    }

    private suspend fun search(query: String) {
        uiState = SearchUiState.Loading
        searchRepositoriesUseCase(query)
            .onSuccess { repos ->
                uiState = if (repos.isEmpty()) {
                    SearchUiState.Error("No repositories found for \"$query\"")
                } else {
                    SearchUiState.Success(repos)
                }
            }
            .onFailure { error ->
                uiState = SearchUiState.Error(error.message ?: "Unknown error")
            }
    }
}
