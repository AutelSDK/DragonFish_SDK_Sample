package com.autel.sdksample.dragonfish.mission.model;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.autel.AutelNet2.camera.enums.CameraType;
import com.autel.common.mission.base.DistanceModel;
import com.autel.common.mission.evo.RemoteControlLostSignalAction;
import com.autel.lib.enums.MissionConstant;
import com.autel.lib.enums.PlanningType;
import com.autel.sdksample.base.mission.AutelLatLng;
import com.autel.sdksample.dragonfish.mission.enums.MissionAltitudeType;
import com.autel.sdksample.dragonfish.mission.enums.MissionFinishActionType;
import com.autel.sdksample.dragonfish.mission.enums.MissionType;
import com.autel.sdksample.utils.CollectionUtil;
import com.autel.util.log.AutelLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 整条的航线任务信息
 */
public class TaskModel  {
    private Long id;

    /* 任务唯一标识 */
    private int uuid;

    private int currentTaskIndex = -1;
    private int currentWaypointIndex = -1;

    /*任务列表*/
    private List<BaseMissionModel> taskList = new ArrayList<>();

    /*返航点信息*/
    private HomePointModel homePoint = new HomePointModel();

    /*上一次的返航点信息*/
    private HomePointModel lastHomePoint;

    /* 相机类型 */
    private CameraType cameraType = CameraType.UNKNOWN;

    /*任务完成动作*/
    private MissionFinishActionType finishActionType = MissionFinishActionType.HOVER;

    /*任务失联动作*/
    private RemoteControlLostSignalAction lostAction = RemoteControlLostSignalAction.RETURN_HOME;

    /*地形跟随*/
    private boolean enableTopographyFollow = false;

    /*Task的简单信息，用于列表展示*/
    private SummaryTaskInfo summaryTaskInfo = new SummaryTaskInfo();

    /* 航线中断的 index，用于断点续飞 */
    private int pauseIndex = -1;

    /* 执行任务时的飞机坐标 */
    private Coordinate3DModel droneLocation = new Coordinate3DModel();

    /* 生成的航线每个点,用于地图调整级别，不需要存到数据库 */
    private List<AutelLatLng> routePointList = new ArrayList<>();

    private List<DistanceModel> distanceList = new ArrayList<>();

    /* 垂直起飞高度 起飞模态切换高度*/
    private float takeoffHeight = MissionConstant.DEFAULT_MISSION_TAKEOFF_HEIGHT;

    /* 高度类型 */
    private MissionAltitudeType heightType = MissionAltitudeType.RELATIVE;

    /* 是否为 kml 文件 */
    private boolean isKml = false;

    /* 上升盘旋是否为跟随航点1 */
    private boolean isFollowWaypointAltitude = true;

    /*重试次数 1 可重试，0 不可重试 */
    private int retryCount = 1;

    /* 航线生成输入基本参数*/
    //航线循环方式：-1: 无限循环；0: 不循环；n: 循环n次；
    private int cycleMode = 0;
    //航线循环起始子任务
    private int startSubID = 1;
    //航线循环起始航点
    private float startWPID = 1;
    //航线循环结束子任务
    private int endSubID = 1;
    //航线循环结束航点
    private float endWPID = 1;

    /* 安全高度 */
    private float safeHeight = MissionConstant.DEFAULT_SAFE_HEIGHT;

    private PlanningType planningType = PlanningType.NORMAL_MISSIONS;

    private int area = 0;//地面规划上传执行任务的面积
    private int waypointNum = 0;//地面规划上传执行任务的航点个数

    public PlanningType getPlanningType() {
        return planningType;
    }

    public void setPlanningType(PlanningType planningType) {
        this.planningType = planningType;
    }

    public boolean isAirMission() {
        return planningType == PlanningType.TEMP_MISSIONS;
    }

    public void setAirMission(boolean airMission) {
        if (airMission) {
            planningType = PlanningType.TEMP_MISSIONS;
        } else {
            planningType = PlanningType.NORMAL_MISSIONS;
        }
    }

    public HomePointModel getLastHomePoint() {
        return lastHomePoint;
    }

