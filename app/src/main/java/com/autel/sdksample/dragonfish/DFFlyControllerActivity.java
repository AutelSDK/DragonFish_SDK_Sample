package com.autel.sdksample.dragonfish;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.autel.common.CallbackWithNoParam;
import com.autel.common.CallbackWithOneParam;
import com.autel.common.camera.visual.VisualWarningInfo;
import com.autel.common.error.AutelError;
import com.autel.common.flycontroller.LandType;
import com.autel.common.flycontroller.cruiser.CruiserFlyControllerInfo;
import com.autel.common.flycontroller.cruiser.LandingParams;
import com.autel.common.flycontroller.cruiser.StationType;
import com.autel.common.flycontroller.visual.AvoidanceRadarInfo;
import com.autel.common.flycontroller.visual.VisualSettingInfo;
import com.autel.common.flycontroller.visual.VisualSettingSwitchblade;
import com.autel.sdk.flycontroller.AutelFlyController;
import com.autel.sdk.flycontroller.CruiserFlyController;
import com.autel.sdk.product.BaseProduct;
import com.autel.sdk.product.CruiserAircraft;
import com.autel.sdksample.R;
import com.autel.sdksample.base.FlyControllerActivity;
import com.autel.sdksample.base.adapter.BaseStationTypeAdapter;
import com.autel.sdksample.base.adapter.LandingTypeAdapter;
import com.autel.sdksample.base.adapter.VisualSettingSwitchBladeAdapter;
import com.autel.sdksample.dragonfish.mission.enums.MissionAltitudeType;

/**
 * Created by A16343 on 2017/9/6.
 */

public class DFFlyControllerActivity extends FlyControllerActivity {
    private CruiserFlyController mEvoFlyController;
    /*  LandingGearStateAdapter mLandingGearStateAdapter;
      LandingGearState selectedLandingGearState = LandingGearState.UNKNOWN;*/
    VisualSettingSwitchblade mVisualSettingSwitchblade = VisualSettingSwitchblade.UNKNOWN;
    LandType landType = LandType.PRECISE_LANDING_PARAM;//精准降落
    StationType stationType = StationType.FIXD_SELF;
    private Switch visualSettingEnableState;
    private EditText bslatEditText, bslongEditText, bsaltEditText, latEditText, longEditText, altEditText,
            latlandEditText, longlandEditText, altlandEditText,
            raduisEditText;

    @Override
    protected AutelFlyController initController(BaseProduct product) {
        mEvoFlyController = ((CruiserAircraft) product).getFlyController();
        return mEvoFlyController;
    }

    @Override
    protected int getCustomViewResId() {
        return R.layout.activity_g2_fc;
    }

