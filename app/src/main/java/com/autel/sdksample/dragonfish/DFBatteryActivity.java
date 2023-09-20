package com.autel.sdksample.dragonfish;

import android.os.Bundle;
import android.view.View;

import com.autel.common.CallbackWithNoParam;
import com.autel.common.CallbackWithOneParam;
import com.autel.common.battery.cruiser.CruiserBatteryInfo;
import com.autel.common.battery.evo.EvoBatteryInfo;
import com.autel.common.error.AutelError;
import com.autel.sdk.battery.AutelBattery;
import com.autel.sdk.battery.CruiserBattery;
import com.autel.sdk.product.BaseProduct;
import com.autel.sdk.product.CruiserAircraft;
import com.autel.sdksample.R;
import com.autel.sdksample.base.BatteryActivity;


public class DFBatteryActivity extends BatteryActivity {
    private CruiserBattery mXStarEvoBattery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Battery");
    }

    @Override
    protected AutelBattery initController(BaseProduct product) {
        mXStarEvoBattery = ((CruiserAircraft) product).getBattery();
        return mXStarEvoBattery;
    }

    @Override
    protected int getCustomViewResId() {
        return R.layout.activity_g2_battery;
    }

    @Override
    protected void initUi() {
        super.initUi();
        findViewById(R.id.setBatteryRealTimeDataListener).setOnClickListener(v -> mXStarEvoBattery.setBatteryStateListener(new CallbackWithOneParam<CruiserBatteryInfo>() {
            @Override
            public void onFailure(AutelError error) {
                logOut("setBatteryStateListener  error :  " + error.getDescription());
            }

            @Override
            public void onSuccess(CruiserBatteryInfo data) {
                logOut("setBatteryStateListener  data current battery :  " + data.toString());
            }
        }));
        findViewById(R.id.resetBatteryRealTimeDataListener).setOnClickListener(v -> mXStarEvoBattery.setBatteryStateListener(null));

        findViewById(R.id.getDischargeDay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CruiserBattery) mController).getDischargeDay(BATTERY_NUM1,new CallbackWithOneParam<Integer>() {
                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getDischargeDay  error :  " + error.getDescription());
                    }

                    @Override
                    public void onSuccess(Integer data) {
                        logOut("getDischargeDay  data :  " + data);
                    }
                });
            }
        });
        findViewById(R.id.setDischargeDay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = dischargeDay.getText().toString();
                mXStarEvoBattery.setDischargeDay(BATTERY_NUM1,isEmpty(value) ? 2 : Integer.valueOf(value), new CallbackWithNoParam() {
                    @Override
                    public void onSuccess() {

                        logOut("setDischargeDay  onSuccess  ");
                    }

                    @Override
                    public void onFailure(AutelError autelError) {
                        logOut("setDischargeDay  error :  " + autelError.getDescription());
                    }
                });
            }
        });
        findViewById(R.id.getDischargeCount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mXStarEvoBattery.getDischargeCount(BATTERY_NUM1,new CallbackWithOneParam<Integer>() {
                    @Override
                    public void onSuccess(Integer data) {
                        logOut("getDischargeCount  " + data);
                    }

                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getDischargeCount error : " + error.getDescription());
                    }
                });
            }
        });
        findViewById(R.id.getVersion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mXStarEvoBattery.getVersion(BATTERY_NUM1,new CallbackWithOneParam<String>() {
                    @Override
                    public void onSuccess(String data) {
                        logOut("getVersion  " + data);
                    }

                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getVersion  error : " + error.getDescription());
                    }
                });
            }
        });
        findViewById(R.id.getSerialNumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mXStarEvoBattery.getSerialNumber(BATTERY_NUM1,new CallbackWithOneParam<String>() {
                    @Override
                    public void onSuccess(String data) {
                        logOut("getSerialNumber  " + data);
                    }

                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getSerialNumber  error : " + error.getDescription());
                    }
                });
            }
        });
    }

}
