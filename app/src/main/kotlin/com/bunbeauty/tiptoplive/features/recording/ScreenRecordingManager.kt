package com.bunbeauty.tiptoplive.features.recording

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Environment
import kotlinx.coroutines.delay
import java.io.File

private const val VIRTUAL_DISPLAY_NAME = "main"

class ScreenRecordingManager {

    private var mediaProjectionManager: MediaProjectionManager? = null
    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var mediaRecorder: MediaRecorder? = null
    var started = false
        private set

    suspend fun startRecording(
        context: Context,
        data: Intent,
        screenWidth: Int,
        screenHeight: Int,
    ) {
        val serviceIntent = Intent(context, RecordingService::class.java)
        context.startForegroundService(serviceIntent)

        delay(1_000)

        mediaProjectionManager = context.getSystemService(MediaProjectionManager::class.java)
        mediaRecorder = MediaRecorder().apply {
            setVideoSource(MediaRecorder.VideoSource.SURFACE)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(getOutputFile(context = context).absolutePath)
            setVideoSize(screenWidth, screenHeight)
            setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            setVideoEncodingBitRate(512 * 1000)
            setVideoFrameRate(30)
            prepare()
        }

        mediaProjection = mediaProjectionManager?.getMediaProjection(Activity.RESULT_OK, data)
        virtualDisplay = mediaProjection?.createVirtualDisplay(
            VIRTUAL_DISPLAY_NAME,
            screenWidth,
            screenHeight,
            context.resources.displayMetrics.densityDpi,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            mediaRecorder?.surface,
            null,
            null
        )
        mediaRecorder?.start()
        started = true
    }

    fun stopRecording() {
        // TODO shutdown RecordingService
        mediaRecorder?.stop()
        mediaRecorder?.reset()
        virtualDisplay?.release()
        mediaProjection?.stop()
        started = false
    }

    private fun getOutputFile(context: Context): File {
        // TODO update file saving
        val videoDir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
        return File(videoDir, "recording.mp4")
    }

}