    @Override
    protected void initUi() {
        super.initUi();
      /*  mLandingGearStateAdapter = new LandingGearStateAdapter(this);
        ((Spinner) findViewById(R.id.landingGearStateList)).setAdapter(mLandingGearStateAdapter);
        ((Spinner) findViewById(R.id.landingGearStateList)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLandingGearState = (LandingGearState) parent.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedLandingGearState = LandingGearState.UNKNOWN;
            }
        });*/

        visualSettingEnableState = (Switch) findViewById(R.id.visualSettingEnableState);

        ((Spinner) findViewById(R.id.visualSettingList)).setAdapter(new VisualSettingSwitchBladeAdapter(this));
        ((Spinner) findViewById(R.id.visualSettingList)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mVisualSettingSwitchblade = (VisualSettingSwitchblade) parent.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // selectedLandingGearState = LandingGearState.UNKNOWN;
            }
        });
        ((Spinner) findViewById(R.id.landList)).setAdapter(new LandingTypeAdapter(this));
        ((Spinner) findViewById(R.id.landList)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                landType = (LandType) parent.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // selectedLandingGearState = LandingGearState.UNKNOWN;
            }
        });
        ((Spinner) findViewById(R.id.baseTypeList)).setAdapter(new BaseStationTypeAdapter(this));
        ((Spinner) findViewById(R.id.baseTypeList)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stationType = (StationType) parent.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    public void setFlyControllerListener(View view) {
        mEvoFlyController.setFlyControllerInfoListener(new CallbackWithOneParam<CruiserFlyControllerInfo>() {
            @Override
            public void onSuccess(CruiserFlyControllerInfo data) {
                logOut("setFlyControllerListener data " + data);
            }

            @Override
            public void onFailure(AutelError error) {
                logOut("setFlyControllerListener " + error.getDescription());
            }
        });

//        mEvoFlyController.setVisualViewPointCoordListener(new CallbackWithOneParam<ViewPointTargetArea>() {
//            @Override
//            public void onSuccess(ViewPointTargetArea data) {
//                logOut("setVisualViewPointCoordListener data " + data);
//            }
//
//            @Override
//            public void onFailure(AutelError error) {
//                logOut("setVisualViewPointCoordListener onFailure " );
//            }
//        });
    }

    public void resetFlyControllerListener(View view) {
        mEvoFlyController.setFlyControllerInfoListener(null);
    }

    public void setBaseStationLocation(View view) {
        bslatEditText = findViewById(R.id.bslat);
        bslongEditText = findViewById(R.id.bslng);
        bsaltEditText = findViewById(R.id.bsalt);
        String lat = bslatEditText.getText().toString();
        String lng = bslongEditText.getText().toString();
        String alt = bsaltEditText.getText().toString();
        if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lng) || TextUtils.isEmpty(alt)) return;
        mEvoFlyController.setBaseStationLocation(Double.parseDouble(lat), Double.parseDouble(lng), Double.parseDouble(alt), new CallbackWithOneParam<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                logOut("setBaseStationLocation onSuccess " + data);
            }

            @Override
            public void onFailure(AutelError error) {
                logOut("setLandingGearState onFailure " + error.getDescription());
            }
        });
    }

    public void setBaseStationType(View view) {
        mEvoFlyController.setBaseStationType(stationType, new CallbackWithOneParam<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                logOut("setBaseStationLocation onSuccess " + data);
            }

            @Override
            public void onFailure(AutelError error) {
                logOut("setLandingGearState onFailure " + error.getDescription());
            }
        });
    }

    public void setLandingLocation(View view) {
        latEditText = findViewById(R.id.lat);//下降盘旋点纬度
        longEditText = findViewById(R.id.lng);//下降盘旋点经度
        altEditText = findViewById(R.id.alt);//下降盘旋点高度
        latlandEditText = findViewById(R.id.latlanding);//迫降点纬度
        longlandEditText = findViewById(R.id.lnglanding);//迫降点经度
        altlandEditText = findViewById(R.id.altlanding);//迫降点高度
        raduisEditText = findViewById(R.id.radius);//下降盘旋点半径
        String lat = latEditText.getText().toString();
        String lng = longEditText.getText().toString();
        String alt = altEditText.getText().toString();
        String latland = latlandEditText.getText().toString();
        String lngland = longlandEditText.getText().toString();
        String altland = altlandEditText.getText().toString();
        String radius = raduisEditText.getText().toString();
        if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lng) || TextUtils.isEmpty(alt)
                || TextUtils.isEmpty(latland) || TextUtils.isEmpty(lngland)
                || TextUtils.isEmpty(altland)
                || TextUtils.isEmpty(radius)) return;
        LandingParams params = new LandingParams();
        params.setLDLatitude((int) (Float.parseFloat(latland) * 1E7));//进场点纬度
        params.setLDLongitude((int) (Float.parseFloat(lngland) * 1E7));//进场点经度
        params.setLDAltitude((int) (Integer.parseInt(altland) * 1E3)); //进场点高度 单位毫米 需要乘以1E3
        params.setLDRadius(Integer.parseInt(radius) * 10);
        params.setLatitude((int) (Float.parseFloat(lat) * 1E7));
        params.setLongitude((int) (Float.parseFloat(lng) * 1E7));
        params.setAltitude((int) (Integer.parseInt(alt) * 1E3));
        params.setLongitude((int) (Float.parseFloat(lngland) * 1E7));
        params.setLDSwitchAlt(100 * 10);//这里设置100m 需要根据实际输入值设置 切换高度 说明此高度是相对高度值 单位分米
        params.setLDAltType(MissionAltitudeType.RELATIVE.getValue());//切换高度类型
        params.setLDEnterAltType(MissionAltitudeType.RELATIVE.getValue());//进场点高度类型
        if (null == mEvoFlyController) return;
        mEvoFlyController.land(landType, params, new CallbackWithNoParam() {
            @Override
            public void onSuccess() {
                logOut("setBaseStationLocation onSuccess ");
            }

            @Override
            public void onFailure(AutelError error) {
                logOut("setLandingGearState onFailure " + error.getDescription());
            }
        });
    }

    public void droneArmed(View view) {
        mEvoFlyController.droneArmed(new CallbackWithNoParam() {
            @Override
            public void onSuccess() {
                //logOut("droneArmed onSuccess " + selectedLandingGearState);
            }

            @Override
            public void onFailure(AutelError error) {
                logOut("droneArmed onFailure " + error.getDescription());
            }
        });
    }

    public void droneDisarmed(View view) {
        mEvoFlyController.droneDisarmed(new CallbackWithNoParam() {
            @Override
            public void onSuccess() {
                // logOut("droneDisarmed onSuccess " + selectedLandingGearState);
            }

            @Override
            public void onFailure(AutelError error) {
                logOut("droneDisarmed onFailure " + error.getDescription());
            }
        });
    }

    public void setVisualWarnListener(View view) {
        mEvoFlyController.setVisualWarnListener(new CallbackWithOneParam<VisualWarningInfo>() {
            @Override
            public void onSuccess(VisualWarningInfo warning) {
                logOut("setVisualWarnListener onSuccess " + warning);
            }

            @Override
            public void onFailure(AutelError error) {
                logOut("setVisualWarnListener onFailure " + error.getDescription());
            }
        });
    }

    public void resetVisualWarnListener(View view) {
        mEvoFlyController.setVisualWarnListener(null);
    }

    public void setRadarInfoListener(View view) {
        mEvoFlyController.setAvoidanceRadarInfoListener(new CallbackWithOneParam<AvoidanceRadarInfo>() {
            @Override
            public void onSuccess(AvoidanceRadarInfo radarInfo) {
                logOut("setRadarInfoListener onSuccess " + radarInfo);
            }

            @Override
            public void onFailure(AutelError error) {
                logOut("setRadarInfoListener onFailure " + error.getDescription());
            }
        });
    }

    public void setVisualSettingEnable(View view) {
        if (mVisualSettingSwitchblade == VisualSettingSwitchblade.SET_VIEW_POINT_COORD) {

        } else {
            mEvoFlyController.setVisualSettingEnable(mVisualSettingSwitchblade, visualSettingEnableState.isEnabled(), new CallbackWithNoParam() {
                @Override
                public void onSuccess() {
                    logOut("setVisualSettingEnable onSuccess ");
                }

                @Override
                public void onFailure(AutelError error) {
                    logOut("setVisualSettingEnable onFailure " + error.getDescription());
                }
            });
        }
    }

    public void getVisualSettingEnable(View view) {
        mEvoFlyController.getVisualSettingInfo(new CallbackWithOneParam<VisualSettingInfo>() {
            @Override
            public void onSuccess(VisualSettingInfo data) {
                logOut("getVisualSettingEnable onSuccess " + data.toString());
            }

            @Override
            public void onFailure(AutelError error) {
                logOut("getVisualSettingEnable onFailure " + error.getDescription());
            }
        });
    }

    public void setVisualViewPointSpeed(View view) {
        String value = ((EditText) findViewById(R.id.VisualViewPointSpeed)).getText().toString();
        mEvoFlyController.setVisualViewPointSpeed(Float.valueOf(value), new CallbackWithNoParam() {
            @Override
            public void onSuccess() {
                logOut("setVisualViewPointSpeed onSuccess ");
            }

            @Override
            public void onFailure(AutelError error) {
                logOut("setVisualViewPointSpeed onFailure " + error.getDescription());
            }
        });
    }

    public void resetRadarInfoListener(View view) {
        mEvoFlyController.setAvoidanceRadarInfoListener(null);
    }
}