    public void setLastHomePoint(HomePointModel lastHomePoint) {
        this.lastHomePoint = lastHomePoint;
    }

    public float getSafeHeight() {
        return safeHeight;
    }

    public void setSafeHeight(float safeHeight) {
        this.safeHeight = safeHeight;
    }

    public int getCycleMode() {
        return cycleMode;
    }

    public void setCycleMode(int cycleMode) {
        this.cycleMode = cycleMode;
    }

    public boolean isFollowWaypointAltitude() {
        return isFollowWaypointAltitude;
    }

    public void setFollowWaypointAltitude(boolean followWaypointAltitude) {
        isFollowWaypointAltitude = followWaypointAltitude;
    }

    public int getStartSubID() {
        return startSubID;
    }

    public void setStartSubID(int startSubID) {
        this.startSubID = startSubID;
    }

    public float getStartWPID() {
        return startWPID;
    }

    public void setStartWPID(float startWPID) {
        this.startWPID = startWPID;
    }

    public int getEndSubID() {
        return endSubID;
    }

    public void setEndSubID(int endSubID) {
        this.endSubID = endSubID;
    }

    public float getEndWPID() {
        return endWPID;
    }

    public void setEndWPID(float endWPID) {
        this.endWPID = endWPID;
    }


    public int getRetryCount() {
        return retryCount;
    }

    public boolean canRetry() {
        return retryCount == 1;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public int getCurrentTaskIndex() {
        return currentTaskIndex;
    }

    public void setCurrentTaskIndex(int currentTaskIndex) {
        this.currentTaskIndex = currentTaskIndex;
    }

    public int getCurrentWaypointIndex() {
        return currentWaypointIndex;
    }

    public void setCurrentWaypointIndex(int currentWaypointIndex) {
        this.currentWaypointIndex = currentWaypointIndex;

    }

    public List<BaseMissionModel> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<BaseMissionModel> taskList) {
        this.taskList = taskList;
    }

    public HomePointModel getHomePoint() {
        return homePoint;
    }

    public void setHomePoint(HomePointModel homePoint) {
        this.homePoint = homePoint;
    }

    public CameraType getCameraType() {
        return cameraType;
    }

    public void setCameraType(CameraType cameraType) {
        this.cameraType = cameraType;
    }

    public MissionFinishActionType getFinishActionType() {
        return finishActionType;
    }

    public void setFinishActionType(MissionFinishActionType finishActionType) {
        this.finishActionType = finishActionType;
    }

    public boolean isEnableTopographyFollow() {
        return enableTopographyFollow;
    }

    public void setEnableTopographyFollow(boolean enableTopographyFollow) {
        this.enableTopographyFollow = enableTopographyFollow;
    }

    public SummaryTaskInfo getSummaryTaskInfo() {
        return summaryTaskInfo;
    }

    public void setSummaryTaskInfo(SummaryTaskInfo summaryTaskInfo) {
        this.summaryTaskInfo = summaryTaskInfo;
    }

    public int getPauseIndex() {
        return pauseIndex;
    }

    public void setPauseIndex(int pauseIndex) {
        this.pauseIndex = pauseIndex;
    }

    public Coordinate3DModel getDroneLocation() {
        return droneLocation;
    }

    public void setDroneLocation(Coordinate3DModel droneLocation) {
        this.droneLocation = droneLocation;
    }

    public List<AutelLatLng> getRoutePointList() {
        return routePointList;
    }

    public void setRoutePointList(List<AutelLatLng> routePointList) {
        this.routePointList = routePointList;
    }

    public List<DistanceModel> getDistanceList() {
        return distanceList;
    }

    public void setDistanceList(List<DistanceModel> distanceList) {
        this.distanceList = distanceList;
    }


    public float getTakeoffHeight() {
        return takeoffHeight;
    }

    public void setTakeoffHeight(float takeoffHeight) {
        this.takeoffHeight = takeoffHeight;
    }

    public MissionAltitudeType getHeightType() {
        return heightType;
    }

    public void setHeightType(MissionAltitudeType heightType) {
        this.heightType = heightType;
    }

