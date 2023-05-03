package ilhamelmujib.assignment.camculator.di

import ilhamelmujib.assignment.camculator.service.ScanTextFromImageService
import org.koin.dsl.module

private fun provideScanTextFromImageService() = ScanTextFromImageService()

val appModule = module {
    factory { provideScanTextFromImageService() }
}