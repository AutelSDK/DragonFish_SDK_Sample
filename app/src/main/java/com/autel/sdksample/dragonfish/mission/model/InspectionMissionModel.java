package com.autel.sdksample.dragonfish.mission.model;


import androidx.annotation.NonNull;

import com.autel.lib.enums.MissionConstant;
import com.autel.lib.jniHelper.Coordinate3D;
import com.autel.lib.jniHelper.WPInfo;
import com.autel.lib.jniHelper.WPTag;
import com.autel.sdksample.dragonfish.mission.enums.WaypointType;
import com.autel.sdksample.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author peiguo.chen
 * @date 2022/5/20
 * 巡检任务
 */
public class InspectionMissionModel{
    public static final float MAX_CLIMB_V = 2;//最大爬升速度
    public static final float MAX_DIVE_V = 2;//最大下降速度

    private Long id;

    /* 航点列表 杆塔坐标 */
    private List<InspectionModel> inspectionList = new ArrayList<>();

    /*目标高度 杆塔高度*/
    private int targetHeight = MissionConstant.DEFAULT_TOWER_HEIGHT;

    /*航线条数*/
    private int routeLineNum = MissionConstant.INSPECTION_DEFAULT_ROUTE_LINES;

    /*航带宽度*/
    private int airBeltWidth = MissionConstant.DEFAULT_STRIP_WIDTH;

    /*偏移距离*/
    private int offsetDistance = MissionConstant.DEFAULT_OFFSET_DISTANCE;

    /*参考观察高度  参考点到飞机的相对高度*/
    private int referenceHeight = MissionConstant.DEFAULT_REFERENCE_INSPECTION_HEIGHT;

    /*幅宽 需要观察的带状区域*/
    private int observeWidth = MissionConstant.DEFAULT_INSPECTION_WIDTH;

    /*巡检类型*/
    private int inspectionType = 1;//1 普通巡检，2 精细化巡检

    /*真高限制*/
    private int trueHeight = MissionConstant.INSPECTION_DEFAULT_TRUE_HEIGHT;

    private int speed = MissionConstant.DEFAULT_FLY_SPEED;//目前是批量设置

    /*标记点坐标*/
    private ArrayList<WPInfo> wpInfo;

    /*校正后的标记点坐标*/
    private ArrayList<WPTag> wpTags;

    /*采样点 非地形高度*/
    private final ArrayList<Coordinate3D> plan_sample_lla = new ArrayList<>();

    /*采样点 地形高度*/
    private final ArrayList<Coordinate3D> terrain_plan_sample_lla2 = new ArrayList<>();

    /*缓存采样id*/
    private short[] sample_waypointId;

    public ArrayList<WPTag> getWpTags() {
        return wpTags;
    }

    public void setWpTags(ArrayList<WPTag> wpTags) {
        this.wpTags = wpTags;
    }

    public void setSample_waypointId(short[] sample_waypointId) {
        this.sample_waypointId = sample_waypointId;
    }

    public short[] getSample_waypointId() {
        return sample_waypointId;
    }

    public ArrayList<Coordinate3D> getPlan_sample_lla() {
        return plan_sample_lla;
    }

    public void setPlan_sample_lla(ArrayList<Coordinate3D> plan_sample_lla) {
        this.plan_sample_lla.clear();
        if (plan_sample_lla != null) {
            this.plan_sample_lla.addAll(plan_sample_lla);
        }
    }

    public ArrayList<Coordinate3D> getTerrain_plan_sample_lla2() {
        return terrain_plan_sample_lla2;
    }

    public void setTerrain_plan_sample_lla2(ArrayList<Coordinate3D> terrain_plan_sample_lla2) {
        this.terrain_plan_sample_lla2.clear();
        if (terrain_plan_sample_lla2 != null) {
            this.terrain_plan_sample_lla2 .addAll(terrain_plan_sample_lla2);
        }
    }

    /*获取带地形高度的采样点*/
    public ArrayList<Coordinate3D> getTerrain_sample_lla() {
        ArrayList<Coordinate3D> result = new ArrayList<>();
        for (Coordinate3D coordinate : plan_sample_lla) {
            Coordinate3D coordinate3D = new Coordinate3D(coordinate.getLatitude(), coordinate.getLongitude(), coordinate.getAltitude());
            int altitude = 0;// ElevationManager.getInstance().getElevation(coordinate.getLatitude(), coordinate.getLongitude());
            coordinate3D.setAltitude(altitude);
            result.add(coordinate3D);
        }
        return result;
    }

    public ArrayList<WPInfo> getWpInfo() {
        return wpInfo;
    }

