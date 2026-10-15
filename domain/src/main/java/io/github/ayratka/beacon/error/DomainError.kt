package io.github.ayratka.beacon.error

sealed interface DomainError {
    data object Network : DomainError
    data object Unauthorized : DomainError
    data class NotFound(val id: String) : DomainError
    data class Blocked(val reason: String) : DomainError
    data class Unknown(val cause: Throwable) : DomainError
}