package io.github.ayratka.beacon.remote

import io.ktor.client.HttpClient
import io.github.ayratka.beacon.fetcher.BlockReason
import io.github.ayratka.beacon.fetcher.FetchResult
import io.github.ayratka.beacon.fetcher.Fetcher
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CancellationException
import java.io.IOException

class KtorJsonFetcher(
    private val client: HttpClient,
) : Fetcher {

    override suspend fun fetch(url: String): FetchResult = try {
        val response: HttpResponse = client.get(url)
        response.toFetchResult()
    } catch (e: CancellationException) {
        throw e
    } catch (e: IOException) {
        FetchResult.NetworkError(e)
    } catch (e: Throwable) {
        FetchResult.NetworkError(e)
    }

    private suspend fun HttpResponse.toFetchResult(): FetchResult = when (status) {
        HttpStatusCode.OK -> FetchResult.Success(
            content = bodyAsText(),
            statusCode = status.value,
        )

        HttpStatusCode.TooManyRequests -> FetchResult.RateLimited(
            retryAfterSeconds = parseRetryAfter(),
        )

        HttpStatusCode.Forbidden,
        HttpStatusCode.Unauthorized -> FetchResult.Blocked(BlockReason.FORBIDDEN)

        else -> FetchResult.NetworkError(
            IOException("Unexpected status: ${status.value} ${status.description}")
        )
    }

    private fun HttpResponse.parseRetryAfter(): Long? =
        headers[HttpHeaders.RetryAfter]?.toLongOrNull()
}