package com.autel.sdksample.dragonfish.mission.enums;

public enum MissionHoverDirection {
    CLOCKWISE(0), ANTICLOCKWISE(1), UNKNOWN(-1);

    private final int value;

    MissionHoverDirection(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }

    public static MissionHoverDirection find(int value) {
        switch (value) {
            case 0:
                return CLOCKWISE;
            case 1:
                return ANTICLOCKWISE;
            default:
                return UNKNOWN;
        }
    }
}
