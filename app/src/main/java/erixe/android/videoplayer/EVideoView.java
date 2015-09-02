package erixe.android.videoplayer;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by emad on 8/13/2015.
 */
public class EVideoView extends LinearLayout implements EMediaPlayer.OnEMediaPlayerPreparedListener{

    public static final int EVIDEO_VIEW_NOT_STARTED = 0;
    public static final int EVIDEO_VIEW_LOADING = 1;
    public static final int EVIDEO_VIEW_PLAYING = 2;
    public static final int EVIDEO_VIEW_PAUSED = 3;
    public static final int EVIDEO_VIEW_FINISHED = 4;

    int EVIDEO_VIEW_MEDIA_LAYOUT_NORMAL_HEIGHT, EVIDEO_VIEW_MEDIA_LAYOUT_NORMAL_WIDTH;

    Context context;
    public EVideoViewHolder vh;
    public EMediaPlayer eMediaPlayer = new EMediaPlayer(this);
    private Handler durationHandler = new Handler();

    public boolean layoutHeightReadyState, layoutBeingAnimated, playMediaAfterPrepared;
    private double timeElapsed = 0;

    public EVideoView(Context context) {
        super(context);
        if(!isInEditMode()){
            initializeViews(context, true);
        }
    }

    public EVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode()) {
            initializeViews(context, false);
        }
    }

    public EVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context, false);
    }

    private void initializeViews(Context context, boolean callFromFirstConstructor) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.e_video_view_layout, this);
        if(callFromFirstConstructor)
            onFinishInflate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        vh = new EVideoViewHolder();
        vh.eVideoViewLayout = this;
        vh.eVideoViewMediaLayout = (RelativeLayout) this.findViewById(R.id.EVideoViewMediaLayout);
        vh.eVideoViewTextureView = (TextureView) this.findViewById(R.id.EVideoViewTextureView);
        vh.eVideoViewBottomLayout = (LinearLayout) this.findViewById(R.id.EVideoViewBottomLayout);
        vh.eVideoViewPlayPauseButton = (ImageButton) this.findViewById(R.id.EVideoViewPlayPauseButton);
        vh.eVideoViewSeekBar = (SeekBar) this.findViewById(R.id.EVideoViewSeekBar);
        vh.eVideoViewDurationTextView = (TextView) this.findViewById(R.id.EVideoViewDurationTextView);
        vh.eVideoViewContentLayout = (LinearLayout) this.findViewById(R.id.EVideoViewContentLayout);

        //call the object that the height values are set
        ViewTreeObserver vto = vh.eVideoViewMediaLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                vh.eVideoViewMediaLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                ViewGroup.LayoutParams lp = vh.eVideoViewMediaLayout.getLayoutParams();
                lp.height = (vh.eVideoViewMediaLayout.getMeasuredWidth() * 9) / 16;
                vh.eVideoViewMediaLayout.setLayoutParams(lp);

                layoutHeightReadyState = true;
                EVIDEO_VIEW_MEDIA_LAYOUT_NORMAL_HEIGHT = vh.eVideoViewMediaLayout.getLayoutParams().height;
                EVIDEO_VIEW_MEDIA_LAYOUT_NORMAL_WIDTH = vh.eVideoViewMediaLayout.getMeasuredWidth();
            }
        });

        setEVideoViewPlayPauseButtonOnClickListener();
    }

    public void setVideoPlayerSurface() {
        if (eMediaPlayer.mediaPath != null && eMediaPlayer.mediaPath != "") {
            if(vh.eVideoViewTextureView.isAvailable() == true)
            {
                Surface mSurface = new Surface(vh.eVideoViewTextureView.getSurfaceTexture());

                if (eMediaPlayer.state > 1)
                {
                    eMediaPlayer.setSurface(mSurface);
                    if(eMediaPlayer.state == EMediaPlayer.MEDIA_PAUSE) {
                        eMediaPlayer.seekTo((int) timeElapsed);
                        eMediaPlayer.start();
                        eMediaPlayer.pause();
                    }
                }
                else if (eMediaPlayer.state == 0)
                {
                    eMediaPlayer.state = 1;
//                    Uri uri = Uri.parse(eMediaPlayer.mediaPath);
                    try {
//                        eMediaPlayer.setDataSource(context, uri);
                        eMediaPlayer.state = EMediaPlayer.MEDIA_NOT_PREPARED;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    eMediaPlayer.setLooping(false);
                    eMediaPlayer.setSurface(mSurface);
                    eMediaPlayer.prepareMedia();
                }
            }
            else {
                vh.eVideoViewTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                    @Override
                    public void onSurfaceTextureAvailable(SurfaceTexture surface, final int width, int height) {
                        Surface mSurface = new Surface(surface);

                        if (eMediaPlayer.state > 1) {
                            eMediaPlayer.setSurface(mSurface);
                            if (eMediaPlayer.state == EMediaPlayer.MEDIA_PAUSE) {
                                eMediaPlayer.seekTo((int) timeElapsed);
                                eMediaPlayer.start();
                                eMediaPlayer.pause();
                            }
                        } else if (eMediaPlayer.state == 0) {
                            eMediaPlayer.state = 1;
//                            Uri uri = Uri.parse(eMediaPlayer.mediaPath);
                            try {
//                                eMediaPlayer.setDataSource(context, uri);
                                eMediaPlayer.state = EMediaPlayer.MEDIA_NOT_PREPARED;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            eMediaPlayer.setLooping(false);
                            eMediaPlayer.setSurface(mSurface);
                            eMediaPlayer.prepareMedia();
                        }
                    }

                    @Override
                    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

                    }

                    @Override
                    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                        return false;
                    }

                    @Override
                    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

                    }
                });
            }
        }
    }

    public void setEVideoViewPlayPauseButtonOnClickListener()
    {
        vh.eVideoViewPlayPauseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eMediaPlayer.state == EMediaPlayer.MEDIA_PAUSE || eMediaPlayer.state == EMediaPlayer.MEDIA_PREPARED) {
                    playMedia();
                }
                else if (eMediaPlayer.state == EMediaPlayer.MEDIA_PLAY) {
                    pauseMedia();
                }
            }
        });
    }

    private void setVideoViewDurationTextViewText(double timeElapsed)
    {
        String minStr, secondStr;
        int min = (int) (timeElapsed / 60000);
        int second = (int) ((timeElapsed - min * 60000) / 1000);

        if (min < 10)
            minStr = "0" + min;
        else
            minStr = String.valueOf(min);

        if (second < 10)
            secondStr = "0" + second;
        else
            secondStr = String.valueOf(second);

        String finalTime = String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed),
                TimeUnit.MILLISECONDS.toSeconds((long) timeElapsed) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed)));

        finalTime = minStr + ":" + secondStr;
        vh.eVideoViewDurationTextView.setText(finalTime);
    }

    public void setMediaPath(String path) throws IOException {
        eMediaPlayer.setMediaPath(path);
    }

    public void prepareMedia(boolean playMediaAfterPrepared)
    {
        this.playMediaAfterPrepared = playMediaAfterPrepared;
        setVideoPlayerSurface();
    }

    public void playMedia()
    {
        if(eMediaPlayer.state >= EMediaPlayer.MEDIA_PREPARED) {
            eMediaPlayer.playMedia();
            timeElapsed = eMediaPlayer.getCurrentPosition();
            vh.eVideoViewSeekBar.setProgress((int) timeElapsed);
            vh.eVideoViewSeekBar.setMax(eMediaPlayer.getDuration());
            durationHandler.postDelayed(updateSeekBarAndDuration, 100);
        }
        else
        {
            playMediaAfterPrepared = true;
            eMediaPlayer.prepareMedia();
        }
    }

    public void pauseMedia()
    {
        eMediaPlayer.pauseMedia();
    }

    private void onConfigurationChanged(){
        vh.eVideoViewSeekBar.setProgress((int)timeElapsed);
        vh.eVideoViewSeekBar.setMax(eMediaPlayer.getDuration());
        durationHandler.postDelayed(updateSeekBarAndDuration, 100);
    }

    private Runnable updateSeekBarAndDuration = new Runnable() {
        public void run() {
            timeElapsed = eMediaPlayer.getCurrentPosition();
            vh.eVideoViewSeekBar.setProgress((int) timeElapsed);
            setVideoViewDurationTextViewText(timeElapsed);
            durationHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if(!layoutHeightReadyState) {
            ViewGroup.LayoutParams lp = vh.eVideoViewTextureView.getLayoutParams();
            ViewGroup.LayoutParams lp2 = vh.eVideoViewMediaLayout.getLayoutParams();

            lp.height = (vh.eVideoViewTextureView.getMeasuredWidth() * 9) / 16;
            lp2.height = lp.height;

            vh.eVideoViewTextureView.setLayoutParams(lp);
            vh.eVideoViewMediaLayout.setLayoutParams(lp2);
        }
//
//        if(!layoutBeingAnimated)
//        {
//            ViewGroup.LayoutParams lp = vh.eVideoViewTextureView.getLayoutParams();
//            ViewGroup.LayoutParams lp2 = vh.eVideoViewMediaLayout.getLayoutParams();
//
//            lp.height = h;
//            lp.width = (h * 16) / 9;
//            lp.height = h;
//            lp2.width = (h * 16) / 9;
//
//            vh.eVideoViewTextureView.setLayoutParams(lp);
//            vh.eVideoViewMediaLayout.setLayoutParams(lp2);
//        }
    }

    @Override
    public void onEMediaPlayerPrepared() {
        if(playMediaAfterPrepared)
            playMedia();
    }

    private static class EVideoViewHolder implements Serializable
    {
        LinearLayout eVideoViewLayout;
        RelativeLayout eVideoViewMediaLayout;
        TextureView eVideoViewTextureView;
        LinearLayout eVideoViewBottomLayout;
        ImageButton eVideoViewPlayPauseButton;
        SeekBar eVideoViewSeekBar;
        TextView eVideoViewDurationTextView;
        LinearLayout eVideoViewContentLayout;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end

        Bundle bundle = new Bundle();
        bundle.putDouble("timeElapsed", timeElapsed);
        bundle.putSerializable("eMediaPlayer", eMediaPlayer);

        ss.stateToSave = bundle;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        //begin boilerplate code so parent classes can restore state
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());
        //end

        Bundle bundle = ss.stateToSave;
        this.timeElapsed = bundle.getDouble("timeElapsed");
        this.eMediaPlayer = (EMediaPlayer)bundle.getSerializable("eMediaPlayer");
//        setOnInteractionWithVideoViewListener((OnLayoutHeightReadyStateListener)context);
        onConfigurationChanged();
    }

    static class SavedState extends BaseSavedState {
        Bundle stateToSave;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.stateToSave = in.readBundle();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeBundle(this.stateToSave);
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
