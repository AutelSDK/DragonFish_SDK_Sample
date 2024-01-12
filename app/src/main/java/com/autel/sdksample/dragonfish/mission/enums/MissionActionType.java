package com.autel.sdksample.dragonfish.mission.enums;

public enum MissionActionType {
    ORBIT(0), // 兴趣点
    TURN_AHEAD(1), //1：提前转弯
    TURN_OVER(2), //2：过点转弯
    TIMED_SURROUND(3), //3：定时盘旋
    ORDER_SURROUND(4), //4：定圈盘旋

    UNKNOWN(-1);

    private final int value;

    MissionActionType(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }

    public static MissionActionType find(int value) {
        switch (value) {
            case 0:
                return ORBIT;
            case 1:
                return TURN_AHEAD;
            case 2:
                return TURN_OVER;
            case 3:
                return TIMED_SURROUND;
            case 4:
                return ORDER_SURROUND;
            default:
                return UNKNOWN;
        }
    }

    public boolean isSurround() {
        return value >= TIMED_SURROUND.getValue();
    }
}
