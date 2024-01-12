package com.autel.sdksample.dragonfish.mission.enums;

/**
 * Created by ZDG
 * Date: 2021/1/9
 */
public enum AltitudeCorrectionType {

    NONE(0),
    DEM(1),
    MANUAL(2),
    UNKNOWN(-1);

    private final int value;

    AltitudeCorrectionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static AltitudeCorrectionType find(int value) {
        switch (value) {
            case 0:
                return NONE;
            case 1:
                return DEM;
            case 2:
                return MANUAL;
            default:
                return UNKNOWN;
        }
    }
}
