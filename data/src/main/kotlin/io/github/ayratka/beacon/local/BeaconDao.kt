package io.github.ayratka.beacon.local
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface BeaconDao {

    @Query("SELECT * FROM beacons ORDER BY name ASC")
    fun observeAll(): Flow<List<BeaconEntity>>

    @Query("SELECT * FROM beacons WHERE id = :id")
    fun observeById(id: String): Flow<BeaconEntity?>

    @Query("SELECT * FROM beacons WHERE id = :id")
    suspend fun getById(id: String): BeaconEntity?

    @Upsert
    suspend fun upsert(beacon: BeaconEntity)

    @Query("DELETE FROM beacons WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM beacons")
    suspend fun deleteAll()

    @Query("SELECT * FROM beacon_checks WHERE beaconId = :beaconId ORDER BY checkedAt DESC")
    fun observeChecksFor(beaconId: String): Flow<List<BeaconCheckEntity>>

    @Query("SELECT * FROM beacon_checks WHERE beaconId = :beaconId ORDER BY checkedAt DESC LIMIT 1")
    suspend fun getLatestCheck(beaconId: String): BeaconCheckEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCheck(check: BeaconCheckEntity): Long
}