package io.github.ayratka.beacon.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        BeaconEntity::class,
        BeaconCheckEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class BeaconDatabase : RoomDatabase() {
    abstract fun beaconDao(): BeaconDao
}