package io.github.ayratka.beacon.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.ayratka.beacon.remote.HttpClientFactory
import io.github.ayratka.beacon.remote.KtorJsonFetcher
import io.github.ayratka.beacon.fetcher.Fetcher
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = HttpClientFactory.create()

    @Provides
    @Singleton
    fun provideFetcher(client: HttpClient): Fetcher = KtorJsonFetcher(client)
}