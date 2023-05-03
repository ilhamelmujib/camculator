package ilhamelmujib.assignment.camculator.di

import ilhamelmujib.assignment.camculator.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        HomeViewModel(scanTextFromImageService = get())
    }
}