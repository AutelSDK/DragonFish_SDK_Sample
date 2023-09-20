package com.autel.sdksample.dragonfish;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.autel.common.CallbackWithNoParam;
import com.autel.common.CallbackWithOneParam;
import com.autel.common.error.AutelError;
import com.autel.common.gimbal.GimbalAxisType;
import com.autel.common.gimbal.evo.EvoAngleInfo;
import com.autel.common.gimbal.evo.GimbalAngleData;
import com.autel.common.gimbal.evo.GimbalAngleRange;
import com.autel.common.gimbal.evo.GimbalAngleSpeed;
import com.autel.sdk.gimbal.AutelGimbal;
import com.autel.sdk.gimbal.CruiserGimbal;
import com.autel.sdk.product.BaseProduct;
import com.autel.sdk.product.CruiserAircraft;
import com.autel.sdksample.R;
import com.autel.sdksample.base.adapter.GimbalAxisAdapter;
import com.autel.sdksample.base.gimbal.GimbalActivity;

/**
 * Created by A16343 on 2017/9/6.
 */

public class DFGimbalActivity extends GimbalActivity {
    private CruiserGimbal cruiserGimbal;
    private EditText gimbalAnglePitch;
    private EditText gimbalAngleRoll;
    private EditText gimbalAngleYaw;
    private EditText gimbalAnglePitchSpeed;
    private EditText gimbalAngleRollSpeed;
    private EditText gimbalAngleYawSpeed;
    private Spinner gimbalAxisTypeList;
    private GimbalAxisType mGimbalAxis = GimbalAxisType.ROLL;

    @Override
    protected AutelGimbal initController(BaseProduct product) {
        cruiserGimbal = ((CruiserAircraft) product).getGimbal();
        return cruiserGimbal;
    }

    @Override
    protected int getCustomViewResId() {
//        return R.layout.ac_base_gimbal;
        return R.layout.activity_df_gimbal;
    }

    @Override
    protected void initUi() {
        super.initUi();
        gimbalAnglePitch = (EditText) findViewById(R.id.gimbalAnglePitch);
        gimbalAngleRoll = (EditText) findViewById(R.id.gimbalAngleRoll);
        gimbalAngleYaw = (EditText) findViewById(R.id.gimbalAngleYaw);
        gimbalAnglePitchSpeed = (EditText) findViewById(R.id.gimbalAnglePitchSpeed);
        gimbalAngleRollSpeed = (EditText) findViewById(R.id.gimbalAngleRollSpeed);
        gimbalAngleYawSpeed = (EditText) findViewById(R.id.gimbalAngleYawSpeed);
        findViewById(R.id.setGimbalAngleListener).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cruiserGimbal.setAngleListener(new CallbackWithOneParam<EvoAngleInfo>() {

                    @Override
                    public void onSuccess(EvoAngleInfo data) {
                        logOut("setAngleListener onSuccess " + data);
                    }

                    @Override
                    public void onFailure(AutelError autelError) {
                        logOut("setAngleListener error " + autelError.getDescription());
                    }
                });
            }
        });
        findViewById(R.id.resetGimbalAngleListener).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cruiserGimbal.setAngleListener(null);
            }
        });
        findViewById(R.id.getAngleRange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cruiserGimbal.getParameterRangeManager().getAngleRange(new CallbackWithOneParam<GimbalAngleRange>() {
                    @Override
                    public void onSuccess(GimbalAngleRange data) {
                        logOut("getAngleRange onSuccess " + data);
                    }

                    @Override
                    public void onFailure(AutelError autelError) {
                        logOut("getAngleRange onSuccess " + autelError.getDescription());
                    }
                });
            }
        });
        findViewById(R.id.setGimbalAngle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pitchValue = gimbalAnglePitch.getText().toString();
                int pitch = isEmpty(pitchValue) ? 0 : Integer.valueOf(pitchValue);

                String rollValue = gimbalAngleRoll.getText().toString();
                int roll = isEmpty(rollValue) ? 0 : Integer.valueOf(rollValue);

                String yawValue = gimbalAngleYaw.getText().toString();
                int yaw = isEmpty(yawValue) ? 0 : Integer.valueOf(yawValue);

                GimbalAngleData angleData = new GimbalAngleData();
                angleData.setPitch(pitch);
                angleData.setRoll(roll);
                angleData.setYaw(yaw);
                cruiserGimbal.setGimbalAngle(pitch,yaw,roll);
            }
        });
        findViewById(R.id.setGimbalAngleSpeed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pitchValue = gimbalAnglePitchSpeed.getText().toString();
                int pitch = isEmpty(pitchValue) ? 0 : Integer.valueOf(pitchValue);

                String rollValue = gimbalAngleRollSpeed.getText().toString();
                int roll = isEmpty(rollValue) ? 0 : Integer.valueOf(rollValue);

                String yawValue = gimbalAngleYawSpeed.getText().toString();
                int yaw = isEmpty(yawValue) ? 0 : Integer.valueOf(yawValue);

                GimbalAngleSpeed angleSpeed = new GimbalAngleSpeed();
                angleSpeed.setPitchSpeed(pitch);
                angleSpeed.setRollSpeed(roll);
                angleSpeed.setYawSpeed(yaw);
                cruiserGimbal.setGimbalAngleWithSpeed(pitch,yaw);
            }
        });
        gimbalAxisTypeList = (Spinner) findViewById(R.id.gimbalAxisTypeList);
        gimbalAxisTypeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mGimbalAxis = (GimbalAxisType) parent.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        GimbalAxisAdapter axisAdapter = new GimbalAxisAdapter(this);
        gimbalAxisTypeList.setAdapter(axisAdapter);
        findViewById(R.id.resetGimbalAngle).setOnClickListener(v -> cruiserGimbal.resetGimbalAngle(mGimbalAxis, new CallbackWithNoParam() {
            @Override
            public void onSuccess() {
                logOut("resetGimbalAngle onSuccess ");
            }

            @Override
            public void onFailure(AutelError error) {
                logOut("resetGimbalAngle onFailure " + error.getDescription());
            }
        }));

        findViewById(R.id.setRollAdjustData).setOnClickListener(v -> {
            String value = ((EditText) findViewById(R.id.rollAdjustDataValue)).getText().toString();
            if ("".equals(value)) {
                return;
            }

            cruiserGimbal.setRollAdjustData(Float.valueOf(value));
        });

        findViewById(R.id.setPitchAdjustData).setOnClickListener(v -> {
            String value = ((EditText) findViewById(R.id.pitchAdjustDataValue)).getText().toString();
            if ("".equals(value)) {
                return;
            }

            cruiserGimbal.setPitchAdjustData(Float.valueOf(value));
        });

        findViewById(R.id.setYawAdjustData).setOnClickListener(v -> {
            String value = ((EditText) findViewById(R.id.yawAdjustDataValue)).getText().toString();
            if ("".equals(value)) {
                return;
            }

            cruiserGimbal.setYawAdjustData(Float.valueOf(value));
        });

        findViewById(R.id.getRollAdjustData).setOnClickListener(v -> cruiserGimbal.getRollAdjustData(new CallbackWithOneParam<Double>() {
            @Override
            public void onSuccess(Double data) {
                logOut("getRollAdjustData onSuccess "+data);
            }

            @Override
            public void onFailure(AutelError error) {
                logOut("getRollAdjustData onFailure " + error.getDescription());
            }
        }));
    }
}
