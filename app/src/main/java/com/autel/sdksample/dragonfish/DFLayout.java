package com.autel.sdksample.dragonfish;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.autel.sdksample.R;
import com.autel.sdksample.base.CodecActivity;
import com.autel.sdksample.base.album.AlbumActivity;
import com.autel.sdksample.base.camera.CameraActivity;
import com.autel.sdksample.base.mission.MissionActivity;
import com.autel.sdksample.dragonfish.mission.DFWayPointActivity;

/**
 * Created by A16343 on 2017/9/5.
 */

public class DFLayout {
    private View mLayout;
    private Context mContext;

    public DFLayout(Context context) {
        mContext = context;
        mLayout = View.inflate(mContext, R.layout.activity_evo2, null);
        initUI();
    }

    public View getLayout() {
        return mLayout;
    }

    private void initUI() {
        mLayout.findViewById(R.id.rcTest).setOnClickListener(v -> mContext.startActivity(new Intent(mContext, DFRCActivity.class)));
        mLayout.findViewById(R.id.fcTest).setOnClickListener(v -> mContext.startActivity(new Intent(mContext, DFFlyControllerActivity.class)));
        mLayout.findViewById(R.id.cameraTest).setOnClickListener(v -> mContext.startActivity(new Intent(mContext, CameraActivity.class)));
        mLayout.findViewById(R.id.codecTest).setOnClickListener(v -> mContext.startActivity(new Intent(mContext, CodecActivity.class)));
        mLayout.findViewById(R.id.DSPTest).setOnClickListener(v -> mContext.startActivity(new Intent(mContext, DFDspActivity.class)));
        mLayout.findViewById(R.id.missionTest).setOnClickListener(v -> mContext.startActivity(new Intent(mContext, MissionActivity.class)));

        mLayout.findViewById(R.id.evo2MissionTest).setOnClickListener(v -> mContext.startActivity(new Intent(mContext, DFWayPointActivity.class)));

        mLayout.findViewById(R.id.BatteryTest).setOnClickListener(v -> mContext.startActivity(new Intent(mContext, DFBatteryActivity.class)));
        mLayout.findViewById(R.id.GimbalTest).setOnClickListener(v -> mContext.startActivity(new Intent(mContext, DFGimbalActivity.class)));
        mLayout.findViewById(R.id.AlbumTest).setOnClickListener(v -> mContext.startActivity(new Intent(mContext, AlbumActivity.class)));
        mLayout.findViewById(R.id.rtktest).setOnClickListener(v -> {
//                mContext.startActivity(new Intent(mContext, Evo2RTKActivity.class));
        });
        mLayout.findViewById(R.id.tvSerial).setOnClickListener(v -> mContext.startActivity(new Intent(mContext,SerialAidlActivity.class)));
        mLayout.findViewById(R.id.tvPush).setOnClickListener(v -> mContext.startActivity(new Intent(mContext, LivePushActivity.class)));
    }
}
