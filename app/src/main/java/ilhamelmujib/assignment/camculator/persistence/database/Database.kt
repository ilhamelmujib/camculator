package ilhamelmujib.assignment.camculator.persistence.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ilhamelmujib.assignment.camculator.persistence.dao.ScanDao
import ilhamelmujib.assignment.camculator.persistence.entity.Scan

@Database(
    entities = [
        Scan::class,
    ],
    version = 1
)
abstract class Database : RoomDatabase() {
    abstract val scanDao: ScanDao
}