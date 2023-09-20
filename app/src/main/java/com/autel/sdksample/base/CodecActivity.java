package com.autel.sdksample.base;

import android.media.MediaCodec;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.autel.manager.player.AutelPlayerManager;
import com.autel.manager.player.autelplayer.AutelPlayer;
import com.autel.manager.player.autelplayer.AutelPlayerView;
import com.autel.manager.player.codec.OnRenderFrameInfoListener;
import com.autel.sdk.product.BaseProduct;
import com.autel.sdk.video.AutelCodec;
import com.autel.sdksample.R;
import com.autel.util.log.AutelLog;

import java.nio.ByteBuffer;

public class CodecActivity extends BaseActivity<AutelCodec> {

    private RelativeLayout content_layout;
    private boolean isCodecing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Codec");
    }

    @Override
    protected AutelCodec initController(BaseProduct product) {
        return product.getCodec();
    }

    @Override
    protected int getCustomViewResId() {
        return R.layout.ac_codec;
    }

    @Override
    protected void initUi() {
        content_layout = (RelativeLayout) findViewById(R.id.content_layout);

        isCodecing = false;
        initClick();
    }

    public void testAutelCodecView(View v) {

    }



    private AutelPlayerView codecView;
    private AutelPlayer mAutelPlayer;

    private AutelPlayerView createAutelCodecView() {
        AutelPlayerView codecView = new AutelPlayerView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        codecView.setLayoutParams(params);
        return codecView;
    }

    /**
     * Use AutelCodecView to display the video stream from camera simply.
     */
    private void initClick() {
        findViewById(R.id.testAutelCodecView).setOnClickListener(v -> {
            isCodecing = true;
            stopPlayer();
            AutelPlayerManager.getInstance().init(CodecActivity.this, false);
            codecView = createAutelCodecView();
            content_layout.setOnClickListener(null);
            content_layout.setVisibility(View.VISIBLE);
            content_layout.addView(codecView);
            mAutelPlayer = new AutelPlayer(0);
            mAutelPlayer.addVideoView(codecView);
            AutelPlayerManager.getInstance().addAutelPlayer(mAutelPlayer);
            mAutelPlayer.startPlayer();
            LinearLayout btn_layout = new LinearLayout(CodecActivity.this);
            btn_layout.setOrientation(LinearLayout.VERTICAL);


            Button btn_pause = new Button(CodecActivity.this);
            btn_pause.setText("Pause");
            btn_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAutelPlayer.pauseVideo();
                }
            });

            Button btn_resume = new Button(CodecActivity.this);
            btn_resume.setText("Resume");
            btn_resume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAutelPlayer.resuemeVideo();
                }
            });


            btn_layout.addView(btn_pause);
            btn_layout.addView(btn_resume);

            content_layout.addView(btn_layout);
        });

        /**
         * The H264 video stream data for developer to deal with
         */
        findViewById(R.id.testAutelCodec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCodecing = true;
                stopPlayer();
                final TextView logTV = new TextView(CodecActivity.this);
                AutelPlayerManager.getInstance().init(CodecActivity.this, false);
                codecView = createAutelCodecView();
                content_layout.setOnClickListener(null);
                content_layout.setVisibility(View.VISIBLE);
                content_layout.addView(codecView);
                mAutelPlayer = new AutelPlayer(0);
                mAutelPlayer.addVideoView(codecView);
                AutelPlayerManager.getInstance().addAutelPlayer(mAutelPlayer);
                AutelPlayerManager.getInstance().addCodecListeners(TAG, 0, new OnRenderFrameInfoListener() {
                    @Override
                    public void onRenderFrameTimestamp(long l) {

                    }

                    @Override
                    public void onRenderFrameSizeChanged(int i, int i1) {

                    }

                    @Override
                    public void onFrameStream(byte[] bytes, boolean b, int i, long l, int i1) {

                    }

                    @Override
                    public void onFrameStream(ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo, boolean b, int i, int i1, int i2) {

                    }
                });
                mAutelPlayer.startPlayer();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (isCodecing) {
            isCodecing = false;

            content_layout.removeAllViews();
            content_layout.setVisibility(View.GONE);
            stopPlayer();
            return;
        }

        super.onBackPressed();
    }

    public void onDestroy() {
        super.onDestroy();
        stopPlayer();
    }

    private void stopPlayer(){
        AutelLog.debug_i("initCodec","stopPlayer  codecBase ");
        if(null != mAutelPlayer){
            mAutelPlayer.removeVideoView();
            AutelPlayerManager.getInstance().removeAutelPlayer(mAutelPlayer);
            mAutelPlayer.stopPlayer();
            mAutelPlayer.releasePlayer();

        }
    }
}
