package com.autel.sdksample.base.adapter;

import android.content.Context;

import com.autel.common.flycontroller.LandType;
import com.autel.common.flycontroller.visual.VisualSettingSwitchblade;

import java.util.Arrays;

public class LandingTypeAdapter extends SelectorAdapter<LandType> {

    public LandingTypeAdapter(Context context) {
        super(context);
        elementList.addAll(Arrays.asList(LandType.values()));
    }
}
