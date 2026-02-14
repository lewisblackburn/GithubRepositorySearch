package com.example.githubrepositorysearch.domain.usecase

import com.example.githubrepositorysearch.domain.model.Repository
import com.example.githubrepositorysearch.domain.repository.GithubRepository
import javax.inject.Inject

class SearchRepositoriesUseCase @Inject constructor(
    private val repository: GithubRepository
) {
    suspend operator fun invoke(query: String): Result<List<Repository>> {
        if (query.isBlank()) return Result.success(emptyList())
        return repository.searchRepositories(query.trim())
    }
}