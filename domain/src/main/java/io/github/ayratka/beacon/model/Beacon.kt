package io.github.ayratka.beacon.model

data class Beacon(
    val id: BeaconId,
    val name: String,
    val url: String,
    val source: SourceType,
    val extractor: Extractor,
    val condition: Condition,
    val intervalMinutes: Int,
    val enabled: Boolean,
)