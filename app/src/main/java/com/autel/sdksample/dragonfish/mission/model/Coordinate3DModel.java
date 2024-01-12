package com.autel.sdksample.dragonfish.mission.model;


import androidx.annotation.NonNull;


import java.util.Objects;

public class Coordinate3DModel {
    private Long id;
    private double latitude;
    private double longitude;
    private float altitude;

    public Coordinate3DModel() {
    }

    public Coordinate3DModel(double latitude, double longitude, float altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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

    public float getAltitude() {
        return altitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate3DModel)) return false;
        Coordinate3DModel that = (Coordinate3DModel) o;
        return Double.compare(that.latitude, latitude) == 0 &&
                Double.compare(that.longitude, longitude) == 0 &&
                Float.compare(that.altitude, altitude) == 0 &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, latitude, longitude, altitude);
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Coordinate3D: latitude = " + latitude + ", longitude = " + longitude + ", altitude = " + altitude;
    }
}
