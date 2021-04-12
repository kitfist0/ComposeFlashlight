package app.flashlight.di

import android.app.Application
import app.flashlight.data.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun dataStoreManager(app: Application): DataStoreManager {
        return DataStoreManager(app)
    }
}