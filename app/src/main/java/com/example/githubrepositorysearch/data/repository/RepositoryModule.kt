package com.example.githubrepositorysearch.data.repository

import com.example.githubrepositorysearch.domain.repository.GithubRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGithubRepository(
        impl: GithubRepositoryImpl
    ): GithubRepository
}
