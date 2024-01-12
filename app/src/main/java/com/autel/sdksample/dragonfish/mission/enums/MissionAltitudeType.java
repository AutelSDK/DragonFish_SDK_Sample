package com.autel.sdksample.dragonfish.mission.enums;

public enum MissionAltitudeType {

    UNKNOWN(-1, 0), //未知
    ALTITUDE(1, 1), //海拔高度
    RELATIVE(2, 0); //相对高度

    private final int value;//传输给协议的值
    private final int position;//传输给界面的值，代表在界面的位置

    MissionAltitudeType(int value, int position) {
        this.value = value;
        this.position = position;
    }

    public int getValue() {
        return value;
    }

    public int getPosition() {
        return position;
    }

    public static MissionAltitudeType find(int value) {
        switch (value) {
            case 2:
                return RELATIVE;
            case 1:
                return ALTITUDE;
            default:
                return UNKNOWN;
        }
    }
}
