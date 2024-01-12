package com.autel.sdksample.dragonfish.mission.enums;

public enum MissionFinishActionType {

    GO_HOME(2,1), HOVER(1,0), UNKNOWN(-1,0);

    private final int value;//传输给协议的值
    private final int position;//传输给界面的值，代表在界面的位置
    MissionFinishActionType(int value, int position) {
        this.value = value;
        this.position = position;
    }
    public int getValue() {
        return value;
    }

    public int getPosition() {
        return position;
    }

    public static MissionFinishActionType find(int value) {
        if (GO_HOME.value == value) {
            return GO_HOME;
        }
        if (HOVER.value == value) {
            return HOVER;
        }
        return UNKNOWN;

    }
}
