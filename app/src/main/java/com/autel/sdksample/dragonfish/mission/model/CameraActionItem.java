package com.autel.sdksample.dragonfish.mission.model;


import androidx.annotation.NonNull;

import com.autel.lib.enums.MissionConstant;
import com.autel.sdksample.dragonfish.mission.enums.CameraActionType;
import com.autel.sdksample.dragonfish.mission.enums.DroneHeadingControl;

import java.util.Objects;

public class CameraActionItem {

    private Long id;

    /* 相机动作 */
    private CameraActionType cameraActionType = CameraActionType.NONE;

    /* 相机参数，定时拍照和定距拍照的间隔 */
    private int cameraInterval = 2;

    /* 云台俯仰角 */
    private float gimbalPitch;

    private DroneHeadingControl droneHeadingControl = DroneHeadingControl.FOLLOW_WAYLINE_DIRECTION;

    /* 偏航角(机头朝向) */
    private float droneYaw = MissionConstant.DEFAULT_WAYPOINT_HEADING;

    /* 动作时长(拍照时长、录像时长) */
    private int actionTimeLen = 10;

    private boolean isSelected;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CameraActionType getCameraActionType() {
        return cameraActionType;
    }

    public void setCameraActionType(CameraActionType cameraActionType) {
        this.cameraActionType = cameraActionType;
    }

    public int getCameraInterval() {
        return cameraInterval;
    }

    public void setCameraInterval(int cameraInterval) {
        this.cameraInterval = cameraInterval;
    }

    public float getGimbalPitch() {
        return gimbalPitch;
    }

    public void setGimbalPitch(float gimbalPitch) {
        this.gimbalPitch = gimbalPitch;
    }

    public DroneHeadingControl getDroneHeadingControl() {
        return droneHeadingControl;
    }

    public void setDroneHeadingControl(DroneHeadingControl droneHeadingControl) {
        this.droneHeadingControl = droneHeadingControl;
    }

    public float getDroneYaw() {
        return droneYaw;
    }

    public void setDroneYaw(float droneYaw) {
        this.droneYaw = droneYaw;
    }

    public int getActionTimeLen() {
        return actionTimeLen;
    }

    public void setActionTimeLen(int actionTimeLen) {
        this.actionTimeLen = actionTimeLen;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @NonNull
    @Override
    protected CameraActionItem clone() {
        try {
            return (CameraActionItem) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CameraActionItem)) return false;
        CameraActionItem that = (CameraActionItem) o;
        return getCameraInterval() == that.getCameraInterval() &&
                Float.compare(that.getGimbalPitch(), getGimbalPitch()) == 0 &&
                Float.compare(that.getDroneYaw(), getDroneYaw()) == 0 &&
                getActionTimeLen() == that.getActionTimeLen() &&
                Objects.equals(getId(), that.getId()) &&
                getCameraActionType() == that.getCameraActionType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCameraActionType(), getCameraInterval(), getGimbalPitch(), getDroneYaw(), getActionTimeLen());
    }
}
