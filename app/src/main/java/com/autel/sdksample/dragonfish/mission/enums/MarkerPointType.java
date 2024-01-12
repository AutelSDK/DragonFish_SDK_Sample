package com.autel.sdksample.dragonfish.mission.enums;

public enum MarkerPointType {
    POINT_TYPE_FREE(0),//自由打点
    POINT_TYPE_QUICK_TASK(1),//快速任务
    POINT_TYPE_SEARCH_LOCATION(2),//搜索位置
    POINT_TYPE_TRACK_OBJECT(3),//追踪对象
    POINT_TYPE_CENTER_OF_VIEW(4),//视场中心
    //临时观察点
    POINT_TYPE_TEMP_POI(5),
    //临时降落点
    POINT_TYPE_LAND(6),
    //参考点
    POINT_TYPE_REFERENCE(7);
    private final int value;

    MarkerPointType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static MarkerPointType find(int value) {
        for (MarkerPointType pointType : values()) {
            if (pointType.value == value) {
                return pointType;
            }
        }
        return POINT_TYPE_FREE;
    }
}