package com.example.githubrepositorysearch.data.repository

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.githubrepositorysearch.data.remote.GithubApiService
import com.example.githubrepositorysearch.data.remote.dto.RepositoryDto
import com.example.githubrepositorysearch.domain.model.Repository
import com.example.githubrepositorysearch.domain.repository.GithubRepository
import java.io.IOException
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(
    private val apiService: GithubApiService
) : GithubRepository {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun searchRepositories(query: String): Result<List<Repository>> {
        return try {
            val response = apiService.searchRepositories(query)
            val repositories = response.items.map { it.toDomain() }
            Result.success(repositories)
        } catch (e: HttpException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: IOException) {
            Result.failure(Exception("No internet connection"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

fun RepositoryDto.toDomain() = Repository(
    id = id,
    name = name,
    fullName = fullName,
    description = description,
    stars = stars,
    forks = forks,
    language = language,
    htmlUrl = htmlUrl,
    ownerLogin = owner.login,
    ownerAvatarUrl = owner.avatarUrl
)