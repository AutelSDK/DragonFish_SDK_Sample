package com.autel.sdksample.base.adapter;

import android.content.Context;

import com.autel.common.gimbal.GimbalAxisType;

public class GimbalAxisAdapter extends SelectorAdapter<GimbalAxisType> {

    public GimbalAxisAdapter(Context context) {
        super(context);
        elementList.add(GimbalAxisType.ROLL);
        elementList.add(GimbalAxisType.PITCH);
        elementList.add(GimbalAxisType.YAW);
    }
}
