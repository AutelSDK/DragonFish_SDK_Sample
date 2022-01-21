package com.autel.sdksample.base.mission.adapter;

import android.content.Context;

import com.autel.common.mission.cruiser.CruiserWaypointFinishedAction;
import com.autel.common.mission.evo.EvoWaypointFinishedAction;
import com.autel.sdksample.base.adapter.SelectorAdapter;

public class EvoWaypointFinishActionAdapter extends SelectorAdapter<CruiserWaypointFinishedAction> {

    public EvoWaypointFinishActionAdapter(Context context) {
        super(context);
        elementList.add(CruiserWaypointFinishedAction.KEEP_ON_LAST_POINT);
        elementList.add(CruiserWaypointFinishedAction.LAND_ON_LAST_POINT);
        elementList.add(CruiserWaypointFinishedAction.RETURN_HOME);
        elementList.add(CruiserWaypointFinishedAction.RETURN_TO_FIRST_POINT);
        elementList.add(CruiserWaypointFinishedAction.STOP_ON_LAST_POINT);
    }
}
