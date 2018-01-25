package com.yichiuan.moedict.common

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT
import android.media.MediaPlayer
import android.os.Build
import timber.log.Timber
import java.io.IOException

class VoicePlayer(context: Context, lifecycle: Lifecycle) : AudioManager.OnAudioFocusChangeListener {

    internal val mediaPlayer = MediaPlayer()

    internal val audioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val audioAttributes: AudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()

    private var onLoadedListener: OnLoadedListener? = null

    val isPlaying: Boolean
        get() = mediaPlayer.isPlaying

    init {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediaPlayer.setAudioAttributes(audioAttributes)
        } else {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        }

        mediaPlayer.setOnCompletionListener { mp -> audioManager.abandonAudioFocus(this) }

        lifecycle.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            internal fun onStop() {
                stopVoice()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            internal fun onRelease() {
                mediaPlayer.release()
            }
        })
    }

    fun playVoice(url: String) {

        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(url)

            mediaPlayer.setOnPreparedListener { mp ->
                val result = audioManager.requestAudioFocus(this,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer.start()
                    if (onLoadedListener != null) {
                        onLoadedListener!!.onLoaded()
                    }
                }
            }

            mediaPlayer.prepareAsync()
        } catch (e: IOException) {
            Timber.e(e)
        }

    }

    fun stopVoice() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            audioManager.abandonAudioFocus(this)
        }
    }

    override fun onAudioFocusChange(focusChange: Int) {
        if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT) {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            mediaPlayer.start()
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            audioManager.abandonAudioFocus(this)
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
        }
    }

    fun setOnLoadedListener(onLoadedListener: OnLoadedListener) {
        this.onLoadedListener = onLoadedListener
    }

    interface OnLoadedListener {
        fun onLoaded()
    }
}
