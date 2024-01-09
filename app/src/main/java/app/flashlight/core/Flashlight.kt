package app.flashlight.core

import android.hardware.camera2.CameraManager
import android.util.Log
import app.flashlight.data.DataStoreManager
import app.flashlight.data.Mode
import app.flashlight.data.Mode.Companion.toDelay
import app.flashlight.data.Timeout
import app.flashlight.data.Timeout.Companion.toMillis
import app.flashlight.di.MainDispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class Flashlight @Inject constructor(
    private val cameraManager: CameraManager,
    private val dataStoreManager: DataStoreManager,
    @MainDispatcher coroutineDispatcher: CoroutineDispatcher,
) : CoroutineScope {

    override val coroutineContext: CoroutineContext = coroutineDispatcher

    private var flashlightJob: Job? = null

    fun start() {
        combine(
            dataStoreManager.flashlightEnabled,
            dataStoreManager.mode,
            dataStoreManager.shutdownTimeout,
        ) { enabled, mode, timeout ->
            cancelFlashlightJob()
            if (enabled) {
                flashlightJob = cameraManager.launchFlashlightJob(mode, timeout)
            }
        }.launchIn(this)
    }

    fun stop() {
        cancelFlashlightJob()
        launch {
            dataStoreManager.setFlashlightEnabled(false)
        }
    }

    private fun cancelFlashlightJob() {
        val cameraId = cameraManager.cameraIdList.first()
        cameraManager.setTorchMode(cameraId, false)
        flashlightJob?.let { job ->
            Log.d(TAG, "cancel flashlight job")
            job.cancel()
            flashlightJob = null
        }
    }

    private fun CameraManager.launchFlashlightJob(mode: Mode, timeout: Timeout): Job {
        Log.d(TAG, "launch flashlight job in mode $mode for ${timeout.valueInMinutes} minutes")
        val cameraId = cameraIdList.first()
        val timeoutMillis = timeout.toMillis()
        return launch {
            if (mode == Mode.MODE_0) {
                setTorchMode(cameraId, true)
                delay(timeoutMillis)
            } else {
                val startTime = System.currentTimeMillis()
                var torchEnabled = true
                while (System.currentTimeMillis() - startTime < timeoutMillis) {
                    setTorchMode(cameraId, torchEnabled)
                    Log.d(TAG, "torch is " + if (torchEnabled) "on" else "off")
                    delay(mode.toDelay())
                    torchEnabled = !torchEnabled
                }
            }
            Log.d(TAG, "time is out")
            stop()
        }
    }

    private companion object {
        const val TAG = "FLASHLIGHT"
    }
}
