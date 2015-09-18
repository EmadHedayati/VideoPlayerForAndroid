package erixe.android.videoplayer.Utilities;

import android.media.MediaPlayer;
import android.media.TimedText;
import android.util.Log;

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
    public String mediaPath = "http://video.erixe.com/videos/11/qualities/original.mp4";
    public String subtitlePath = "";
    transient OnEMediaPlayerPreparedListener callBack;

    public EMediaPlayer(OnEMediaPlayerPreparedListener onEMediaPlayerPreparedListener)
    {
        super();
        callBack = onEMediaPlayerPreparedListener;
    }

    public void setOnEMediaPlayerPreparedListener(OnEMediaPlayerPreparedListener onEMediaPlayerPreparedListener)
    {
        callBack = onEMediaPlayerPreparedListener;
    }

    public void setMediaPath(String path) {
//        this.setDataSource(path);
        this.reset();
        state = 0;
        try {
            this.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPath = path;
    }

    public void setMediaPath(String path, String subtitlePath) {
//        this.setDataSource(path);
        this.reset();
        state = 0;
        try {
            this.setDataSource(path);
            this.addTimedTextSource(subtitlePath, MediaPlayer.MEDIA_MIMETYPE_TEXT_SUBRIP);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPath = path;
        this.subtitlePath = subtitlePath;

        int textTrackIndex = findTrackIndexFor(
                MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT, this.getTrackInfo());
        if (textTrackIndex >= 0) {
            this.selectTrack(textTrackIndex);
        } else {
            Log.w("test", "Cannot find text track!");
        }

        this.setOnTimedTextListener(new MediaPlayer.OnTimedTextListener() {
            @Override
            public void onTimedText(MediaPlayer mediaPlayer, TimedText timedText) {
                if(timedText != null && callBack != null)
                    callBack.onTimedTextReady(timedText.getText().toString());
            }
        });
    }

    private int findTrackIndexFor(int mediaTrackType, MediaPlayer.TrackInfo[] trackInfo) {
        int index = -1;
        for (int i = 0; i < trackInfo.length; i++) {
            if (trackInfo[i].getTrackType() == mediaTrackType) {
                return i;
            }
        }
        return index;
    }

    public void prepareMedia() {
        if (state < MEDIA_PREPARING && state != MEDIA_NOT_LOADED) {
            this.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    state = MEDIA_PREPARED;
                    if(callBack != null)
                        callBack.onEMediaPlayerPrepared();
                }
            });
            state = MEDIA_PREPARING;
            this.prepareAsync();
        }
    }

    public void changeQualityTo(String path, final int seekTo) {
        this.reset();
        state = 0;
        try {
            this.setDataSource(mediaPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPath = path;

        this.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                state = MEDIA_PREPARED;
                seekTo(seekTo);
                EMediaPlayer.this.playMedia();

                if(callBack != null)
                    callBack.onQualityChanged();
            }
        });
        state = MEDIA_PREPARING;
        this.prepareAsync();
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
        void onQualityChanged();
        void onTimedTextReady(String newSubtitleText);
    }

}