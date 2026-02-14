package com.example.githubrepositorysearch.data.remote

import com.example.githubrepositorysearch.data.remote.dto.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApiService {
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("sort") sort: String = "stars",
        @Query("order") order: String = "desc",
        @Query("per_page") perPage: Int = 30
    ): SearchResponseDto
}