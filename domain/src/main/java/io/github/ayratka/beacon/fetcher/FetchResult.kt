package io.github.ayratka.beacon.fetcher

sealed interface FetchResult {
    data class Success(val content: String, val statusCode: Int) : FetchResult
    data class RateLimited(val retryAfterSeconds: Long?) : FetchResult
    data class Blocked(val reason: BlockReason) : FetchResult
    data class NetworkError(val cause: Throwable) : FetchResult
}

enum class BlockReason {
    FORBIDDEN,
    ROBOTS_DISALLOWED,
    CAPTCHA,
    UNKNOWN,
}