package com.autel.sdksample.base.adapter;

import android.content.Context;

import com.autel.common.flycontroller.LandType;
import com.autel.common.flycontroller.cruiser.StationType;

import java.util.Arrays;

public class BaseStationTypeAdapter extends SelectorAdapter<StationType> {

    public BaseStationTypeAdapter(Context context) {
        super(context);
        elementList.addAll(Arrays.asList(StationType.values()));
    }
}
