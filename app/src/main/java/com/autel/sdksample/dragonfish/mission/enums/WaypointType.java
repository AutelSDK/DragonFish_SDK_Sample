package com.autel.sdksample.dragonfish.mission.enums;

/**
 * <p> 功能描述：航点类型 <br>
 * <p>
 * 详细描述：
 * 航点、观察点、离场点、进场点、home点等
 *
 */
public enum WaypointType {
    WAYPOINT(0),
    POIPOINT(1),
    UPHOVER(2),
    DOWNHOVER(3),
    HOMEPOINT(4),
    ALL(5),
    VERTEX(7), //多边形顶点
    MAPPINGWAYPOINT(8), //多边形途经点
    DRONE(9), //飞机位置
    TEMP_POI_POINT(10), //临时观察点
    INSPECTION(11), //杆塔坐标点
    OVERVIEW(12), //多边形总览
    GEOFENCE_OVERVIEW(13), //电子围栏总览
    UNKNOWN(-1);

    private final int value;

    WaypointType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static WaypointType find(int value) {
        switch (value) {
            case 0:
                return WAYPOINT;
            case 1:
                return POIPOINT;
            case 2:
                return UPHOVER;
            case 3:
                return DOWNHOVER;
            case 4:
                return HOMEPOINT;
            case 5:
                return ALL;
            case 7:
                return VERTEX;
            case 8:
                return MAPPINGWAYPOINT;
            case 9:
                return DRONE;
            case 10:
                return TEMP_POI_POINT;
            case 11:
                return INSPECTION;
            case 12:
                return OVERVIEW;
            case 13:
                return GEOFENCE_OVERVIEW;
            default:
                return UNKNOWN;
        }
    }
}
