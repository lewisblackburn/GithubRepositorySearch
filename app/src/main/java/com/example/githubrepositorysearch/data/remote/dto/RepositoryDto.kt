package com.example.githubrepositorysearch.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RepositoryDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("description") val description: String?,
    @SerializedName("stargazers_count") val stars: Int,
    @SerializedName("forks_count") val forks: Int,
    @SerializedName("language") val language: String?,
    @SerializedName("html_url") val htmlUrl: String,
    @SerializedName("owner") val owner: OwnerDto
)

data class OwnerDto(
    @SerializedName("login") val login: String,
    @SerializedName("avatar_url") val avatarUrl: String
)