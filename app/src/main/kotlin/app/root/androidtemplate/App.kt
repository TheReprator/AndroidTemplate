package app.root.androidtemplate

import androidx.multidex.MultiDexApplication
import androidx.work.Configuration
import app.root.androidtemplate.appinitializers.AppInitializers
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : MultiDexApplication(), Configuration.Provider {

    @Inject
    lateinit var initializers: AppInitializers

    @Inject
    lateinit var configuration: Configuration

    override fun onCreate() {
        super.onCreate()

        initializers.init(this)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return configuration
    }
}