    public void setWpInfo(ArrayList<WPInfo> wpInfo) {
        this.wpInfo = wpInfo;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /*是否启用dem*/
    private boolean isUseDem = true;

    public boolean isUseDem() {
        return isUseDem;
    }

    public void setUseDem(boolean useDem) {
        isUseDem = useDem;
    }

    public int getTargetHeight() {
        return targetHeight;
    }

    public void setTargetHeight(int targetHeight) {
        this.targetHeight = targetHeight;
    }

    public int getRouteLineNum() {
        return routeLineNum;
    }

    public void setRouteLineNum(int routeLineNum) {
        this.routeLineNum = routeLineNum;
    }

    public int getAirBeltWidth() {
        return airBeltWidth;
    }

    public void setAirBeltWidth(int airBeltWidth) {
        this.airBeltWidth = airBeltWidth;
    }

    public int getOffsetDistance() {
        return offsetDistance;
    }

    public void setOffsetDistance(int offsetDistance) {
        this.offsetDistance = offsetDistance;
    }

    public int getReferenceHeight() {
        return referenceHeight;
    }

    public void setReferenceHeight(int referenceHeight) {
        this.referenceHeight = referenceHeight;
    }

    public int getObserveWidth() {
        return observeWidth;
    }

    public void setObserveWidth(int observeWidth) {
        this.observeWidth = observeWidth;
    }

    public int getInspectionType() {
        return inspectionType;
    }

    public void setInspectionType(int inspectionType) {
        this.inspectionType = inspectionType;
    }

    public int getTrueHeight() {
        return trueHeight;
    }

    public void setTrueHeight(int trueHeight) {
        this.trueHeight = trueHeight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<InspectionModel> getInspectionList() {
        return inspectionList;
    }

    public void setInspectionList(List<InspectionModel> inspectionList) {
        this.inspectionList = inspectionList;
    }

    /**
     * 获取源数据
     *
     * @return
     */
    public ArrayList<Coordinate3D> getSourceList() {
        ArrayList<Coordinate3D> result = new ArrayList<>();
        for (InspectionModel model : inspectionList) {
            int altitude = 0;//ElevationManager.getInstance().getElevation(model.getLatitude(), model.getLongitude());
            result.add(new Coordinate3D(model.getLatitude(), model.getLongitude(), altitude + targetHeight));
        }
        return result;
    }

    /**
     * 获取飞机相对杆塔高度数组
     *
     * @return
     */
    public float[] getFlyAbove() {
        float[] result = new float[inspectionList.size()];
        Arrays.fill(result, referenceHeight);
        return result;
    }


    public int getInspectionSize() {
        int length = 0;
        if (CollectionUtil.isNotEmpty(inspectionList)) {
            for (InspectionModel waypoint : inspectionList) {
                if (waypoint.getWaypointType() == WaypointType.INSPECTION) {
                    length++;
                }
            }
        }
        return length;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        InspectionMissionModel missionModel = (InspectionMissionModel) super.clone();
        List<InspectionModel> inspectionModels = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(inspectionList)) {
            for (InspectionModel model : inspectionList) {
                inspectionModels.add(model.clone());
            }
        }
        missionModel.setInspectionList(inspectionModels);
        return missionModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InspectionMissionModel)) return false;
        InspectionMissionModel that = (InspectionMissionModel) o;
        return that.getTargetHeight() == getTargetHeight() &&
                that.getRouteLineNum() == getRouteLineNum() &&
                that.getAirBeltWidth() == getAirBeltWidth() &&
                that.getOffsetDistance() == getOffsetDistance() &&
                that.getReferenceHeight() == getReferenceHeight() &&
                that.getObserveWidth() == getObserveWidth() &&
                that.getInspectionType() == getInspectionType() &&
                that.getTrueHeight() == getTrueHeight() &&
                Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getInspectionList(), getTargetHeight(),
                getRouteLineNum(), getAirBeltWidth(), getOffsetDistance(),
                getReferenceHeight(), getObserveWidth(), getInspectionType(), getTrueHeight());
    }

    @Override
    public String toString() {
        return "InspectionMissionModel{" +
                "id=" + id +
                ", inspectionList=" + inspectionList +
                ", targetHeight=" + targetHeight +
                ", routeLineNum=" + routeLineNum +
                ", airBeltWidth=" + airBeltWidth +
                ", offsetDistance=" + offsetDistance +
                ", referenceHeight=" + referenceHeight +
                ", observeWidth=" + observeWidth +
                ", inspectionType=" + inspectionType +
                ", trueHeight=" + trueHeight +
                ", speed=" + speed +
                ", wpInfo=" + wpInfo +
                ", isUseDem=" + isUseDem +
                '}';
    }
}
