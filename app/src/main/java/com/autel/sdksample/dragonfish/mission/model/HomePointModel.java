package com.autel.sdksample.dragonfish.mission.model;

import androidx.annotation.NonNull;

import com.autel.lib.enums.MissionConstant;
import com.autel.sdksample.dragonfish.mission.enums.MissionAltitudeType;
import com.autel.sdksample.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 返航点信息
 */
public class HomePointModel {
    private Long id;
    /* 返航点坐标、高度、绝对海拔 */
    private float landHeight; //降落的模态切换高度

    private double latitude;
    private double longitude;
    private float height;
    private MissionAltitudeType altitudeType = MissionAltitudeType.RELATIVE;

    /*离场点坐标、高度、绝对海拔、半径*/
    private double upHoverLatitude;
    private double upHoverLongitude;
    private float upHoverHeight;
    private MissionAltitudeType upHoverAltitudeType = MissionAltitudeType.RELATIVE;
    private float upHoverRadius;

    /*进场点坐标、高度、绝对海拔、半径*/
    private double downHoverLatitude;
    private double downHoverLongitude;
    private float downHoverHeight;
    private MissionAltitudeType downHoverAltitudeType = MissionAltitudeType.RELATIVE;
    private float downHoverRadius;

    /* Home点位置是否跟随飞机位置 */
    private boolean isFollowDroneLocation = true;

    /*紧急降落点坐标、高度*/
    private List<Coordinate3DModel> forceLandingPoints = new ArrayList<>();

