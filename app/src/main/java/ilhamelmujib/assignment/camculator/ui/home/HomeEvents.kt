package ilhamelmujib.assignment.camculator.ui.home

import ilhamelmujib.assignment.camculator.persistence.entity.Scan

sealed class HomeEvents {
    object ShowPermissionInfo: HomeEvents()
    object ShowScanLoading: HomeEvents()
    object ShowScanEmpty: HomeEvents()
    data class ShowScanError(val message: String? = null): HomeEvents()
    data class ShowScanSuccess(val scan: Scan): HomeEvents()
}