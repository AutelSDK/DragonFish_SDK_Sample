package com.autel.sdksample.dragonfish.mission.model;

import androidx.annotation.NonNull;

import com.autel.lib.enums.MissionConstant;
import com.autel.sdksample.base.mission.AutelLatLng;
import com.autel.sdksample.dragonfish.mission.enums.CameraActionType;
import com.autel.sdksample.dragonfish.mission.enums.MissionActionType;
import com.autel.sdksample.dragonfish.mission.enums.MissionAltitudeType;
import com.autel.sdksample.dragonfish.mission.enums.MissionHoverDirection;
import com.autel.sdksample.dragonfish.mission.enums.WaypointType;
import com.autel.sdksample.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 航点
 */
public class WaypointModel extends BasePointModel {

    private Long id;
    /* 航点动作 */
    private MissionActionType missionActionType = MissionActionType.TURN_OVER;

    /* 相机动作 */
    private CameraActionType cameraActionType = CameraActionType.NONE;

    /* 航点类型 */
    private WaypointType waypointType = WaypointType.WAYPOINT;

    /* 相机参数，定时拍照和定距拍照的间隔 */
    private int cameraParam;

    /* 航点坐标经纬度、相对高度、绝对海拔*/
    private double latitude;
    private double longitude;
    private float height;
    private MissionAltitudeType missionAltitudeType = MissionAltitudeType.RELATIVE;

    /* 飞行速度 */
    private float speed;

    /* 云台平移角 */
    private float gimbalYaw;

    /* 云台俯仰角 */
    private float gimbalPitch = 0;

    /* 盘旋方向，只针对航点动作为盘旋时有效 */
    private MissionHoverDirection hoverDirection = MissionHoverDirection.CLOCKWISE;

    /* 盘旋半径，只针对航点动作为盘旋时有效 */
    private float hoverRadius;

    /* 盘旋圈数，只针对航点动作为盘旋时有效 */
    private int hoverCylinderNumber;

    /* 拍摄距离，只针对航点动作为兴趣点时有效 */
    private float shootDistance;

    /* 水平角度，只针对航点动作为兴趣点时有效 */
    private float shootHorizontalAngle;

    /* 进入兴趣点的角度，只针对航点动作为兴趣点时有效 */
    private float enterAngle;

    /* 兴趣点 Index */
    private int poiIndex = -1;

    /* 进入兴趣点后的盘旋方向 ，只针对航点动作为兴趣点时有效 */
    private MissionHoverDirection orbitDirection;

    /* 相机动作列表 */
    private List<CameraActionItem> missionActions = new ArrayList<>();

    //预留参数 八字盘旋参考距离，单位m，[205,10000]
    private float wpReserved1;

