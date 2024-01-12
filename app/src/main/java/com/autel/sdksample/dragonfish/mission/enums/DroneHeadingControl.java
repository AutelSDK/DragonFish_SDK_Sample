package com.autel.sdksample.dragonfish.mission.enums;

/**
 * Created by ZDG
 * Date: 2019/5/7
 */
public enum DroneHeadingControl {
    FOLLOW_WAYLINE_DIRECTION(0),    //沿航线方向
    MANUAL_CONTROL(1),  //手动控制
    CUSTOM(2),
    UNKNOWN(-1);

    private final int value;

    DroneHeadingControl(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DroneHeadingControl find(int value) {
        switch (value) {
            case 0:
                return FOLLOW_WAYLINE_DIRECTION;
            case 1:
                return MANUAL_CONTROL;
            case 2:
                return CUSTOM;
            default:
                return UNKNOWN;
        }
    }
}
