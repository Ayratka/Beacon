package io.github.ayratka.beacon.fetcher

interface Fetcher {
    suspend fun fetch(url: String): FetchResult
}