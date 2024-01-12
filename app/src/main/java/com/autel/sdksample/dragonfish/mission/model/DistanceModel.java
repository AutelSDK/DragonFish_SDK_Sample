package com.autel.sdksample.dragonfish.mission.model;



import com.autel.sdksample.dragonfish.mission.enums.WaypointType;

import java.util.Objects;

/**
 * Created by ZDG
 * Date: 2019-09-25
 */
public class DistanceModel {
    private short type;
    private double latitude;
    private double longitude;
    private double altitude;
    private double distance;
    private int index;

    public DistanceModel(short type, double latitude, double longitude, double altitude, double distance, int index) {
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.distance = distance;
        this.index = index;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DistanceModel)) return false;
        DistanceModel that = (DistanceModel) o;
        return Double.compare(that.getLatitude(), getLatitude()) == 0 &&
                Double.compare(that.getLongitude(), getLongitude()) == 0 &&
                Double.compare(that.getDistance(), getDistance()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLatitude(), getLongitude(), getDistance());
    }

    @Override
    public String toString() {
        return "DistanceModel{" +
                "type=" + type +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                ", distance=" + distance +
                ", index=" + index +
                '}';
    }

    //标签类型：
    //0：垂直起飞到达点；
    //-1：上升表征点；
    //-2：上升盘旋点；
    //-3：下降盘旋点；
    //-4：下降表征点；
    //-5：垂直降落开始点；
    //1~wpNum：第1到wpNum号航点
    public WaypointType getPointType() {
        switch (type) {
            case 0:
                return WaypointType.DRONE;
            case -1:
            case -4:
                return WaypointType.UNKNOWN;
            case -2:
                return WaypointType.UPHOVER;
            case -3:
                return WaypointType.DOWNHOVER;
            case -5:
                return WaypointType.HOMEPOINT;
        }
        return WaypointType.WAYPOINT;
    }
}
