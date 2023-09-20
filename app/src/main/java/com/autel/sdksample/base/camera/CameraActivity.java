package com.autel.sdksample.base.camera;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.autel.common.CallbackWithTwoParams;
import com.autel.common.camera.CameraProduct;
import com.autel.common.error.AutelError;
import com.autel.common.video.OnRenderFrameInfoListener;
import com.autel.manager.player.AutelPlayerManager;
import com.autel.manager.player.autelplayer.AutelPlayer;
import com.autel.manager.player.autelplayer.AutelPlayerView;
import com.autel.sdk.camera.AutelBaseCamera;
import com.autel.sdk.camera.AutelCameraManager;
import com.autel.sdk.product.BaseProduct;
import com.autel.sdk.widget.AutelCodecView;
import com.autel.sdksample.R;
import com.autel.sdksample.TestApplication;
import com.autel.sdksample.base.camera.fragment.CameraDFFragment;
import com.autel.sdksample.base.camera.fragment.CameraNotConnectFragment;
import com.autel.util.log.AutelLog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class CameraActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    FrameLayout mContentLayout;
    TextView cameraType;
    TextView cameraLogOutput;
    AutelBaseCamera currentCamera;
    AutelCameraManager autelCameraManager;
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String text = (String) msg.obj;
            if (null != cameraLogOutput) {
                cameraLogOutput.setText(text);
            }
        }
    };
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

    private void initVideo() {
        AutelLog.debug_i("initCodec","initVideo  codecBase ");
        AutelPlayerManager.getInstance().init(this, false);
        codecView = createAutelCodecView();
        mContentLayout.addView(codecView);
        mAutelPlayer = new AutelPlayer(0);
        mAutelPlayer.addVideoView(codecView);
        AutelPlayerManager.getInstance().addAutelPlayer(mAutelPlayer);
        mAutelPlayer.startPlayer();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Camera");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_camera);
        cameraType = (TextView) findViewById(R.id.camera_type);
        cameraLogOutput = (TextView) findViewById(R.id.camera_log_output);
        BaseProduct product = ((TestApplication) getApplicationContext()).getCurrentProduct();
        if (null != product) {
            autelCameraManager = product.getCameraManager();
        }
        changePage(CameraNotConnectFragment.class);
//        initListener();
        mContentLayout = (FrameLayout) findViewById(R.id.contentCodec);

        findViewById(R.id.camera_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSize();
            }
        });

        initVideo();
    }

    boolean fullScreen = true;
    private void changeSize() {
        RelativeLayout.LayoutParams params = fullScreen ? new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) :
                new RelativeLayout.LayoutParams(300, 150);
        codecView.setLayoutParams(params);
        fullScreen = !fullScreen;

//        Log.v("videoDecodeBug","playstatus "+ CodecManager.getInstance().getPlayStatus());
    }

    private void initListener() {
        if (null == autelCameraManager) {
            return;
        }
        autelCameraManager.setCameraChangeListener(new CallbackWithTwoParams<CameraProduct, AutelBaseCamera>() {
            @Override
            public void onSuccess(final CameraProduct data1, final AutelBaseCamera data2) {
                Log.v(TAG, "initListener onSuccess connect " + data1);
                if (currentCamera == data2) {
                    return;
                }
                currentCamera = data2;
                cameraType.setText(data1.toString());
                switch (data1) {
//                    case R12:
//                        changePage(CameraR12Fragment.class);
//                        break;
//                    case XB015:
//                        changePage(CameraXB015Fragment.class);
//                        break;
//                    case XT701:
//                        changePage(CameraXT701Fragment.class);
//                        break;
//                    case XT705:
//                        changePage(CameraXT705Fragment.class);
//                        break;
//                    case XT706:
//                        changePage(CameraXT706Fragment.class);
//                        break;
                    case XT708:
                    case XT710:
                    case XT711:
                    case XT713:
                    case XT714:
                    case XT715:
                    case XT717:
                        changePage(CameraDFFragment.class);
                        break;

                    default:
                        changePage(CameraNotConnectFragment.class);
                }

            }

            @Override
            public void onFailure(AutelError error) {
                Log.v(TAG, "initListener onFailure error " + error.getDescription());
                cameraType.setText("currentCamera connect broken  " + error.getDescription());
            }
        });
    }

    boolean state;

    public void onResume() {
        super.onResume();
        initListener();
        state = false;
    }

    public void onPause() {
        super.onPause();
        if (null == autelCameraManager) {
            return;
        }
        autelCameraManager.setCameraChangeListener(null);
    }

    private void changePage(Class page) {
        try {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_layout, (Fragment) page.newInstance()).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AutelBaseCamera getCurrentCamera() {
        return currentCamera;
    }

    public void onDestroy() {
        super.onDestroy();
        stopPlayer();
    }

    public void logOut(String log) {
        Log.v(TAG, log);
        Message msg = handler.obtainMessage();
        msg.obj = log;
        handler.sendMessage(msg);
    }

}
