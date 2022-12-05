package app.flashlight.core

import android.hardware.camera2.CameraManager
import android.util.Log
import app.flashlight.data.Mode
import app.flashlight.data.Mode.Companion.toDelay
import app.flashlight.di.DefaultDispatcher
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class Flashlight @Inject constructor(
    private val cameraManager: CameraManager,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
) : CoroutineScope {

    override val coroutineContext: CoroutineContext = defaultDispatcher
    private var flickeringJob: Job? = null

    private var cameraId = cameraManager.cameraIdList[0]
    private var flashlightEnabled = false
    private var flashlightMode = Mode.DEFAULT_MODE
    private var torchEnabled = false

    init {
        cameraManager.registerTorchCallback(
            object : CameraManager.TorchCallback() {
                override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
                    super.onTorchModeChanged(cameraId, enabled)
                    torchEnabled = enabled
                }
            },
            null
        )
    }

    fun toggle(enabled: Boolean) {
        flashlightEnabled = enabled
        toggleIfNecessary()
    }

    fun setMode(mode: Mode) {
        flashlightMode = mode
        toggleIfNecessary()
    }

    private fun toggleIfNecessary() {

        flickeringJob?.cancel()

        if (flashlightEnabled) {
            Log.d(TAG, "start $flashlightMode")
            when (flashlightMode) {
                Mode.DEFAULT_MODE -> toggleTorch(true)
                else -> flickeringJob = launch {
                    while (true) {
                        toggleTorch(!torchEnabled)
                        delay(flashlightMode.toDelay())
                    }
                }
            }
        } else {
            Log.d(TAG, "stop")
            toggleTorch(false)
        }
    }

    private fun toggleTorch(enabled: Boolean) = try {
        cameraManager.setTorchMode(cameraId, enabled)
    } catch (e: Exception) {
        torchEnabled = false
    }

    private companion object {
        const val TAG = "FLASHLIGHT"
    }
}