    public RemoteControlLostSignalAction getLostAction() {
        return lostAction;
    }

    public void setLostAction(RemoteControlLostSignalAction lostAction) {
        this.lostAction = lostAction;
    }


    public WaypointMissionModel getWaypointMission() {
        if (CollectionUtil.isEmpty(taskList)) return null;
        for (BaseMissionModel model : taskList) {
            if (model.getWaypointMission() != null) {
                return model.getWaypointMission();
            }
        }
        return null;
    }

    public MappingMissionModel getMappingMission() {
        if (CollectionUtil.isEmpty(taskList)) return null;
        for (BaseMissionModel model : taskList) {
            if (model.getMappingMission() != null) {
                return model.getMappingMission();
            }
        }
        return null;
    }

    /**
     * 获取当前的巡检任务对象
     */
    public InspectionMissionModel getInspectionMission() {
        if (summaryTaskInfo.getMissionType() == MissionType.MISSION_TYPE_MAPPING_INSPECTION) {
            if (CollectionUtil.isEmpty(taskList)) return null;
            for (BaseMissionModel model : taskList) {
                if (model.getInspectionMission() != null) {
                    return model.getInspectionMission();
                }
            }
        }
        return null;
    }

    /**
     * 获取上一次缓存的巡检任务对象
     */
    public InspectionMissionModel getLastInspectionMission() {
        if (summaryTaskInfo.getMissionType() == MissionType.MISSION_TYPE_MAPPING_INSPECTION) {
            if (CollectionUtil.isEmpty(taskList)) return null;
            for (BaseMissionModel model : taskList) {
                return model.getLastInspectionMission();
            }
        }
        return null;
    }

