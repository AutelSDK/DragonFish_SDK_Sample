package com.autel.sdksample.dragonfish.mission.model;

import androidx.annotation.NonNull;

import com.autel.lib.enums.MissionConstant;
import com.autel.sdksample.base.mission.AutelLatLng;

import java.util.Objects;

public class MappingVertexPoint extends BasePointModel {
    private Long id;

    private double latitude;
    private double longitude;
    private float height = MissionConstant.DEFAULT_MAPPING_FLY_HEIGHT;   //相对高度
    private float altitude; //绝对海拔

    public MappingVertexPoint() {
    }

    public MappingVertexPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public MappingVertexPoint(double latitude, double longitude, float height) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.height = height;
    }

    public MappingVertexPoint(double latitude, double longitude, float height, float altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.height = height;
        this.altitude = altitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MappingVertexPoint)) return false;
        MappingVertexPoint that = (MappingVertexPoint) o;
        return Double.compare(that.latitude, latitude) == 0 &&
                Double.compare(that.longitude, longitude) == 0 &&
                Float.compare(that.height, height) == 0 &&
                Float.compare(that.altitude, altitude) == 0 &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, latitude, longitude, height, altitude);
    }

    @Override
    public String toString() {
        return "MappingVertexPoint{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", height=" + height +
                ", altitude=" + altitude +
                '}' + super.toString();
    }

    public AutelLatLng getAutelLatLng() {
        return new AutelLatLng(latitude, longitude);
    }
}
