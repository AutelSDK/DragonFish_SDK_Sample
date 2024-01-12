package com.autel.sdksample.dragonfish.mission.model;


import androidx.annotation.NonNull;

import com.autel.lib.enums.MissionConstant;
import com.autel.sdksample.dragonfish.mission.enums.CameraActionType;
import com.autel.sdksample.dragonfish.mission.enums.DroneHeadingControl;
import com.autel.sdksample.dragonfish.mission.enums.WaypointType;
import com.autel.sdksample.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 航点任务
 */
public class WaypointMissionModel {
    private Long id;

    /* 航点列表 */
    private List<WaypointModel> waypointList = new ArrayList<>();

    /* 整体航线的全局高度, 此值不需要保存到数据库 */
    private float waylineHeight = MissionConstant.DEFAULT_WAYPOINT_FLY_HEIGHT;

    /* 整体航线的全局速度, 此值不需要保存到数据库 */
    private float waylineSpeed = MissionConstant.DEFAULT_WAYPOINT_FLY_SPEED;

    /* 偏航角(沿航线方向、手动控制) */
    private DroneHeadingControl droneHeadingControl = DroneHeadingControl.FOLLOW_WAYLINE_DIRECTION;

    /* 相机动作 */
    private CameraActionType cameraActionType = CameraActionType.NONE;

    /* 相机参数 */
    private int cameraInterval;

    /* 云台俯仰角 */
    private float gimbalPitch = -90;
    private float gimbalYaw;

    /* 最高、最低飞行海拔*/
    private int maxFlyHeight = MissionConstant.DEFAULT_WAYPOINT_MAX_FLY_HEIGHT;
    private int minFlyHeight = MissionConstant.DEFAULT_WAYPOINT_MIN_FLY_HEIGHT;

    /* 兴趣点列表 */
    private List<POIPoint> poiPointList = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<WaypointModel> getWaypointList() {
        return waypointList;
    }

    public void setWaypointList(List<WaypointModel> waypointList) {
        this.waypointList = waypointList;
    }

    public float getWaylineHeight() {
        return waylineHeight;
    }

    public void setWaylineHeight(float waylineHeight) {
        this.waylineHeight = waylineHeight;
    }

    public float getWaylineSpeed() {
        return waylineSpeed;
    }

    public void setWaylineSpeed(float waylineSpeed) {
        this.waylineSpeed = waylineSpeed;
    }

    public int getMaxFlyHeight() {
        return maxFlyHeight;
    }

    public void setMaxFlyHeight(int maxFlyHeight) {
        this.maxFlyHeight = maxFlyHeight;
    }

    public int getMinFlyHeight() {
        return minFlyHeight;
    }

    public void setMinFlyHeight(int minFlyHeight) {
        this.minFlyHeight = minFlyHeight;
    }

    public DroneHeadingControl getDroneHeadingControl() {
        return droneHeadingControl;
    }

    public void setDroneHeadingControl(DroneHeadingControl droneHeadingControl) {
        this.droneHeadingControl = droneHeadingControl;
    }

    public CameraActionType getCameraActionType() {
        return cameraActionType;
    }

    public void setCameraActionType(CameraActionType cameraActionType) {
        this.cameraActionType = cameraActionType;
    }

    public Integer getCameraInterval() {
        return cameraInterval;
    }

    public void setCameraInterval(Integer cameraInterval) {
        this.cameraInterval = cameraInterval;
    }

    public float getGimbalPitch() {
        return gimbalPitch;
    }

    public void setGimbalPitch(float gimbalPitch) {
        this.gimbalPitch = gimbalPitch;
    }

    public float getGimbalYaw() {
        return gimbalYaw;
    }

    public void setGimbalYaw(float gimbalYaw) {
        this.gimbalYaw = gimbalYaw;
    }

    public List<POIPoint> getPoiPointList() {
        return poiPointList;
    }

    public void setPoiPointList(List<POIPoint> poiPointList) {
        this.poiPointList = poiPointList;
    }

    public void setCameraInterval(int cameraInterval) {
        this.cameraInterval = cameraInterval;
    }


    public int getWaypointSize() {
        int length = 0;
        if (CollectionUtil.isNotEmpty(waypointList)) {
            for (WaypointModel waypoint : waypointList) {
                if (waypoint.getWaypointType() == WaypointType.WAYPOINT) {
                    length++;
                }
            }
        }
        return length;
    }

    public int getPOIPointSize() {
        int length = 0;
        if (CollectionUtil.isNotEmpty(poiPointList)) {
            length = poiPointList.size();
        }
        return length;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        WaypointMissionModel missionModel = (WaypointMissionModel) super.clone();
        List<WaypointModel> waypointModelList = new ArrayList<>();
        List<POIPoint> poiModelList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(waypointList)) {
            for (WaypointModel model : waypointList) {
                waypointModelList.add(model.clone());
            }
        }
        if (CollectionUtil.isNotEmpty(poiPointList)) {
            for (POIPoint model : poiPointList) {
                poiModelList.add(model.clone());
            }
        }
        missionModel.setWaypointList(waypointModelList);
        missionModel.setPoiPointList(poiModelList);
        return missionModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WaypointMissionModel)) return false;
        WaypointMissionModel that = (WaypointMissionModel) o;
        return Float.compare(that.getWaylineHeight(), getWaylineHeight()) == 0 &&
                Float.compare(that.getWaylineSpeed(), getWaylineSpeed()) == 0 &&
                getCameraInterval().equals(that.getCameraInterval()) &&
                Float.compare(that.getGimbalPitch(), getGimbalPitch()) == 0 &&
                Float.compare(that.getGimbalYaw(), getGimbalYaw()) == 0 &&
                getMaxFlyHeight() == that.getMaxFlyHeight() &&
                getMinFlyHeight() == that.getMinFlyHeight() &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getWaypointList(), that.getWaypointList()) &&
                getDroneHeadingControl() == that.getDroneHeadingControl() &&
                getCameraActionType() == that.getCameraActionType() &&
                Objects.equals(getPoiPointList(), that.getPoiPointList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getWaypointList(), getWaylineHeight(), getWaylineSpeed(), getDroneHeadingControl(), getCameraActionType(), getCameraInterval(), getGimbalPitch(), getGimbalYaw(), getMaxFlyHeight(), getMinFlyHeight(), getPoiPointList());
    }

    @Override
    public String toString() {
        return "WaypointMissionModel{" +
                "id=" + id +
                ", waypointList=" + waypointList +
                ", waylineHeight=" + waylineHeight +
                ", waylineSpeed=" + waylineSpeed +
                ", droneHeadingControl=" + droneHeadingControl +
                ", cameraActionType=" + cameraActionType +
                ", cameraInterval=" + cameraInterval +
                ", gimbalPitch=" + gimbalPitch +
                ", gimbalYaw=" + gimbalYaw +
                ", maxFlyHeight=" + maxFlyHeight +
                ", minFlyHeight=" + minFlyHeight +
                ", poiPointList=" + poiPointList +
                '}';
    }
}