    public HomePointModel() {
        landHeight = MissionConstant.DEFAULT_GO_HOME_HEIGHT;
        upHoverHeight = MissionConstant.DEFAULT_HOMEPOINT_HOVER_HEIGHT;
        upHoverRadius = MissionConstant.MIN_HOVER_RADIUS;
        downHoverHeight = MissionConstant.DEFAULT_HOMEPOINT_HOVER_HEIGHT;
        downHoverRadius = MissionConstant.MIN_HOVER_RADIUS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getLandHeight() {
        return landHeight;
    }

    public void setLandHeight(float landHeight) {
        this.landHeight = landHeight;
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

    public double getUpHoverLatitude() {
        return upHoverLatitude;
    }

    public void setUpHoverLatitude(double upHoverLatitude) {
        this.upHoverLatitude = upHoverLatitude;
    }

    public double getUpHoverLongitude() {
        return upHoverLongitude;
    }

    public void setUpHoverLongitude(double upHoverLongitude) {
        this.upHoverLongitude = upHoverLongitude;
    }

    public double getDownHoverLatitude() {
        return downHoverLatitude;
    }

    public void setDownHoverLatitude(double downHoverLatitude) {
        this.downHoverLatitude = downHoverLatitude;
    }

    public double getDownHoverLongitude() {
        return downHoverLongitude;
    }

    public void setDownHoverLongitude(double downHoverLongitude) {
        this.downHoverLongitude = downHoverLongitude;
    }

    public float getUpHoverHeight() {
        return upHoverHeight;
    }

    public void setUpHoverHeight(float upHoverHeight) {
        this.upHoverHeight = upHoverHeight;
    }

    public float getUpHoverRadius() {
        return upHoverRadius;
    }

    public void setUpHoverRadius(float upHoverRadius) {
        this.upHoverRadius = upHoverRadius;
    }

    public float getDownHoverHeight() {
        return downHoverHeight;
    }

    public void setDownHoverHeight(float downHoverHeight) {
        this.downHoverHeight = downHoverHeight;
    }

    public float getDownHoverRadius() {
        return downHoverRadius;
    }

    public void setDownHoverRadius(float downHoverRadius) {
        this.downHoverRadius = downHoverRadius;
    }

    public MissionAltitudeType getAltitudeType() {
        return altitudeType;
    }

    public void setAltitudeType(MissionAltitudeType altitudeType) {
        this.altitudeType = altitudeType;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public MissionAltitudeType getUpHoverAltitudeType() {
        return upHoverAltitudeType;
    }

    public void setUpHoverAltitudeType(MissionAltitudeType upHoverAltitudeType) {
        this.upHoverAltitudeType = upHoverAltitudeType;
    }

    public MissionAltitudeType getDownHoverAltitudeType() {
        return downHoverAltitudeType;
    }

    public void setDownHoverAltitudeType(MissionAltitudeType downHoverAltitudeType) {
        this.downHoverAltitudeType = downHoverAltitudeType;
    }

    public boolean isFollowDroneLocation() {
        return isFollowDroneLocation;
    }

    public void setFollowDroneLocation(boolean followDroneLocation) {
        isFollowDroneLocation = followDroneLocation;
    }

    public List<Coordinate3DModel> getForceLandingPoints() {
        return forceLandingPoints;
    }

    public void setForceLandingPoints(List<Coordinate3DModel> forceLandingPoints) {
        this.forceLandingPoints = forceLandingPoints;
    }

    public void reset() {
        resetWithoutForce();
        forceLandingPoints.clear();
    }

    public void resetWithoutForce() {
        id = null;
        latitude = 0;
        longitude = 0;
        upHoverLatitude = 0;
        upHoverLongitude = 0;
        downHoverLatitude = 0;
        downHoverLongitude = 0;
        landHeight = MissionConstant.DEFAULT_GO_HOME_HEIGHT;
        upHoverHeight = MissionConstant.DEFAULT_HOMEPOINT_HOVER_HEIGHT;
        upHoverRadius = MissionConstant.MIN_HOVER_RADIUS;
        downHoverHeight = MissionConstant.DEFAULT_HOMEPOINT_HOVER_HEIGHT;
        downHoverRadius = MissionConstant.MIN_HOVER_RADIUS;
        isFollowDroneLocation = true;
    }

    public void resetWithoutID() {
        forceLandingPoints.clear();
        latitude = 0;
        longitude = 0;
        upHoverLatitude = 0;
        upHoverLongitude = 0;
        downHoverLatitude = 0;
        downHoverLongitude = 0;
        landHeight = MissionConstant.DEFAULT_GO_HOME_HEIGHT;
        upHoverHeight = MissionConstant.DEFAULT_HOMEPOINT_HOVER_HEIGHT;
        upHoverRadius = MissionConstant.MIN_HOVER_RADIUS;
        downHoverHeight = MissionConstant.DEFAULT_HOMEPOINT_HOVER_HEIGHT;
        downHoverRadius = MissionConstant.MIN_HOVER_RADIUS;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        HomePointModel model = (HomePointModel) super.clone();
        List<Coordinate3DModel> pointList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(forceLandingPoints)) {
            for (Coordinate3DModel point : forceLandingPoints) {
                pointList.add((Coordinate3DModel) point.clone());
            }
        }
        model.setForceLandingPoints(pointList);
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HomePointModel)) return false;
        HomePointModel that = (HomePointModel) o;
        return Float.compare(that.getLandHeight(), getLandHeight()) == 0 &&
                Float.compare(that.getHeight(), getHeight()) == 0 &&
                Double.compare(that.getLatitude(), getLatitude()) == 0 &&
                Double.compare(that.getLongitude(), getLongitude()) == 0 &&
                Double.compare(that.getUpHoverLatitude(), getUpHoverLatitude()) == 0 &&
                Double.compare(that.getUpHoverLongitude(), getUpHoverLongitude()) == 0 &&
                Float.compare(that.getUpHoverHeight(), getUpHoverHeight()) == 0 &&
                that.getUpHoverAltitudeType() == getUpHoverAltitudeType() &&
                Float.compare(that.getUpHoverRadius(), getUpHoverRadius()) == 0 &&
                Double.compare(that.getDownHoverLatitude(), getDownHoverLatitude()) == 0 &&
                Double.compare(that.getDownHoverLongitude(), getDownHoverLongitude()) == 0 &&
                Float.compare(that.getDownHoverHeight(), getDownHoverHeight()) == 0 &&
                that.getDownHoverAltitudeType() == getDownHoverAltitudeType() &&
                Float.compare(that.getDownHoverRadius(), getDownHoverRadius()) == 0 &&
                isFollowDroneLocation() == that.isFollowDroneLocation() &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getForceLandingPoints(), that.getForceLandingPoints());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLandHeight(), getHeight(), getLatitude(), getLongitude(), getUpHoverLatitude(), getUpHoverLongitude(), getUpHoverHeight(), getUpHoverAltitudeType(), getUpHoverRadius(), getDownHoverLatitude(), getDownHoverLongitude(), getDownHoverHeight(), getDownHoverAltitudeType(), getDownHoverRadius(), isFollowDroneLocation(), getForceLandingPoints());
    }

    public boolean isValid() {
        return latitude != 0f && longitude != 0f && upHoverLatitude != 0f && upHoverLongitude != 0f
                && downHoverLatitude != 0f && downHoverLongitude != 0f;
    }

    @Override
    public String toString() {
        return "HomePointModel{" +
                "id=" + id +
                ", landHeight=" + landHeight +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", height=" + height +
                ", altitudeType=" + altitudeType +
                ", upHoverLatitude=" + upHoverLatitude +
                ", upHoverLongitude=" + upHoverLongitude +
                ", upHoverHeight=" + upHoverHeight +
                ", upHoverAltitudeType=" + upHoverAltitudeType +
                ", upHoverRadius=" + upHoverRadius +
                ", downHoverLatitude=" + downHoverLatitude +
                ", downHoverLongitude=" + downHoverLongitude +
                ", downHoverHeight=" + downHoverHeight +
                ", downHoverAltitudeType=" + downHoverAltitudeType +
                ", downHoverRadius=" + downHoverRadius +
                ", isFollowDroneLocation=" + isFollowDroneLocation +
                ", forceLandingPoints=" + forceLandingPoints +
                '}';
    }
}
