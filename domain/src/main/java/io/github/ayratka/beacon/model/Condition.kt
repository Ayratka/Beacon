package io.github.ayratka.beacon.model

sealed interface Condition {
    data class Below(val threshold: Double) : Condition
    data class Above(val threshold: Double) : Condition
    data class Contains(val text: String) : Condition
    data object Changed : Condition
}