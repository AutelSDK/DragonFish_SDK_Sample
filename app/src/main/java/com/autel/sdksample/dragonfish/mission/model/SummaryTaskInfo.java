package com.autel.sdksample.dragonfish.mission.model;


import androidx.annotation.NonNull;


import com.autel.sdksample.dragonfish.mission.enums.DroneType;
import com.autel.sdksample.dragonfish.mission.enums.MapType;
import com.autel.sdksample.dragonfish.mission.enums.MissionType;

import java.util.Objects;

/**
 * 任务的简单信息，用于任务列表页面显示
 */
public class SummaryTaskInfo {
    private Long id;

    /*创建时间*/
    private long createTime;

    /*更新时间，上次飞行时间*/
    private long updateTime;

    /*航线名称*/
    private String name;

    /*航线的地图截图存储路径*/
    private String mapScreenshotPath;

    /* 航线类型 */
    private MissionType missionType = MissionType.MISSION_TYPE_WAYPOINT;

    /* 预估总飞行时间 */
    private int estimateFlyTime;

    /* 预估总飞行长度 */
    private int estimateFlyLength;

    /* 预估总拍照数量 */
    private int totalPhotos;

    /* 已经拍照数量 */
    private int takenPhotos;

    /* 飞行次数 */
    private int flyTimes;

    /* 地图类型 */
    private String mapType = MapType.MAPBOX.value();

    /* 飞机类型 */
    private DroneType droneType;

    /* 矩形或多边形的区域面积，只用于界面展示，暂时不需要存数据库 */
    private int area;

    public SummaryTaskInfo() {
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMapScreenshotPath() {
        return mapScreenshotPath;
    }

    public void setMapScreenshotPath(String mapScreenshotPath) {
        this.mapScreenshotPath = mapScreenshotPath;
    }

    public MissionType getMissionType() {
        return missionType;
    }

    public void setMissionType(MissionType missionType) {
        this.missionType = missionType;
    }

    public int getEstimateFlyTime() {
        return estimateFlyTime;
    }

    public void setEstimateFlyTime(int estimateFlyTime) {
        this.estimateFlyTime = estimateFlyTime;
    }

    public int getEstimateFlyLength() {
        return estimateFlyLength;
    }

    public void setEstimateFlyLength(int estimateFlyLength) {
        this.estimateFlyLength = estimateFlyLength;
    }

    public int getTotalPhotos() {
        return totalPhotos;
    }

    public void setTotalPhotos(int totalPhotos) {
        this.totalPhotos = totalPhotos;
    }

    public int getTakenPhotos() {
        return takenPhotos;
    }

    public void setTakenPhotos(int takenPhotos) {
        this.takenPhotos = takenPhotos;
    }

    public int getFlyTimes() {
        return flyTimes;
    }

    public void setFlyTimes(int flyTimes) {
        this.flyTimes = flyTimes;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }

    public DroneType getDroneType() {
        return droneType;
    }

    public void setDroneType(DroneType droneType) {
        this.droneType = droneType;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getArea() {
        return area;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SummaryTaskInfo)) return false;
        SummaryTaskInfo that = (SummaryTaskInfo) o;
        return getCreateTime() == that.getCreateTime() &&
                getUpdateTime() == that.getUpdateTime() &&
                getEstimateFlyTime() == that.getEstimateFlyTime() &&
                getEstimateFlyLength() == that.getEstimateFlyLength() &&
                getTotalPhotos() == that.getTotalPhotos() &&
                getTakenPhotos() == that.getTakenPhotos() &&
                getFlyTimes() == that.getFlyTimes() &&
                getArea() == that.getArea() &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getMapScreenshotPath(), that.getMapScreenshotPath()) &&
                getMissionType() == that.getMissionType() &&
                Objects.equals(getMapType(), that.getMapType()) &&
                getDroneType() == that.getDroneType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCreateTime(), getUpdateTime(), getName(), getMapScreenshotPath(), getMissionType(), getEstimateFlyTime(), getEstimateFlyLength(), getTotalPhotos(), getTakenPhotos(), getFlyTimes(), getMapType(), getDroneType(), getArea());
    }

    public void resetWithoutId() {
        createTime = 0;
        updateTime = 0;
        estimateFlyTime = 0;
        estimateFlyLength = 0;
        totalPhotos = 0;
        takenPhotos = 0;
        flyTimes = 0;
        name = "";
        mapScreenshotPath = "";
        mapType = "";
        area = 0;
    }

    public void reset() {
        id = null;
        resetWithoutId();
    }

    @Override
    public String toString() {
        return "SummaryTaskInfo{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", name='" + name + '\'' +
                ", mapScreenshotPath='" + mapScreenshotPath + '\'' +
                ", missionType=" + missionType +
                ", estimateFlyTime=" + estimateFlyTime +
                ", estimateFlyLength=" + estimateFlyLength +
                ", totalPhotos=" + totalPhotos +
                ", takenPhotos=" + takenPhotos +
                ", flyTimes=" + flyTimes +
                ", mapType='" + mapType + '\'' +
                ", droneType=" + droneType +
                ", area=" + area +
                '}';
    }
}
