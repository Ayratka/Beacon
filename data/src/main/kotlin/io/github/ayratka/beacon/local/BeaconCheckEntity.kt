package io.github.ayratka.beacon.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "beacon_checks",
    foreignKeys = [
        ForeignKey(
            entity = BeaconEntity::class,
            parentColumns = ["id"],
            childColumns = ["beaconId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("beaconId")],
)
data class BeaconCheckEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val beaconId: String,
    val checkedAt: Long,
    val value: String,
    val conditionFired: Boolean,
)