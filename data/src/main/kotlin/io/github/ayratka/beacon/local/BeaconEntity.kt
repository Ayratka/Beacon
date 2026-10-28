package io.github.ayratka.beacon.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "beacons")
data class BeaconEntity(
    @PrimaryKey val id: String,
    val name: String,
    val url: String,
    val source: String,
    val extractorType: String,
    val extractorValue: String,
    val conditionType: String,
    val conditionValue: Double?,
    val conditionText: String?,
    val intervalMinutes: Int,
    val enabled: Boolean,
)