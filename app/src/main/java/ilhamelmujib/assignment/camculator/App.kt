package ilhamelmujib.assignment.camculator

import android.app.Application
import ilhamelmujib.assignment.camculator.di.appModule
import ilhamelmujib.assignment.camculator.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@App)
            modules(listOf(appModule, viewModelModule))
        }
    }
}