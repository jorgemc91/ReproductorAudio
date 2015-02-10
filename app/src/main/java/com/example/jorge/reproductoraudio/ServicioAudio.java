package com.example.jorge.reproductoraudio;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;

import java.io.IOException;

/**
 * Created by Jorge on 08/02/2015.
 */
public class ServicioAudio extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener {

    private MediaPlayer mp;
    private enum Estados{
        idle,
        initialized,
        preparing,
        prepared,
        started,
        paused,
        completed,
        stopped,
        end,
        error
    };
    private Estados estado;
    public static final String PLAY="play";
    public static final String STOP="stop";
    public static final String ADD="add";
    public static final String PAUSE="pause";
    private String rutaCancion=null;
    private boolean reproducir;

    /* ******************************************************* */
    // METODOS SOBREESCRITOS //
    /* ****************************************************** */

    @Override
    public void onCreate() {
        super.onCreate();
        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int r = am.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if(r==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
            // normal
            addCancion();
        } else {
            stopSelf();
        }
    }

    public void addCancion(){
        mp = new MediaPlayer();
        mp.setOnPreparedListener(this);
        mp.setOnCompletionListener(this);
        mp.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
        estado = Estados.idle;
    }

    @Override
    public void onDestroy() {
        mp.release();
        mp = null;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        String dato = intent.getStringExtra("cancion");
        if(action.equals(PLAY)){
            play();
        }else if(action.equals(ADD)){
            add(dato);
        }else if(action.equals(STOP)){
            stop();
        }else if(action.equals(PAUSE)) {
            pause();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    /* ******************************************************* */
    // INTERFAZ PREPARED LISTENER //
    /* ****************************************************** */

    @Override
    public void onPrepared(MediaPlayer mp) {
        estado = Estados.prepared;
        if(reproducir){
            mp.start();
            estado = Estados.started;
        }
    }

    /* ******************************************************* */
    // INTERFAZ COMPLETED LISTENER //
    /* ****************************************************** */

    @Override
    public void onCompletion(MediaPlayer mp) {
        estado = Estados.completed;
    }

    /* ******************************************************* */
    // INTERFAZ AUDIO FOCUS CHANGED //
    /* ****************************************************** */

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                play();
                mp.setVolume(1.0f, 1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                mp.setVolume(0.1f, 0.1f);
                break;
        }
    }

    /* ******************************************************* */
    // METODOS DE AUDIO //
    /* ****************************************************** */

    private void play(){
        if(rutaCancion != null){
            if(estado == Estados.error){
                estado = Estados.idle;
            }
            if(estado == Estados.idle){
                reproducir = true;
                try {
                    mp.setDataSource(rutaCancion);
                    estado = Estados.initialized;
                } catch (IOException e) {
                    estado= Estados.error;
                }
            }
            if(estado == Estados.initialized ||
                    estado == Estados.stopped){
                reproducir = true;
                mp.prepareAsync();
                estado = Estados.preparing;
            } else if(estado == Estados.preparing) {
                reproducir = true;
            }
            if(estado == Estados.prepared ||
                    estado == Estados.paused ||
                    estado == Estados.completed ||
                    estado == Estados.started) {
                mp.start();
                estado = Estados.started;
            }
        }
    }

    private void stop(){
        if(estado == Estados.prepared ||
                estado == Estados.started ||
                estado == Estados.paused ||
                estado == Estados.completed){
            mp.seekTo(0); // Para volver al principio
            mp.stop();
            estado = Estados.stopped;
        }
        reproducir = false;
    }

    private void pause() {
        if(estado == Estados.paused ||
                estado == Estados.started) {
            mp.pause();
            estado = Estados.paused;
        }
    }

    private void add(String cancion){
        this.rutaCancion = cancion;
        addCancion();
    }
}
