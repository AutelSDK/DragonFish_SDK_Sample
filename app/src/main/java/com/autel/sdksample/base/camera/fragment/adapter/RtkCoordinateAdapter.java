package com.autel.sdksample.base.camera.fragment.adapter;

import android.content.Context;

import com.autel.sdksample.base.adapter.SelectorAdapter;

import java.util.List;

public class RtkCoordinateAdapter extends SelectorAdapter<String> {

    public RtkCoordinateAdapter(Context context,List<String> mode) {
        super(context);
        elementList = mode;

    }

    public RtkCoordinateAdapter(Context context) {
        super(context);
    }
}
