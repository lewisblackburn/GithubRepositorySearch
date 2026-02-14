package com.example.githubrepositorysearch.domain.model

data class Repository(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val stars: Int,
    val forks: Int,
    val language: String?,
    val htmlUrl: String,
    val ownerLogin: String,
    val ownerAvatarUrl: String
)
