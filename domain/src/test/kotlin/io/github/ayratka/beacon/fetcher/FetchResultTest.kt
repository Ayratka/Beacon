package io.github.ayratka.beacon.fetcher

import org.junit.Assert.assertEquals
import org.junit.Test

class FetchResultTest {

    @Test
    fun `success results are equal when content and code match`() {
        val a = FetchResult.Success("body", 200)
        val b = FetchResult.Success("body", 200)
        assertEquals(a, b)
    }

    @Test
    fun `blocked results carry reason`() {
        val result = FetchResult.Blocked(BlockReason.ROBOTS_DISALLOWED)
        assertEquals(BlockReason.ROBOTS_DISALLOWED, result.reason)
    }
}