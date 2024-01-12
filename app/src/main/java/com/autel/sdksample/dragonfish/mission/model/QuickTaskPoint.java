package com.autel.sdksample.dragonfish.mission.model;

import com.autel.AutelNet2.aircraft.mission.enmus.LocationStatus;
import com.autel.lib.enums.MissionConstant;
import com.autel.sdksample.base.mission.AutelLatLng;
import com.autel.sdksample.dragonfish.mission.enums.MissionAltitudeType;

public class QuickTaskPoint {
    private LocationStatus locationStatus = LocationStatus.QUICK_MISSION;
    private double latitude;
    private double longitude;
    private float height = MissionConstant.DEFAULT_QUICK_POINT_HEIGHT;
    private float safeHeight = MissionConstant.DEFAULT_SAFE_HEIGHT;
    private float radius = MissionConstant.MIN_HOVER_RADIUS;
    private double distance = MissionConstant.MIN_HOVER_RADIUS * 2 + 5;
    private double angle = MissionConstant.DEFAULT_QUICK_POINT_ANGLE;

    private MissionAltitudeType altitudeType = MissionAltitudeType.ALTITUDE;  // //海拔高度类型 2相对，1绝对

    private String taskName;
    private boolean isCollect;

    public MissionAltitudeType getAltitudeType() {
        return altitudeType;
    }

    public void setAltitudeType(MissionAltitudeType altitudeType) {
        this.altitudeType = altitudeType;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public QuickTaskPoint() {

    }

    public QuickTaskPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getSafeHeight() {
        return safeHeight;
    }

    public void setSafeHeight(float safeHeight) {
        this.safeHeight = safeHeight;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        if (distance < radius * 2 + 5) {
            distance = radius * 2 + 5;
        }
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public LocationStatus getLocationStatus() {
        return locationStatus;
    }

    public void setLocationStatus(LocationStatus locationStatus) {
        this.locationStatus = locationStatus;
    }

    public AutelLatLng getAutelLatLng() {
        return new AutelLatLng(latitude, longitude);
    }

    public boolean isValidLatlng() {
        return latitude > -90 && latitude < 90 && latitude != 0d
                && longitude > -180 && longitude < 180 && longitude != 0d;
    }

    public void reset() {
        locationStatus = LocationStatus.QUICK_MISSION;
        latitude = 0;
        longitude = 0;
        height = MissionConstant.DEFAULT_QUICK_POINT_HEIGHT;
        radius = MissionConstant.MIN_HOVER_RADIUS;
        distance = MissionConstant.MIN_HOVER_RADIUS * 2 + 5;
        angle = MissionConstant.DEFAULT_QUICK_POINT_ANGLE;
    }

    @Override
    public String toString() {
        return "QuickTaskPoint{" +
                "locationStatus=" + locationStatus +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", height=" + height +
                ", safeHeight=" + safeHeight +
                ", radius=" + radius +
                ", distance=" + distance +
                ", angle=" + angle +
                ", altitudeType=" + altitudeType +
                ", taskName='" + taskName + '\'' +
                ", isCollect=" + isCollect +
                ", time=" + time +
                '}';
    }
}
