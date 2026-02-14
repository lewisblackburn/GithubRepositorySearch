package com.example.githubrepositorysearch.domain.repository

import com.example.githubrepositorysearch.domain.model.Repository

interface GithubRepository {
    suspend fun searchRepositories(query: String): Result<List<Repository>>
}