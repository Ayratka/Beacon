package io.github.ayratka.beacon.model

import java.time.Instant

data class BeaconCheck(
    val beaconId: BeaconId,
    val checkedAt: Instant,
    val value: String,
    val conditionFired: Boolean,
)