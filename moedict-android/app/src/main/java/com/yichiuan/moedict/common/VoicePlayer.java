package com.yichiuan.moedict.common;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;

import java.io.IOException;

import timber.log.Timber;

import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;

public class VoicePlayer {

    MediaPlayer mediaPlayer = new MediaPlayer();

    AudioManager audioManager;

    AudioAttributes audioAttributes;

    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener;

    OnLoadedListener onLoadedListener;

    public VoicePlayer(Context context, Lifecycle lifecycle) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mediaPlayer.setOnCompletionListener(mp -> {
            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        });

        lifecycle.addObserver(new LifecycleObserver() {

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            void onStop() {
                stopVoice();
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            void onRelease() {
                mediaPlayer.release();
            }
        });
    }

    public void playVoice(String url) {

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (audioAttributes == null) {
                    audioAttributes = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build();
                }

                mediaPlayer.setAudioAttributes(audioAttributes);
            }

            mediaPlayer.setOnPreparedListener(mp -> {
                initAudioFocusChangeListener();

                int result = audioManager.requestAudioFocus(onAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer.start();
                    if (onLoadedListener != null) {
                        onLoadedListener.onLoaded();
                    }
                }
            });

            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Timber.e(e);
        }
    }

    public void stopVoice() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    private void initAudioFocusChangeListener() {
        if (onAudioFocusChangeListener == null) {
            onAudioFocusChangeListener = focusChange -> {
                if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                } else if(focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                    mediaPlayer.start();
                } else if(focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                    audioManager.abandonAudioFocus(onAudioFocusChangeListener);
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                }
            };
        }
    }

    public void setOnLoadedListener(OnLoadedListener onLoadedListener) {
        this.onLoadedListener = onLoadedListener;
    }

    public interface OnLoadedListener {
        void onLoaded();
    }
}
