package io.github.ayratka.beacon.remote

import io.github.ayratka.beacon.fetcher.BlockReason
import io.github.ayratka.beacon.fetcher.FetchResult
import io.github.ayratka.beacon.remote.HttpClientFactory
import io.github.ayratka.beacon.remote.KtorJsonFetcher
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.IOException
//import kotlin.test.assertEquals
//import kotlin.test.assertIs

class KtorJsonFetcherTest {

    @Test
    fun `returns Success on 200`() = runTest {
        val fetcher = fetcherReturning(
            status = HttpStatusCode.OK,
            body = """{"key":"value"}""",
        )

        val result = fetcher.fetch("https://example.com/data")

        val success = assertIs<FetchResult.Success>(result)
        assertEquals(200, success.statusCode)
        assertEquals("""{"key":"value"}""", success.content)
    }

    @Test
    fun `returns RateLimited with retryAfter on 429`() = runTest {
        val fetcher = fetcherReturning(
            status = HttpStatusCode.TooManyRequests,
            headers = headersOf(HttpHeaders.RetryAfter, "60"),
        )

        val result = fetcher.fetch("https://example.com/data")

        val limited = assertIs<FetchResult.RateLimited>(result)
        assertEquals(60L, limited.retryAfterSeconds)
    }

    @Test
    fun `returns RateLimited without retryAfter when header missing`() = runTest {
        val fetcher = fetcherReturning(status = HttpStatusCode.TooManyRequests)

        val result = fetcher.fetch("https://example.com/data")

        val limited = assertIs<FetchResult.RateLimited>(result)
        assertEquals(null, limited.retryAfterSeconds)
    }

    @Test
    fun `returns Blocked on 403`() = runTest {
        val fetcher = fetcherReturning(status = HttpStatusCode.Forbidden)

        val result = fetcher.fetch("https://example.com/data")

        val blocked = assertIs<FetchResult.Blocked>(result)
        assertEquals(BlockReason.FORBIDDEN, blocked.reason)
    }

    @Test
    fun `returns Blocked on 401`() = runTest {
        val fetcher = fetcherReturning(status = HttpStatusCode.Unauthorized)

        val result = fetcher.fetch("https://example.com/data")

        val blocked = assertIs<FetchResult.Blocked>(result)
        assertEquals(BlockReason.FORBIDDEN, blocked.reason)
    }

    @Test
    fun `returns NetworkError when engine throws IOException`() = runTest {
        val engine = MockEngine { throw IOException("no connection") }
        val fetcher = KtorJsonFetcher(HttpClientFactory.create(engine))

        val result = fetcher.fetch("https://example.com/data")

        assertIs<FetchResult.NetworkError>(result)
    }

    @Test
    fun `returns NetworkError on unexpected status`() = runTest {
        val fetcher = fetcherReturning(status = HttpStatusCode.InternalServerError)

        val result = fetcher.fetch("https://example.com/data")

        assertIs<FetchResult.NetworkError>(result)
    }

    private fun fetcherReturning(
        status: HttpStatusCode,
        body: String = "",
        headers: Headers = Headers.Empty,
    ) = KtorJsonFetcher(
        HttpClientFactory.create(
            engine = MockEngine { request ->
                respond(
                    content = body,
                    status = status,
                    headers = headers,
                )
            }
        )
    )
}