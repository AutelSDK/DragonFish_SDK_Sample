package com.autel.sdksample.dragonfish;

import android.os.Bundle;
import android.view.View;

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
        findViewById(R.id.setBatteryRealTimeDataListener).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mXStarEvoBattery.setBatteryStateListener(new CallbackWithOneParam<CruiserBatteryInfo>() {
                    @Override
                    public void onFailure(AutelError error) {
                        logOut("setBatteryStateListener  error :  " + error.getDescription());
                    }

                    @Override
                    public void onSuccess(CruiserBatteryInfo data) {
                        logOut("setBatteryStateListener  data current battery :  " + data.toString());
                    }
                });
            }
        });

    }
}
