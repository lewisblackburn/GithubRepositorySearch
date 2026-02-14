package com.example.githubrepositorysearch.presentation.search

import com.example.githubrepositorysearch.domain.model.Repository

sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Loading : SearchUiState
    data class Success(val repositories: List<Repository>) : SearchUiState
    data class Error(val message: String) : SearchUiState
}