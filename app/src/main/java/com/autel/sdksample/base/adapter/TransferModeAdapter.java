package com.autel.sdksample.base.adapter;

import android.content.Context;

import com.autel.common.dsp.evo.TransferMode;

public class TransferModeAdapter extends SelectorAdapter<TransferMode> {

    public TransferModeAdapter(Context context) {
        super(context);
        elementList.add(TransferMode.FLUENCY);
        elementList.add(TransferMode.HIGH_DEFINITION);
        elementList.add(TransferMode.NORMAL);
        elementList.add(TransferMode.UNKNOWN);
    }
}
