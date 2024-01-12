package com.autel.sdksample.utils;

import static com.autel.lib.enums.MissionConstant.LANDING;
import static com.autel.lib.enums.MissionConstant.MAX_SPEED;
import static com.autel.lib.enums.MissionConstant.MIN_HOVER_RADIUS;
import static com.autel.lib.enums.MissionConstant.MISSION;

import com.autel.AutelNet2.aircraft.mission.enmus.LocationStatus;
import com.autel.common.mission.AutelCoordinate3D;
import com.autel.common.mission.cruiser.OnlinePlanningInfo;
import com.autel.common.mission.evo.RemoteControlLostSignalAction;
import com.autel.internal.sdk.AutelBaseApplication;
import com.autel.internal.sdk.util.AutelSharedPreferencesUtils;
import com.autel.lib.enums.MissionConstant;
import com.autel.lib.jniHelper.CodeType;
import com.autel.lib.jniHelper.ErrorCode;
import com.autel.lib.jniHelper.ForceLandInfo;
import com.autel.lib.jniHelper.InterestArea;
import com.autel.lib.jniHelper.IntroInfo;
import com.autel.lib.jniHelper.LandInfo;
import com.autel.lib.jniHelper.LaunchInfo;
import com.autel.lib.jniHelper.MissionInfo;
import com.autel.lib.jniHelper.NativeHelper;
import com.autel.lib.jniHelper.PathPlanningParameter;
import com.autel.lib.jniHelper.PathPlanningResult;
import com.autel.lib.jniHelper.PrimalScanPlanResult;
import com.autel.lib.jniHelper.SubMissionInfo;
import com.autel.lib.jniHelper.TerrainAvoidPlanResult;
import com.autel.lib.jniHelper.WPInfo;
import com.autel.sdksample.dragonfish.listener.PathPlanningListener;
import com.autel.sdksample.dragonfish.mission.enums.MissionAltitudeType;
import com.autel.sdksample.dragonfish.mission.enums.MissionType;
import com.autel.sdksample.dragonfish.mission.model.Coordinate3DModel;
import com.autel.sdksample.dragonfish.mission.model.HomePointModel;
import com.autel.sdksample.dragonfish.mission.model.InspectionMissionModel;
import com.autel.sdksample.dragonfish.mission.model.MappingMissionModel;
import com.autel.sdksample.dragonfish.mission.model.MappingVertexPoint;
import com.autel.sdksample.dragonfish.mission.model.POIPoint;
import com.autel.sdksample.dragonfish.mission.model.QuickTaskPoint;
import com.autel.sdksample.dragonfish.mission.model.TaskModel;
import com.autel.sdksample.dragonfish.mission.model.WaypointModel;
import com.autel.util.log.AutelLog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p> 功能描述：航线 工具类<br>
 * <p>
 * 详细描述：
 *
 * @UpdateUser: gss
 * @UpdateDate: 2021/12/30
 * @UpdateRemark: 优化警告问题
 * @Version: 1.0 版本
 */
public class RealTimePathPlaningUtils {

    private static final String TAG = "RealTimePathPlaningUtils";

    /**
     * 航线任务算法库（适用于航点任务，多边形任务，临时备降点任务，临时返航任务）
     */
    public static PathPlanningResult getMissionPath(String missionJson) {
        AutelLog.debug_i(TAG, "getMissionPath -> start");
        PathPlanningParameter pathPlanningParameter = new Gson().fromJson(missionJson, PathPlanningParameter.class);//getPathPlanningParameter(taskModel, droneLocation, subMissionInfo);
        NativeHelper.setPathPlanningParameter(pathPlanningParameter);
        PathPlanningResult pathPlanningResult = NativeHelper.getPathPlanning(pathPlanningParameter);
        AutelLog.debug_i(TAG, "getMissionPath -> end");
        return pathPlanningResult;
    }