    public WaypointModel() {
        speed = (int) MissionConstant.DEFAULT_WAYPOINT_FLY_SPEED;
        height = MissionConstant.DEFAULT_WAYPOINT_FLY_HEIGHT;
        hoverRadius = Math.max(MissionConstant.MIN_HOVER_RADIUS, MissionConstant.MIN_HOVER_RADIUS);
        hoverCylinderNumber = MissionConstant.DEFAULT_WAYPOINT_HOVER_CIRCLES;
        shootDistance = MissionConstant.DEFAULT_WAYPOINT_ORBIT_SHOOT_DISTANCE;
        shootHorizontalAngle = MissionConstant.DEFAULT_WAYPOINT_ORBIT_HORIZONTAL_ANGLE;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MissionActionType getMissionActionType() {
        return missionActionType;
    }

    public void setMissionActionType(MissionActionType missionActionType) {
        this.missionActionType = missionActionType;
    }

    public CameraActionType getCameraActionType() {
        return cameraActionType;
    }

    public void setCameraActionType(CameraActionType cameraActionType) {
        this.cameraActionType = cameraActionType;
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

    public MissionHoverDirection getHoverDirection() {
        return hoverDirection;
    }

    public void setHoverDirection(MissionHoverDirection hoverDirection) {
        this.hoverDirection = hoverDirection;
    }

    public float getHoverRadius() {
        return hoverRadius;
    }

    public void setHoverRadius(float hoverRadius) {
        this.hoverRadius = hoverRadius;
    }

    public int getHoverCylinderNumber() {
        return hoverCylinderNumber;
    }

    public void setHoverCylinderNumber(int hoverCylinderNumber) {
        this.hoverCylinderNumber = hoverCylinderNumber;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getGimbalYaw() {
        return gimbalYaw;
    }

    public void setGimbalYaw(float gimbalYaw) {
        this.gimbalYaw = gimbalYaw;
    }

    public float getGimbalPitch() {
        return gimbalPitch;
    }

    public void setGimbalPitch(float gimbalPitch) {
        this.gimbalPitch = gimbalPitch;
    }

    public float getShootDistance() {
        return shootDistance;
    }

    public void setShootDistance(float shootDistance) {
        this.shootDistance = shootDistance;
    }

    public float getShootHorizontalAngle() {
        return shootHorizontalAngle;
    }

    public void setShootHorizontalAngle(float shootHorizontalAngle) {
        this.shootHorizontalAngle = shootHorizontalAngle;
    }

    public MissionAltitudeType getMissionAltitudeType() {
        return missionAltitudeType;
    }

    public void setMissionAltitudeType(int missionAltitudeType) {
        this.missionAltitudeType = MissionAltitudeType.find(missionAltitudeType);
    }

    public void setMissionAltitudeType(MissionAltitudeType altitude) {
        this.missionAltitudeType = altitude;
    }

    public float getEnterAngle() {
        return enterAngle;
    }

    public void setEnterAngle(float enterAngle) {
        this.enterAngle = enterAngle;
    }

    public MissionHoverDirection getOrbitDirection() {
        return orbitDirection;
    }

    public void setOrbitDirection(MissionHoverDirection orbitDirection) {
        this.orbitDirection = orbitDirection;
    }

    public int getPoiIndex() {
        return poiIndex;
    }

    public void setPoiIndex(int poiIndex) {
        this.poiIndex = poiIndex;
    }

    public int getCameraParam() {
        return cameraParam;
    }

    public void setCameraParam(int cameraParam) {
        this.cameraParam = cameraParam;
    }

    public List<CameraActionItem> getMissionActions() {
        return missionActions;
    }

    public void setMissionActions(List<CameraActionItem> missionActions) {
        this.missionActions = missionActions;
    }

    public float getWpReserved1() {
        return wpReserved1;
    }

    public void setWpReserved1(float wpReserved1) {
        this.wpReserved1 = wpReserved1;
    }

    public float getPointHeading() {
        float heading = MissionConstant.DEFAULT_WAYPOINT_HEADING;
        List<CameraActionItem> actions = getMissionActions();
        if (CollectionUtil.isNotEmpty(actions)) {
            heading = actions.get(0).getDroneYaw();
        }
        return heading;
    }

    @NonNull
    @Override
    public WaypointModel clone() {
        try {
            WaypointModel waypoint = (WaypointModel) super.clone();
            List<CameraActionItem> cameraActionItems = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(missionActions)) {
                for (CameraActionItem model : missionActions) {
                    cameraActionItems.add(model.clone());
                }
            }
            waypoint.setMissionActions(cameraActionItems);
            return waypoint;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WaypointModel)) return false;
        WaypointModel that = (WaypointModel) o;
        return getCameraParam() == that.getCameraParam() &&
                Double.compare(that.getLatitude(), getLatitude()) == 0 &&
                Double.compare(that.getLongitude(), getLongitude()) == 0 &&
                Float.compare(that.getHeight(), getHeight()) == 0 &&
                Objects.equals(that.getMissionAltitudeType(), getMissionAltitudeType()) &&
                Float.compare(that.getSpeed(), getSpeed()) == 0 &&
                Float.compare(that.getGimbalYaw(), getGimbalYaw()) == 0 &&
                Float.compare(that.getGimbalPitch(), getGimbalPitch()) == 0 &&
                Float.compare(that.getHoverRadius(), getHoverRadius()) == 0 &&
                getHoverCylinderNumber() == that.getHoverCylinderNumber() &&
                Float.compare(that.getShootDistance(), getShootDistance()) == 0 &&
                Float.compare(that.getShootHorizontalAngle(), getShootHorizontalAngle()) == 0 &&
                Float.compare(that.getEnterAngle(), getEnterAngle()) == 0 &&
                getPoiIndex() == that.getPoiIndex() &&
                Objects.equals(getId(), that.getId()) &&
                getMissionActionType() == that.getMissionActionType() &&
                getCameraActionType() == that.getCameraActionType() &&
                getWaypointType() == that.getWaypointType() &&
                getHoverDirection() == that.getHoverDirection() &&
                getOrbitDirection() == that.getOrbitDirection() &&
                Objects.equals(getMissionActions(), that.getMissionActions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getMissionActionType(), getCameraActionType(), getWaypointType(), getCameraParam(), getLatitude(), getLongitude(), getHeight(), getMissionAltitudeType(), getSpeed(), getGimbalYaw(), getGimbalPitch(), getHoverDirection(), getHoverRadius(), getHoverCylinderNumber(), getShootDistance(), getShootHorizontalAngle(), getEnterAngle(), getPoiIndex(), getOrbitDirection(), getMissionActions());
    }

    public AutelLatLng getAutelLatLng() {
        return new AutelLatLng(latitude, longitude);
    }

    @Override
    public String toString() {
        return "WaypointModel{" +
                "id=" + id +
                ", missionActionType=" + missionActionType +
                ", cameraActionType=" + cameraActionType +
                ", waypointType=" + waypointType +
                ", cameraParam=" + cameraParam +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", height=" + height +
                ", missionAltitudeType=" + missionAltitudeType +
                ", speed=" + speed +
                ", gimbalYaw=" + gimbalYaw +
                ", gimbalPitch=" + gimbalPitch +
                ", hoverDirection=" + hoverDirection +
                ", hoverRadius=" + hoverRadius +
                ", hoverCylinderNumber=" + hoverCylinderNumber +
                ", shootDistance=" + shootDistance +
                ", shootHorizontalAngle=" + shootHorizontalAngle +
                ", enterAngle=" + enterAngle +
                ", poiIndex=" + poiIndex +
                ", orbitDirection=" + orbitDirection +
                ", missionActions=" + missionActions +
                ", wpReserved1=" + wpReserved1 +
                '}';
    }
}
