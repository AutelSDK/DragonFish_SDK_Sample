package com.autel.sdksample.dragonfish.mission.model;


import androidx.annotation.NonNull;


import com.autel.sdksample.dragonfish.mission.enums.MissionType;

import java.util.Objects;

/**
 * 任务的基类
 */
public class BaseMissionModel  {
    private Long id;

    /* Mission类型，0为航点任务，1为测绘任务.特别注意测绘中的第一个插入点和最后一个插入点是属于测绘任务的，所有其类型为1*/
    private MissionType missionType;

    /* 航点任务 */
    private WaypointMissionModel waypointMission ;

    /* 测绘任务 */
    private MappingMissionModel mappingMission;

    /* 巡检任务 */
    private InspectionMissionModel inspectionMission;

    /*上一次的巡检任务，当新的巡检任务失败后，还原到旧的巡检任务*/
    private InspectionMissionModel lastInspectionMission;

    public InspectionMissionModel getLastInspectionMission() {
        return lastInspectionMission;
    }

    public void setLastInspectionMission(InspectionMissionModel lastInspectionMission) {
        this.lastInspectionMission = lastInspectionMission;
    }

    public InspectionMissionModel getInspectionMission() {
        return inspectionMission;
    }

    public void setInspectionMission(InspectionMissionModel inspectionMission) {
        this.inspectionMission = inspectionMission;
    }

    public void resetInspectionMission() {
        if (null != inspectionMission) {
            InspectionMissionModel newMission = new InspectionMissionModel();
            newMission.setInspectionList(inspectionMission.getInspectionList());
            newMission.setPlan_sample_lla(inspectionMission.getPlan_sample_lla());
            newMission.setSample_waypointId(inspectionMission.getSample_waypointId());
            newMission.setWpInfo(inspectionMission.getWpInfo());
            setInspectionMission(newMission);
        }
    }

    public MissionType getMissionType() {
        return missionType;
    }

    public void setMissionType(MissionType missionType) {
        this.missionType = missionType;
    }

    public WaypointMissionModel getWaypointMission() {
        return waypointMission;
    }

    public void setWaypointMission(WaypointMissionModel waypointMission) {
        this.waypointMission = waypointMission;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MappingMissionModel getMappingMission() {
        return mappingMission;
    }

    public void setMappingMission(MappingMissionModel mappingMission) {
        this.mappingMission = mappingMission;
    }


    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        BaseMissionModel model = (BaseMissionModel) super.clone();
        if (waypointMission != null) {
            model.setWaypointMission((WaypointMissionModel) waypointMission.clone());
        }
        if (mappingMission != null) {
            model.setMappingMission((MappingMissionModel) mappingMission.clone());
        }
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseMissionModel)) return false;
        BaseMissionModel model = (BaseMissionModel) o;
        return missionType == model.missionType &&
                Objects.equals(id, model.id) &&
                Objects.equals(waypointMission, model.waypointMission) &&
                Objects.equals(mappingMission, model.mappingMission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, missionType, waypointMission, mappingMission);
    }

    @Override
    public String toString() {
        return "BaseMissionModel{" +
                "id=" + id +
                ", missionType=" + missionType +
                ", waypointMission=" + waypointMission +
                ", mappingMission=" + mappingMission +
                ", inspectionMission=" + inspectionMission +
                ", lastInspectionMission=" + lastInspectionMission +
                '}';
    }
}
