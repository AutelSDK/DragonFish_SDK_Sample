package com.autel.sdksample.dragonfish;

import com.autel.sdk.product.BaseProduct;
import com.autel.sdk.remotecontroller.AutelRemoteController;
import com.autel.sdksample.R;
import com.autel.sdksample.base.RemoteControllerActivity;

public class DFRCActivity extends RemoteControllerActivity {
    AutelRemoteController mAutelRemoteController;
    @Override
    protected AutelRemoteController initController(BaseProduct product) {
        mAutelRemoteController = product.getRemoteController();
        return mAutelRemoteController;
    }

    @Override
    protected int getCustomViewResId() {
        return R.layout.activity_rc;
    }


}