    /**
     * 保存当前成功的巡检任务
     */
    public void setLastInspectionMission() {
        if (summaryTaskInfo.getMissionType() == MissionType.MISSION_TYPE_MAPPING_INSPECTION) {
            if (CollectionUtil.isEmpty(taskList)) return;
            try {
                for (BaseMissionModel model : taskList) {
                    InspectionMissionModel missionModel = model.getInspectionMission();
                    if (missionModel != null) {
                        // 需要克隆一个对象，不然新旧对象的属性会被一起修改掉了
                        model.setLastInspectionMission((InspectionMissionModel) missionModel.clone());
                    }
                }
                lastHomePoint = (HomePointModel) homePoint.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 重置为通道巡检任务
     */
    public void resetInspectionMission() {
        BaseMissionModel baseMissionModel = new BaseMissionModel();
        InspectionMissionModel inspectionMissionModel = new InspectionMissionModel();
        baseMissionModel.setMissionType(MissionType.MISSION_TYPE_MAPPING_INSPECTION);
        baseMissionModel.setInspectionMission(inspectionMissionModel);
        for (BaseMissionModel model : taskList) {
            InspectionMissionModel lastInspectionMission = model.getLastInspectionMission();
            if (lastInspectionMission != null) {
                baseMissionModel.setLastInspectionMission(lastInspectionMission);
            }
        }
        taskList.clear();
        taskList.add(baseMissionModel);
    }


    public boolean isNullTask() {
        if (TextUtils.isEmpty(summaryTaskInfo.getName())) {//任务名称为空,则认为是空任务
            AutelLog.e("TaskModel", "isNullTask:任务名称为空,则认为是空任务");
            return true;
        }
        if (summaryTaskInfo.getMissionType() == MissionType.MISSION_TYPE_WAYPOINT) {
            return getWaypointMission() == null || CollectionUtil.isEmpty(getWaypointMission().getWaypointList());
        } else if (summaryTaskInfo.getMissionType() == MissionType.MISSION_TYPE_MAPPING_POLYGON) {
            return getMappingMission() == null || CollectionUtil.isEmpty(getMappingMission().getVertexList());
        } else if (summaryTaskInfo.getMissionType() == MissionType.MISSION_TYPE_MAPPING_INSPECTION) {
            return getInspectionMission() == null || CollectionUtil.isEmpty(getInspectionMission().getInspectionList());
        } else if (summaryTaskInfo.getMissionType() == MissionType.MISSION_TYPE_QUICK_8) {
            return getWaypointMission() == null || CollectionUtil.isEmpty(getWaypointMission().getWaypointList());
        }
        return true;
    }

    @NonNull
    @Override
    public TaskModel clone() {
        try {
            TaskModel model = (TaskModel) super.clone();
            if (homePoint != null) {
                model.setHomePoint((HomePointModel) homePoint.clone());
            }
            if (summaryTaskInfo != null) {
                model.setSummaryTaskInfo((SummaryTaskInfo) summaryTaskInfo.clone());
            }
            if (droneLocation != null) {
                model.setDroneLocation((Coordinate3DModel) droneLocation.clone());
            }
            List<BaseMissionModel> baseMissionModelList = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(taskList)) {
                for (BaseMissionModel missionModel : taskList) {
                    baseMissionModelList.add((BaseMissionModel) missionModel.clone());
                }
            }
            model.setTaskList(baseMissionModelList);

            return model;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isKml() {
        return isKml;
    }

    public void setKml(boolean kml) {
        isKml = kml;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskModel)) return false;
        TaskModel taskModel = (TaskModel) o;
        return getUuid() == taskModel.getUuid() &&
                getCurrentTaskIndex() == taskModel.getCurrentTaskIndex() &&
                getCurrentWaypointIndex() == taskModel.getCurrentWaypointIndex() &&
                isEnableTopographyFollow() == taskModel.isEnableTopographyFollow() &&
                getPauseIndex() == taskModel.getPauseIndex() &&
                getTakeoffHeight() == taskModel.getTakeoffHeight() &&
                Objects.equals(getId(), taskModel.getId()) &&
                Objects.equals(getTaskList(), taskModel.getTaskList()) &&
                Objects.equals(getHomePoint(), taskModel.getHomePoint()) &&
                getCameraType() == taskModel.getCameraType() &&
                getFinishActionType() == taskModel.getFinishActionType() &&
                Objects.equals(getSummaryTaskInfo(), taskModel.getSummaryTaskInfo()) &&
                Objects.equals(getDroneLocation(), taskModel.getDroneLocation()) &&
                Objects.equals(getRoutePointList(), taskModel.getRoutePointList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUuid(), getCurrentTaskIndex(), getCurrentWaypointIndex(), getTaskList(), getHomePoint(), getCameraType(), getFinishActionType(), isEnableTopographyFollow(), getSummaryTaskInfo(), getPauseIndex(), getDroneLocation(), getRoutePointList(), getTakeoffHeight());
    }

    /**
     * 克隆一个新的对象，深拷贝
     *
     * @return 新对象
     */
    public TaskModel convertNewTaskModel() {
        return this.clone();
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getWaypointNum() {
        return waypointNum;
    }

    public void setWaypointNum(int waypointNum) {
        this.waypointNum = waypointNum;
    }

    @Override
    public String toString() {
        return "TaskModel{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", currentTaskIndex=" + currentTaskIndex +
                ", currentWaypointIndex=" + currentWaypointIndex +
                ", taskList=" + taskList +
                ", homePoint=" + homePoint +
                ", lastHomePoint=" + lastHomePoint +
                ", cameraType=" + cameraType +
                ", finishActionType=" + finishActionType +
                ", lostAction=" + lostAction +
                ", enableTopographyFollow=" + enableTopographyFollow +
                ", summaryTaskInfo=" + summaryTaskInfo +
                ", pauseIndex=" + pauseIndex +
                ", droneLocation=" + droneLocation +
                ", distanceList=" + distanceList +
                ", takeoffHeight=" + takeoffHeight +
                ", heightType=" + heightType +
                ", isKml=" + isKml +
                ", isFollowWaypointAltitude=" + isFollowWaypointAltitude +
                ", retryCount=" + retryCount +
                ", cycleMode=" + cycleMode +
                ", startSubID=" + startSubID +
                ", startWPID=" + startWPID +
                ", endSubID=" + endSubID +
                ", endWPID=" + endWPID +
                ", safeHeight=" + safeHeight +
                ", planningType=" + planningType +
                '}';
    }
}