    /**
     * 生成航线任务参数对象
     *
     * @param taskModel      任务对象
     * @param droneLocation  飞机位置
     * @param subMissionInfo 子任务信息
     * @return 航线规划参数对象
     */
    public static PathPlanningParameter getPathPlanningParameter(TaskModel taskModel, AutelCoordinate3D droneLocation,
                                                                 SubMissionInfo subMissionInfo) {
        if (null == taskModel) {
            return new PathPlanningParameter(ErrorCode.CODE_90001);
        }
        if (subMissionInfo == null) {
            subMissionInfo = new SubMissionInfo();
        }

        double[] drone;
        if (droneLocation != null) {
            drone = getDronePoint(droneLocation);
        } else {
            drone = getHomePoint(taskModel.getHomePoint());
        }
        HomePointModel homePointModel = taskModel.getHomePoint();
        IntroInfo introInfo = new IntroInfo();
        introInfo.setInitLat(drone[0]);
        introInfo.setInitLon(drone[1]);
        introInfo.setInitAlt((float) drone[2]);
        introInfo.setForceLandNum(homePointModel.getForceLandingPoints().size());
        introInfo.setMinRadius(MIN_HOVER_RADIUS);
        introInfo.setMaxVz(MAX_SPEED);

        //飞机速度
        float airVel = 20;
        ArrayList<WPInfo> wpInfo = new ArrayList<>();
        //观察点
        ArrayList<InterestArea> interestAreaArrayList = new ArrayList<>();

        int WPClimbMode = 1;//爬升模式 1：正常 2：快速

        short waypointIndex = 0;
        if (null != taskModel.getWaypointMission()) {
            airVel = taskModel.getWaypointMission().getWaylineSpeed();
            int WPType = 4;//航点类型 4：普通航点 5：测绘角点
            List<WaypointModel> waypointModelList = taskModel.getWaypointMission().getWaypointList();
            if (taskModel.isFollowWaypointAltitude()) {
                if (waypointModelList.size() > 0) {
                    homePointModel.setUpHoverHeight(waypointModelList.get(0).getHeight());
                    homePointModel.setUpHoverAltitudeType(waypointModelList.get(0).getMissionAltitudeType());
                }
            }
            for (WaypointModel waypoint : waypointModelList) {
                waypointIndex++;
                WPInfo wpInfo1 = new WPInfo();
                wpInfo1.setWpIndex(waypointIndex);
                wpInfo1.setWpLat(waypoint.getLatitude());
                wpInfo1.setWpLon(waypoint.getLongitude());
                wpInfo1.setWpAlt(waypoint.getHeight());
                wpInfo1.setWpAltType(waypoint.getMissionAltitudeType().getValue());
                wpInfo1.setWpType(WPType);
                wpInfo1.setWpVel(waypoint.getSpeed());
                wpInfo1.setGimbalYaw(waypoint.getGimbalYaw());
                wpInfo1.setGimbalPitch(waypoint.getGimbalPitch());
                wpInfo1.setWpRadius(waypoint.getHoverRadius());
                wpInfo1.setWpTurnMode(waypoint.getMissionActionType().getValue());
                wpInfo1.setWpTurnParam1(waypoint.getHoverCylinderNumber());
                wpInfo1.setWpClimbMode(WPClimbMode);
                wpInfo1.setPayloadAction(waypoint.getCameraActionType().getValue());
                wpInfo1.setActionParam1(waypoint.getCameraParam());
                wpInfo1.setWpReserved1(waypoint.getWpReserved1());
                wpInfo.add(wpInfo1);
            }

            List<POIPoint> poiPoints = taskModel.getWaypointMission().getPoiPointList();
            for (POIPoint poiPoint : poiPoints) {
                InterestArea interestArea1 = new InterestArea();
                interestArea1.setIp_lat_deg(poiPoint.getLatitude());
                interestArea1.setIp_lon_deg(poiPoint.getLongitude());
//                interestArea1.setIP_Alt_m(poiPoint.getAltitude());
                interestArea1.setIp_alt_m((float) (poiPoint.getHeight()));
                interestArea1.setIp_radius_m(poiPoint.getRadius());
                interestArea1.setIp_type(poiPoint.getLinkPointIdList().isEmpty() ? 0 : 1);
                interestArea1.setValid_cpt_num(poiPoint.getLinkPointIdList().size());
                interestArea1.setIp_connectPt(MissionSaveUtils.getSortPoiLinkPoints(poiPoint));
                interestAreaArrayList.add(interestArea1);
            }

            introInfo.setCycleMode(taskModel.getCycleMode());
            if (introInfo.getCycleMode() != 0) {
                introInfo.setStartSubID(taskModel.getStartSubID());
                introInfo.setEndSubID(taskModel.getEndSubID());
                introInfo.setStartWPID(taskModel.getStartWPID());
                introInfo.setEndWPID(taskModel.getEndWPID());
            }
        }
        if (null != taskModel.getMappingMission()) {
            MappingMissionModel mappingMission = taskModel.getMappingMission();
            airVel = mappingMission.getSpeed();
            if (taskModel.isFollowWaypointAltitude()) {
                homePointModel.setUpHoverHeight(mappingMission.getHeight() + mappingMission.getBaseAlt());
                homePointModel.setUpHoverAltitudeType(MissionAltitudeType.ALTITUDE);
            }
            subMissionInfo.setAirLineDir(mappingMission.getCourseAngle());
            subMissionInfo.setBaseAlt(mappingMission.getBaseAlt());
            subMissionInfo.setResolution(mappingMission.getGroundResolution());
            subMissionInfo.setOverlapSide((int) mappingMission.getSideRate());
            subMissionInfo.setOverlapAlong((int) mappingMission.getCourseRate());

            List<MappingVertexPoint> vertexList = taskModel.getMappingMission().getVertexList();
            for (MappingVertexPoint waypoint : vertexList) {
                waypointIndex++;
                WPInfo wpInfo1 = new WPInfo();
                wpInfo1.setWpIndex(waypointIndex);
                wpInfo1.setWpLat(waypoint.getLatitude());
                wpInfo1.setWpLon(waypoint.getLongitude());
                wpInfo1.setWpAlt(waypoint.getHeight());
                wpInfo1.setWpType(5);
                wpInfo1.setWpVel(airVel);
                wpInfo1.setWpClimbMode(WPClimbMode);
                wpInfo1.setGimbalPitch(mappingMission.getGimbalPitch());
                wpInfo.add(wpInfo1);
            }
        }
        if (null != taskModel.getInspectionMission()) {
            InspectionMissionModel inspectionMission = taskModel.getInspectionMission();
            if (inspectionMission.getInspectionList() == null || inspectionMission.getInspectionList().size() == 0) {
                AutelLog.debug_i(TAG, "getPathPlanningParameter -> inspectionMission getWaypointList is null or size is 0");
                return new PathPlanningParameter(ErrorCode.CODE_90004);
            }
            airVel = inspectionMission.getSpeed();
            ArrayList<com.autel.lib.jniHelper.Coordinate3D> sourceList = inspectionMission.getSourceList();
            if (sourceList.size() == 0) {
                AutelLog.debug_i(TAG, "getPathPlanningParameter -> inspectionMission getSourceList size is 0");
                return new PathPlanningParameter(ErrorCode.CODE_90004);
            }

            PrimalScanPlanResult scanPlanResult = NativeHelper.primalScanPlan(sourceList, sourceList.get(0),
                    inspectionMission.getOffsetDistance(), (short) inspectionMission.getRouteLineNum(), inspectionMission.getAirBeltWidth(),
                    inspectionMission.getFlyAbove(), inspectionMission.getSpeed(), (short) inspectionMission.getInspectionType());
            AutelLog.debug_i(TAG, "getPathPlanningParameter -> scanPlanResult errorCode=" + scanPlanResult.getErrorCode());
            if (scanPlanResult.getErrorCode() != 0) {
                return new PathPlanningParameter(ErrorCode.find(CodeType.INSPECTION, scanPlanResult.getErrorCode()));
            }
            // 打印wpInfo列表，里面的经纬度可能为NAN
            AutelLog.debug_i(TAG, "setWpInfo -> setWpInfo=" + scanPlanResult.getWpInfo().toString());
            //缓存标记点坐标
            inspectionMission.setWpInfo(scanPlanResult.getWpInfo());
            //缓存校正标记点坐标
            inspectionMission.setWpTags(scanPlanResult.getWpTags());

            //缓存非地形采样点
            inspectionMission.setPlan_sample_lla(scanPlanResult.getPlan_sample_lla());
            //缓存采样点id
            inspectionMission.setSample_waypointId(scanPlanResult.getSample_waypointId());

            wpInfo.addAll(scanPlanResult.getWpInfo());
            interestAreaArrayList.addAll(scanPlanResult.getInterestArea());

            //测试算法2
            if (inspectionMission.isUseDem()) {

                float safe_h = 100; //安全高度 默认100，需要重飞控参数中获取出来 替换

                TerrainAvoidPlanResult terrainAvoidPlanResult = NativeHelper.terrainAvoidPlan(sourceList, inspectionMission.getPlan_sample_lla(),
                        inspectionMission.getTerrain_sample_lla(), inspectionMission.getSample_waypointId(), sourceList.get(0), safe_h, inspectionMission.getSpeed(),
                        InspectionMissionModel.MAX_CLIMB_V, InspectionMissionModel.MAX_DIVE_V, (short) inspectionMission.getInspectionType());
                AutelLog.debug_i(TAG, "getPathPlanningParameter -> terrainAvoidPlanResult errorCode=" + terrainAvoidPlanResult.getErrorCode());
                if (terrainAvoidPlanResult.getErrorCode() == 0) {
                    wpInfo.clear();
                    interestAreaArrayList.clear();
                    wpInfo.addAll(terrainAvoidPlanResult.getWpInfo());
                    interestAreaArrayList.addAll(terrainAvoidPlanResult.getInterestAreas());

                    //缓存地形采样点
                    inspectionMission.setTerrain_plan_sample_lla2(terrainAvoidPlanResult.getPlan_sample_LLA());
                    //缓存标记点和兴趣点
                    inspectionMission.setWpInfo(terrainAvoidPlanResult.getWpInfo());
                    //缓存校正标记点坐标
                    inspectionMission.setWpTags(terrainAvoidPlanResult.getWpTags());
                }
            }
        }


        LaunchInfo launchInfo = new LaunchInfo();
        launchInfo.setLaunchLat(drone[0]);
        launchInfo.setLaunchLon(drone[1]);
        launchInfo.setLaunchAlt((int) drone[2]);

        launchInfo.setDepartureLat(homePointModel.getUpHoverLatitude());
        launchInfo.setDepartureLon(homePointModel.getUpHoverLongitude());
        launchInfo.setAltType(getHeightType(homePointModel.getUpHoverAltitudeType().getValue()));
        if (homePointModel.getUpHoverRadius() < MIN_HOVER_RADIUS) {
            homePointModel.setUpHoverRadius(MIN_HOVER_RADIUS);
        }
        launchInfo.setDepartureR((int) homePointModel.getUpHoverRadius());
        launchInfo.setDepartureVel(airVel);

        launchInfo.setTransAlt((int) taskModel.getTakeoffHeight());
        launchInfo.setDepartureAlt((int) (homePointModel.getUpHoverHeight()));

        LandInfo landInfo = new LandInfo();
        landInfo.setApproachLat(homePointModel.getDownHoverLatitude());
        landInfo.setApproachLon(homePointModel.getDownHoverLongitude());

        landInfo.setAltType(getHeightType(homePointModel.getDownHoverAltitudeType().getValue()));
        if (homePointModel.getDownHoverRadius() < MIN_HOVER_RADIUS) {
            homePointModel.setDownHoverRadius(MIN_HOVER_RADIUS);
        }
        landInfo.setApproachR((int) homePointModel.getDownHoverRadius());
        landInfo.setApproachVel(airVel);

        if (homePointModel.isFollowDroneLocation()) {
            landInfo.setHomeLat(drone[0]);
            landInfo.setHomeLon(drone[1]);
            landInfo.setHomeAlt(0);
            landInfo.setHomeAltType(MissionAltitudeType.RELATIVE.getValue());
        } else {
            landInfo.setHomeLat(homePointModel.getLatitude());
            landInfo.setHomeLon(homePointModel.getLongitude());
            landInfo.setHomeAlt(homePointModel.getHeight());
            landInfo.setHomeAltType(getHeightType(homePointModel.getAltitudeType().getValue()));
        }

        landInfo.setTransAlt(taskModel.getHomePoint().getLandHeight());
        landInfo.setApproachAlt(homePointModel.getDownHoverHeight());


        ArrayList<ForceLandInfo> forceLandInfo = new ArrayList<>();
        for (Coordinate3DModel landing : homePointModel.getForceLandingPoints()) {
            ForceLandInfo forceLandInfo1 = new ForceLandInfo();
            forceLandInfo.add(forceLandInfo1);
        }

        ArrayList<MissionInfo> missionInfo = new ArrayList<>();
        MissionInfo missionInfo1 = new MissionInfo();
        MissionType subMissionType = taskModel.getSummaryTaskInfo().getMissionType();
        switch (subMissionType) {
            case MISSION_TYPE_MAPPING_RECTANGLE:
            case MISSION_TYPE_MAPPING_POLYGON:
                missionInfo1.setSubMissionType(2);
                break;
            case MISSION_TYPE_QUICK_8:
                missionInfo1.setSubMissionType(3);
                break;
            default:
                missionInfo1.setSubMissionType(1);
                break;
        }

        int FinishMove = taskModel.getFinishActionType().getValue();
        int LinkLostMove = taskModel.getLostAction() == RemoteControlLostSignalAction.RETURN_HOME ? 2 : 1;
        missionInfo1.setFinishMove(FinishMove);
        missionInfo1.setLinkLostMove(LinkLostMove);
        missionInfo1.setSubMissionInfo(subMissionInfo);
        missionInfo1.setWpInfo(wpInfo);
        missionInfo1.setWPNum(wpInfo.size());
        missionInfo1.setInterestArea(interestAreaArrayList);
        missionInfo1.setIANum(interestAreaArrayList.size());

        missionInfo.add(missionInfo1);
        introInfo.setSubMisNum(missionInfo.size());
        introInfo.setPlanningType(taskModel.getPlanningType().getValue());
        return new PathPlanningParameter(introInfo, launchInfo, landInfo, forceLandInfo, missionInfo);
    }

