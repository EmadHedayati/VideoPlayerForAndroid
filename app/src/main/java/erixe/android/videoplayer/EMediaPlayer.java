package erixe.android.videoplayer;

import android.media.MediaPlayer;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by emad on 8/13/2015.
 */
public class EMediaPlayer extends MediaPlayer implements Serializable{

    public static final int MEDIA_NOT_LOADED = 0;
    public static final int MEDIA_NOT_PREPARED = 1;
    public static final int MEDIA_PREPARING = 2;
    public static final int MEDIA_PREPARED = 3;
    public static final int MEDIA_PAUSE = 4;
    public static final int MEDIA_PLAY = 5;
    public static final int MEDIA_FINISHED = 6;

    public int state = 0;
    public String mediaPath = "android.resource://com.example.emad.pagertest/raw/video";
    OnEMediaPlayerPreparedListener callBack;

    public EMediaPlayer(OnEMediaPlayerPreparedListener onEMediaPlayerPreparedListener)
    {
        super();
        callBack = onEMediaPlayerPreparedListener;
    }

    public void setMediaPath(String path) throws IOException {
//        this.setDataSource(path);
        this.reset();
        state = 0;
        this.setDataSource(path);
        mediaPath = path;
    }

    public void prepareMedia() {
        if (state < MEDIA_PREPARING && state != MEDIA_NOT_LOADED) {
            this.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    state = MEDIA_PREPARED;
                    seekTo(0);

                    if(callBack != null)
                        callBack.onEMediaPlayerPrepared();
                }
            });
            this.prepareAsync();
            state = MEDIA_PREPARING;
        }
    }

    public void playMedia() {
        if (state > MEDIA_PREPARING && state != MEDIA_PLAY) {
            this.start();
            state = MEDIA_PLAY;
        }
    }

    public void pauseMedia() {
        if (state > MEDIA_PREPARING && state != MEDIA_PAUSE) {
            this.pause();
            state = MEDIA_PAUSE;
        }
    }

    public interface OnEMediaPlayerPreparedListener
    {
        void onEMediaPlayerPrepared();
    }

}