package com.autel.sdksample.dragonfish.listener;

import com.autel.lib.jniHelper.PathPlanningResult;

public interface PathPlanningListener {
    /**
     * 回调结果
     * @param result
     */
    void onPlanningResult(PathPlanningResult result);
}