    private static int getHeightType(int downHoverAltitude) {
        return downHoverAltitude == 0 ? 2 : downHoverAltitude;
    }

    /**
     * 获取在线规划路径
     *
     * @param onlinePlanningInfo 在线规划参数对象
     * @return 航线
     */
    public static PathPlanningResult getOnlinePlanningPath(OnlinePlanningInfo onlinePlanningInfo, int itemNum) {
        double precisionUnit = (int) Math.pow(10, 7);
        double heightUnit = 1000;
        double angleUnit = 10;
        double[] startPoint = new double[3];
        startPoint[0] = onlinePlanningInfo.getFLat() / precisionUnit;
        startPoint[1] = onlinePlanningInfo.getFLon() / precisionUnit;
        startPoint[2] = onlinePlanningInfo.getFAlt() / heightUnit;
        double startR = onlinePlanningInfo.getFRadius() / heightUnit;
        double startYaw = onlinePlanningInfo.getFYaw() / angleUnit;

        double[] endPoint = new double[3];
        endPoint[0] = onlinePlanningInfo.getToLat() / precisionUnit;
        endPoint[1] = onlinePlanningInfo.getToLon() / precisionUnit;
        endPoint[2] = onlinePlanningInfo.getToAlt() / heightUnit;
        double endR = onlinePlanningInfo.getToRadius() / heightUnit;
        double endYaw = onlinePlanningInfo.getToYaw() / angleUnit;

        double[] auxLLA = new double[3];
        auxLLA[0] = onlinePlanningInfo.getAuxLat() / precisionUnit;
        auxLLA[1] = onlinePlanningInfo.getAuxLon() / precisionUnit;
        auxLLA[2] = onlinePlanningInfo.getAuxAlt() / heightUnit;
        double auxR = onlinePlanningInfo.getAuxRadius() / heightUnit;
        double auxYaw = onlinePlanningInfo.getAuxYaw() / angleUnit;

        double cruiseSpeed = MissionConstant.MISSION_SPEED;
        double minRadius = MIN_HOVER_RADIUS;
        double maxSpeed = MissionConstant.MAX_SPEED;


        short planType = onlinePlanningInfo.getPlanningType();
        String text = planType + "," + itemNum + "," + Arrays.toString(endPoint) + "," + Arrays.toString(startPoint) + "," + startR + "," + startYaw + "," + Arrays.toString(endPoint) + "," +
                endR + "," + endYaw + "," + Arrays.toString(auxLLA) + "," + auxR + "," + auxYaw + "," + cruiseSpeed + "," + minRadius + "," + maxSpeed;
        AutelLog.debug_i("updateOnlinePlanning", "updateOnlinePath=" + text);
        return NativeHelper.onlinePath(planType, itemNum, endPoint, startPoint, startR, startYaw, endPoint,
                endR, endYaw, auxLLA, auxR, auxYaw, cruiseSpeed, minRadius, maxSpeed);
    }

    /**
     * 返航任务
     */
    public static PathPlanningResult getReturnHomePointPath(LandInfo landInfo) {
        IntroInfo introInfo = new IntroInfo();
        LaunchInfo launchInfo = new LaunchInfo();
        landInfo.setAltType(landInfo.getAltType());
        landInfo.setHomeAltType(landInfo.getHomeAltType());

        ArrayList<ForceLandInfo> forceLandInfo = new ArrayList<>();
        ArrayList<MissionInfo> missionInfo = new ArrayList<>();
        introInfo.setSubMisNum(missionInfo.size());
        introInfo.setInitLat(landInfo.getHomeLat());
        introInfo.setInitLon(landInfo.getHomeLon());
        introInfo.setInitAlt(landInfo.getHomeAlt());
        introInfo.setPlanningType(LANDING);
        introInfo.setMinRadius(MIN_HOVER_RADIUS);

        return NativeHelper.getPathPlanning(new PathPlanningParameter(introInfo, launchInfo, landInfo, forceLandInfo, missionInfo));
    }


    /**
     * 8字快速任务
     */
    public static PathPlanningResult getQuick8Path(QuickTaskPoint quickTaskPoint) {
        AutelLog.debug_i(TAG, "getQuick8Path -> quickTaskPoint=" + quickTaskPoint.toString());
        PathPlanningParameter pathPlanningParameter = getQuickMissionParameter(quickTaskPoint);
        NativeHelper.setPathPlanningParameter(pathPlanningParameter);
        return NativeHelper.getPathPlanning(pathPlanningParameter);
    }

    public static PathPlanningParameter getQuickMissionParameter(QuickTaskPoint quickTaskPoint) {
        IntroInfo introInfo = new IntroInfo();
        introInfo.setInitLat(quickTaskPoint.getLatitude());
        introInfo.setInitLon(quickTaskPoint.getLongitude());
        introInfo.setInitAlt(quickTaskPoint.getHeight());
        LaunchInfo launchInfo = new LaunchInfo();
        LandInfo landInfo = new LandInfo();
        ArrayList<ForceLandInfo> forceLandInfo = new ArrayList<>();
        ArrayList<MissionInfo> missionInfo = new ArrayList<>();

        MissionInfo missionInfo1 = new MissionInfo();
        missionInfo1.setSubMissionType(3);

        //具体参数待联调后补充
        int FinishMove = 1;
        int LinkLostMove = 2;
        missionInfo1.setFinishMove(FinishMove);
        missionInfo1.setLinkLostMove(LinkLostMove);

        SubMissionInfo subMissionInfo = new SubMissionInfo();
        missionInfo1.setSubMissionInfo(subMissionInfo);

        ArrayList<WPInfo> wpInfo = new ArrayList<>();
        WPInfo wpInfo1 = new WPInfo();
        wpInfo1.setWpIndex((short) 1);
        wpInfo1.setWpLat(quickTaskPoint.getLatitude());
        wpInfo1.setWpLon(quickTaskPoint.getLongitude());
        wpInfo1.setWpAlt(quickTaskPoint.getHeight());
        wpInfo1.setWpAltType(quickTaskPoint.getAltitudeType().getValue());
        wpInfo1.setWpType(4);
        wpInfo1.setWpVel(20);
        wpInfo1.setWpRadius(quickTaskPoint.getRadius());
        wpInfo1.setWpTurnMode(5);
        wpInfo1.setWpClimbMode(1);
        wpInfo1.setWpTurnParam1((float) quickTaskPoint.getAngle());
        wpInfo1.setWpReserved1((float) quickTaskPoint.getDistance());
        wpInfo.add(wpInfo1);

        missionInfo1.setWpInfo(wpInfo);
        missionInfo1.setWPNum(wpInfo.size());
        missionInfo.add(missionInfo1);

        introInfo.setSubMisNum(missionInfo.size());
        introInfo.setPlanningType(MISSION);

        return new PathPlanningParameter(introInfo, launchInfo, landInfo, forceLandInfo, missionInfo);
    }

