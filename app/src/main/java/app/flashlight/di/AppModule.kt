package app.flashlight.di

import android.app.Application
import android.content.Context
import android.hardware.camera2.CameraManager
import app.flashlight.data.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCameraManager(app: Application): CameraManager {
        return app.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    @Provides
    @Singleton
    fun dataStoreManager(app: Application): DataStoreManager {
        return DataStoreManager(app)
    }

    @DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher
