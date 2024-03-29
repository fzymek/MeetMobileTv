package meet.mobile.tv.activity;


import android.annotation.TargetApi;
import android.app.Activity;
import android.media.MediaCodec;
import android.media.MediaCodec.CryptoException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.FrameworkSampleSource;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecTrackRenderer.DecoderInitializationException;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.SampleSource;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.VideoSurfaceView;
import com.google.android.exoplayer.util.PlayerControl;

import meet.mobile.R;
import meet.mobile.model.Image;

/**
 * An activity that plays media using {@link ExoPlayer}.
 */
public class TvPlayerActivity extends Activity implements SurfaceHolder.Callback,
        ExoPlayer.Listener, MediaCodecVideoTrackRenderer.EventListener {


    public static final int RENDERER_COUNT = 2;

    private static final String TAG = "PlayerActivity";

    private Image mVideo;

    private MediaController mediaController;
    private View shutterView;
    private VideoSurfaceView surfaceView;

    private ExoPlayer player;
    private MediaCodecVideoTrackRenderer videoRenderer;

    private boolean autoPlay = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_player);

        mVideo = getIntent().getParcelableExtra(Image.INTENT_EXTRA_IMAGE);

        Log.d("PlayerActivity", "mVideo: " + mVideo);
        View root = findViewById(R.id.root);
        mediaController = new MediaController(this);

        //overscan safe on 1980 * 1080 TV
        mediaController.setPadding(48, 27, 48, 27);
        mediaController.setAnchorView(root);
        shutterView = findViewById(R.id.shutter);
        surfaceView = (VideoSurfaceView) findViewById(R.id.surface_view);
        surfaceView.getHolder().addCallback(this);

        preparePlayer();
    }

    private void preparePlayer() {

        SampleSource sampleSource =
                new FrameworkSampleSource(this, Uri.parse(mVideo.getVideoUrl()), /* headers */ null, RENDERER_COUNT);

        // Build the track renderers
        videoRenderer = new MediaCodecVideoTrackRenderer(sampleSource, MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        TrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource);


        // Setup the player
        player = ExoPlayer.Factory.newInstance(RENDERER_COUNT, 1000, 5000);
        player.addListener(this);
        // Build the player controls
        mediaController.setMediaPlayer(new PlayerControl(player));
        mediaController.setEnabled(true);
        player.prepare(videoRenderer, audioRenderer);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPause() {
        requestVisibleBehind(true);
        super.onPause();
    }

    @Override
    public void onVisibleBehindCanceled() {
        super.onVisibleBehindCanceled();
        if (player != null) {
            player.release();
            player = null;
        }
        videoRenderer = null;
        shutterView.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
            player = null;
        }
        videoRenderer = null;
        shutterView.setVisibility(View.VISIBLE);

    }

    private void maybeStartPlayback() {
        Log.d(TAG, "maybeStartPlayback");
        Surface surface = surfaceView.getHolder().getSurface();
        if (videoRenderer == null || surface == null || !surface.isValid()) {
            // We're not ready yet.
            return;
        }
        player.sendMessage(videoRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, surface);
        if (autoPlay) {
            player.setPlayWhenReady(true);
            autoPlay = false;
        }
    }


    private void onError(Exception e) {
        Log.e(TAG, "Playback failed", e);
        Toast.makeText(this, "Playback failed", Toast.LENGTH_SHORT).show();
        finish();
    }

    // ExoPlayer.Listener implementation

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Log.d(TAG, "player state " + playbackState);
        if (playbackState == ExoPlayer.STATE_READY) {
            shutterView.setVisibility(View.GONE);
            mediaController.show(0);
        }

    }

    @Override
    public void onPlayWhenReadyCommitted() {
        // Do nothing.
    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {
        onError(e);
    }

    @Override
    public void onVideoSizeChanged(int width, int height) {
        surfaceView.setVideoWidthHeightRatio(height == 0 ? 1 : (float) width / height);
    }

    @Override
    public void onDrawnToSurface(Surface surface) {
        shutterView.setVisibility(View.GONE);
        mediaController.show(0);

    }

    @Override
    public void onDroppedFrames(int count, long elapsed) {
        Log.d(TAG, "Dropped frames: " + count);
    }

    @Override
    public void onDecoderInitializationError(DecoderInitializationException e) {
        // This is for informational purposes only. Do nothing.
    }

    @Override
    public void onCryptoError(CryptoException e) {
        // This is for informational purposes only. Do nothing.
    }

    // SurfaceHolder.Callback implementation

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "maybeStartPlayback");
        maybeStartPlayback();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Do nothing.
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (videoRenderer != null) {
            player.blockingSendMessage(videoRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, null);
        }
    }
}