package ilhamelmujib.assignment.camculator.persistence.repository

import ilhamelmujib.assignment.camculator.persistence.database.Database
import ilhamelmujib.assignment.camculator.persistence.entity.Scan

class ScanRepository(
    database: Database
) {
    private val dao = database.scanDao

    val getScans = dao.getScans()

    suspend fun insertScan(scan: Scan) = dao.insertScan(scan)
    suspend fun deleteScan(scan: Scan) = dao.deleteScan(scan)
}