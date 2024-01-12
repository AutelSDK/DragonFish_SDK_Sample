package com.autel.sdksample.dragonfish.mission.model;

import androidx.annotation.NonNull;

import com.autel.lib.enums.MissionConstant;
import com.autel.sdksample.dragonfish.mission.enums.MissionType;
import com.autel.sdksample.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 测绘任务
 */
public class MappingMissionModel {
    private Long id;

    /* 地面分辨率 */
    private float groundResolution;

    /* 航向重叠率 */
    private float courseRate;

    /* 旁向重叠率 */
    private float sideRate;

    /* 主航线角度 */
    private float courseAngle;

    /* 飞行速度 */
    private float speed;

    /* 飞行高度 */
    private float height;

    /* 顶点坐标列表 */
    private List<MappingVertexPoint> vertexList = new ArrayList<>();

    /* 测绘任务类型 */
    private MissionType missionType = MissionType.MISSION_TYPE_MAPPING_POLYGON;

    /* 主航线角度是否由用户来定义，用于生成航线路径时使用 */
    private boolean autoDefineAngle;

    /* 测绘中生成的白色航线路径，只用于获取高层数据使用，不需要保存到数据库 */
    private List<MappingVertexPoint> whiteLatLngList = new ArrayList<>();

    /* 矩形的旋转角度，用于恢复 UI，保存此值是为了解决旋转矩形后又调整了航线角度无法还原的情形 */
    private float rectAngle;

    /* 云台俯仰角 */
    private float gimbalPitch;

    /* 基准面海拔高度 */
    private float baseAlt;

    private float POI8Dir;//八字盘旋参考航向[-180°,180°]
    private float POI8Dis;//八字盘旋参考距离，单位m，[205,10000]

    public MappingMissionModel() {
        height = MissionConstant.DEFAULT_MAPPING_FLY_HEIGHT;
        speed = MissionConstant.DEFAULT_MAPPING_FLY_SPEED;
        groundResolution = MissionConstant.DEFAULT_MAPPING_GROUND_RESOLUTION;
        courseRate = MissionConstant.DEFAULT_MAPPING_COURSE_OVERLAP_RATE;
        sideRate = MissionConstant.DEFAULT_MAPPING_SIDE_OVERLAP_RATE;
        courseAngle = MissionConstant.DEFAULT_MAPPING_COURSE_ANGLE;
        gimbalPitch = MissionConstant.DEFAULT_WAYPOINT_GIMBAL_PITCH;
        autoDefineAngle = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getGroundResolution() {
        return groundResolution;
    }

    public void setGroundResolution(float groundResolution) {
        this.groundResolution = groundResolution;
    }

    public float getCourseRate() {
        return courseRate;
    }

    public void setCourseRate(float courseRate) {
        this.courseRate = courseRate;
    }

    public float getSideRate() {
        return sideRate;
    }

    public void setSideRate(float sideRate) {
        this.sideRate = sideRate;
    }

    public float getCourseAngle() {
        return courseAngle;
    }

    public void setCourseAngle(float courseAngle) {
        this.courseAngle = courseAngle;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public List<MappingVertexPoint> getVertexList() {
        return vertexList;
    }

    public void setVertexList(List<MappingVertexPoint> vertexList) {
        this.vertexList = vertexList;
    }

    public MissionType getMissionType() {
        return missionType;
    }

    public void setMissionType(MissionType missionType) {
        this.missionType = missionType;
    }

    public boolean isAutoDefineAngle() {
        return autoDefineAngle;
    }

    public void setAutoDefineAngle(boolean autoDefineAngle) {
        this.autoDefineAngle = autoDefineAngle;
    }

    public List<MappingVertexPoint> getWhiteLatLngList() {
        return whiteLatLngList;
    }

    public void setWhiteLatLngList(List<MappingVertexPoint> whiteLatLngList) {
        this.whiteLatLngList = whiteLatLngList;
    }

    public float getRectAngle() {
        return rectAngle;
    }

    public void setRectAngle(float rectAngle) {
        this.rectAngle = rectAngle;
    }

    public float getGimbalPitch() {
        return gimbalPitch;
    }

    public void setGimbalPitch(float gimbalPitch) {
        this.gimbalPitch = gimbalPitch;
    }

    public float getBaseAlt() {
        return baseAlt;
    }

    public void setBaseAlt(float baseAlt) {
        this.baseAlt = baseAlt;
    }

    public float getPOI8Dir() {
        return POI8Dir;
    }

    public void setPOI8Dir(float POI8Dir) {
        this.POI8Dir = POI8Dir;
    }

    public float getPOI8Dis() {
        return POI8Dis;
    }

    public void setPOI8Dis(float POI8Dis) {
        this.POI8Dis = POI8Dis;
    }


    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        MappingMissionModel model = (MappingMissionModel) super.clone();
        List<MappingVertexPoint> pointList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(vertexList)) {
            for (MappingVertexPoint vertex : vertexList) {
                pointList.add((MappingVertexPoint) vertex.clone());
            }
        }
        model.setVertexList(pointList);
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MappingMissionModel)) return false;
        MappingMissionModel that = (MappingMissionModel) o;
        return Float.compare(that.getGroundResolution(), getGroundResolution()) == 0 &&
                Float.compare(that.getCourseRate(), getCourseRate()) == 0 &&
                Float.compare(that.getSideRate(), getSideRate()) == 0 &&
                Float.compare(that.getCourseAngle(), getCourseAngle()) == 0 &&
                Float.compare(that.getSpeed(), getSpeed()) == 0 &&
                Float.compare(that.getHeight(), getHeight()) == 0 &&
                isAutoDefineAngle() == that.isAutoDefineAngle() &&
                Float.compare(that.getRectAngle(), getRectAngle()) == 0 &&
                Float.compare(that.getGimbalPitch(), getGimbalPitch()) == 0 &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getVertexList(), that.getVertexList()) &&
                getMissionType() == that.getMissionType() &&
                Objects.equals(getWhiteLatLngList(), that.getWhiteLatLngList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getGroundResolution(), getCourseRate(), getSideRate(), getCourseAngle(), getSpeed(), getHeight(), getVertexList(), getMissionType(), isAutoDefineAngle(), getWhiteLatLngList(), getRectAngle(), getGimbalPitch());
    }

    @Override
    public String toString() {
        return "MappingMissionModel{" +
                "id=" + id +
                ", groundResolution=" + groundResolution +
                ", courseRate=" + courseRate +
                ", sideRate=" + sideRate +
                ", courseAngle=" + courseAngle +
                ", speed=" + speed +
                ", height=" + height +
                ", vertexList=" + vertexList +
                ", missionType=" + missionType +
                ", autoDefineAngle=" + autoDefineAngle +
                ", whiteLatLngList=" + whiteLatLngList +
                ", rectAngle=" + rectAngle +
                ", gimbalPitch=" + gimbalPitch +
                ", baseAlt=" + baseAlt +
                '}';
    }
}