    public static WaypointModel quickTaskPointToWaypointModel(QuickTaskPoint quickTaskPoint){
        WaypointModel waypointModel = new WaypointModel();
        waypointModel.setLatitude(quickTaskPoint.getLatitude());
        waypointModel.setLongitude(quickTaskPoint.getLongitude());
        waypointModel.setHeight(quickTaskPoint.getHeight());
        waypointModel.setMissionAltitudeType(quickTaskPoint.getAltitudeType());
        waypointModel.setHoverRadius(quickTaskPoint.getRadius());
        waypointModel.setHoverCylinderNumber((int) quickTaskPoint.getAngle());
        waypointModel.setWpReserved1((int) quickTaskPoint.getDistance());
        return waypointModel;
    }

    public static QuickTaskPoint waypointModelToQuickTaskPoint(WaypointModel waypointModel){
        QuickTaskPoint quickTaskPoint = new QuickTaskPoint();
        quickTaskPoint.setLocationStatus(LocationStatus.QUICK_NUMBER8);
        quickTaskPoint.setLatitude(waypointModel.getLatitude());
        quickTaskPoint.setLongitude(waypointModel.getLongitude());
        quickTaskPoint.setHeight(waypointModel.getHeight());
        quickTaskPoint.setAltitudeType(waypointModel.getMissionAltitudeType());
        quickTaskPoint.setRadius(waypointModel.getHoverRadius());
        quickTaskPoint.setAngle(waypointModel.getHoverCylinderNumber());
        quickTaskPoint.setDistance(waypointModel.getWpReserved1());
        return quickTaskPoint;
    }


    private static double[] getDronePoint(AutelCoordinate3D droneLocation) {
        double[] drone = new double[3];
        drone[0] = droneLocation.getLatitude();
        drone[1] = droneLocation.getLongitude();
        drone[2] = droneLocation.getAltitude();
        return drone;
    }

    private static double[] getHomePoint(HomePointModel homePointModel) {
        double[] homPoint = new double[3];
        homPoint[0] = homePointModel.getLatitude();
        homPoint[1] = homePointModel.getLongitude();
        homPoint[2] = homePointModel.getHeight();
        return homPoint;
    }

    public static double getTurningRadius() {
        return MIN_HOVER_RADIUS;
    }


}