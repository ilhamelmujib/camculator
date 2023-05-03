package ilhamelmujib.assignment.camculator.persistence.dao

import androidx.room.*
import ilhamelmujib.assignment.camculator.persistence.entity.Scan
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanDao {
    @Query("SELECT * FROM scan ORDER BY created_at DESC")
    fun getScans(): Flow<List<Scan>>

    @Insert
    suspend fun insertScan(scan: Scan): Long

    @Delete
    suspend fun deleteScan(scan: Scan)

}