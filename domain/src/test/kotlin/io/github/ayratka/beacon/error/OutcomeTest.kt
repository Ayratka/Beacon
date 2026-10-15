package io.github.ayratka.beacon.error

import junit.framework.TestCase.assertEquals
import org.junit.Test

class OutcomeTest {

    @Test
    fun `map transforms success value`() {
        val outcome: Outcome<Int> = Outcome.Success(5)

        val mapped = outcome.map { it * 2 }

        assertEquals(Outcome.Success(10), mapped)
    }

    @Test
    fun `map keeps failure unchanged`() {
        val outcome: Outcome<Int> = Outcome.Failure(DomainError.Network)

        val mapped = outcome.map { it * 2 }

        when (mapped) {
            is Outcome.Failure -> assertEquals(DomainError.Network, mapped.error)
            is Outcome.Success -> error("Expected Failure, got $mapped")
        }
    }
}