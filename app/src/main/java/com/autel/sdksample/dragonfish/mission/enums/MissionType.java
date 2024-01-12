package com.autel.sdksample.dragonfish.mission.enums;

/**
 * 枚举用于地图区分各种编辑模式
 */
public enum MissionType {
    MISSION_TYPE_WAYPOINT(0), //航点任务
    MISSION_TYPE_MAPPING_RECTANGLE(1), //测绘任务，矩形
    MISSION_TYPE_MAPPING_POLYGON(3), //测绘任务，多边形
    MISSION_TYPE_SAFE(5),//安全组件
    MISSION_TYPE_MAPPING_INSPECTION(7), //巡检任务
    MISSION_TYPE_QUICK_8(8); //8字快速任务
    private final int value;

    MissionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static MissionType find(int value) {
        switch (value) {
            case 1:
                return MISSION_TYPE_MAPPING_RECTANGLE;
            case 3:
                return MISSION_TYPE_MAPPING_POLYGON;
            case 5:
                return MISSION_TYPE_SAFE;
            case 7:
                return MISSION_TYPE_MAPPING_INSPECTION;
            case 8:
                return MISSION_TYPE_QUICK_8;
            default:
                return MISSION_TYPE_WAYPOINT;
        }
    }
}
