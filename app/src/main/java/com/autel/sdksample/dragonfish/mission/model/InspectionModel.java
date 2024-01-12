package com.autel.sdksample.dragonfish.mission.model;

import androidx.annotation.NonNull;

import com.autel.lib.enums.MissionConstant;
import com.autel.sdksample.dragonfish.mission.enums.WaypointType;

import java.util.Objects;

/**
 * @author peiguo.chen
 * @date 2022/5/26
 * 杆塔坐标点
 */
public class InspectionModel  {
    private Long id;

    /* 航点类型 */
    private WaypointType waypointType = WaypointType.INSPECTION;

    /* 航点坐标经纬度、相对高度、绝对海拔*/
    private double latitude;
    private double longitude;
    private float height;
    private float altitude;

    /* 飞行速度 */
    private float speed;

    public InspectionModel() {
        speed = (int) MissionConstant.DEFAULT_WAYPOINT_FLY_SPEED;
        height = MissionConstant.DEFAULT_WAYPOINT_FLY_HEIGHT;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WaypointType getWaypointType() {
        return waypointType;
    }

    public void setWaypointType(WaypointType waypointType) {
        this.waypointType = waypointType;
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

    public float getAltitude() {
        return altitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }


    @NonNull
    @Override
    public InspectionModel clone() {
        try {
            InspectionModel waypoint = (InspectionModel) super.clone();
            return waypoint;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InspectionModel)) return false;
        InspectionModel that = (InspectionModel) o;
        return Double.compare(that.getLatitude(), getLatitude()) == 0 &&
                Double.compare(that.getLongitude(), getLongitude()) == 0 &&
                Float.compare(that.getHeight(), getHeight()) == 0 &&
                Float.compare(that.getAltitude(), getAltitude()) == 0 &&
                Float.compare(that.getSpeed(), getSpeed()) == 0 &&
                Objects.equals(getId(), that.getId()) &&
                getWaypointType() == that.getWaypointType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),  getWaypointType(),getLatitude(), getLongitude(), getHeight(), getAltitude(), getSpeed());
    }

    @Override
    public String toString() {
        return "InspectionModel{" +
                "id=" + id +
                ", waypointType=" + waypointType +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", height=" + height +
                ", altitude=" + altitude +
                ", speed=" + speed +
                '}';
    }
}
