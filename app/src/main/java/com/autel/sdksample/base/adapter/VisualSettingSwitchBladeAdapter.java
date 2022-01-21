package com.autel.sdksample.base.adapter;

import android.content.Context;

import com.autel.common.flycontroller.visual.VisualSettingSwitchblade;

import java.util.Arrays;

public class VisualSettingSwitchBladeAdapter extends SelectorAdapter<VisualSettingSwitchblade> {

    public VisualSettingSwitchBladeAdapter(Context context) {
        super(context);
        elementList.addAll(Arrays.asList(VisualSettingSwitchblade.values()));
    }
}
