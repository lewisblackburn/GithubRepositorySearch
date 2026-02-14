package com.example.githubrepositorysearch.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SearchResponseDto(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("items") val items: List<RepositoryDto>
)
