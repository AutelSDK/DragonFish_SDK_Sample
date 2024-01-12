package com.autel.sdksample.utils;


import com.autel.common.mission.AutelCoordinate3D;
import com.autel.sdksample.dragonfish.mission.enums.CameraActionType;
import com.autel.sdksample.dragonfish.mission.enums.MissionActionType;
import com.autel.sdksample.dragonfish.mission.enums.MissionHoverDirection;
import com.autel.sdksample.dragonfish.mission.enums.WaypointType;
import com.autel.sdksample.dragonfish.mission.model.HomePointModel;
import com.autel.sdksample.dragonfish.mission.model.InspectionMissionModel;
import com.autel.sdksample.dragonfish.mission.model.MappingMissionModel;
import com.autel.sdksample.dragonfish.mission.model.MappingVertexPoint;
import com.autel.sdksample.dragonfish.mission.model.POIPoint;
import com.autel.sdksample.dragonfish.mission.model.PoiLinkPointModel;
import com.autel.sdksample.dragonfish.mission.model.TaskModel;
import com.autel.sdksample.dragonfish.mission.model.WaypointMissionModel;
import com.autel.sdksample.dragonfish.mission.model.WaypointModel;

import java.util.List;


public class MissionSaveUtils {

    private static final int WAYPOINT_PARAM_COLUMN = 16;
    private static final int POI_POINT_PARAM_COLUMN = 6;
    private static final int MAX_POI_LINK_POINT_NUM = 5;

    public static AutelCoordinate3D getDroneLocation(TaskModel taskModel, AutelCoordinate3D drone) {
        AutelCoordinate3D droneLocation = drone;
        if (droneLocation == null) {
            double latitude = taskModel.getHomePoint().getLatitude();
            double longitude = taskModel.getHomePoint().getLongitude();
            droneLocation = new AutelCoordinate3D(latitude, longitude, 0);
        }
        return droneLocation;
    }

    public static double[] getHomeLocation(TaskModel taskModel) {
        HomePointModel homePoint = taskModel.getHomePoint();
//        if (LocationUtils.isValidLatlng(homePoint.getGpsLatitude(), homePoint.getGpsLongitude())) {
//            return new double[]{homePoint.getGpsLatitude(), homePoint.getGpsLongitude(), homePoint.getReturnHeight()};
//        } else {
        return new double[]{homePoint.getLatitude(), homePoint.getLongitude(), homePoint.getHeight()};
//        }
    }

    public static double[] getLaunchLocation(TaskModel taskModel) {
        HomePointModel homePoint = taskModel.getHomePoint();
        return new double[]{homePoint.getUpHoverLatitude(), homePoint.getUpHoverLongitude(), homePoint.getUpHoverHeight(), homePoint.getUpHoverRadius()};
    }

    public static double[] getLandingLocation(TaskModel taskModel) {
        HomePointModel homePoint = taskModel.getHomePoint();
        return new double[]{homePoint.getDownHoverLatitude(), homePoint.getDownHoverLongitude(), homePoint.getDownHoverHeight(), homePoint.getDownHoverRadius()};
    }

    public static double[] getWaypointParamList(TaskModel taskModel) {
        WaypointMissionModel waypointMission = taskModel.getWaypointMission();
        MappingMissionModel mappingMission = taskModel.getMappingMission();
        InspectionMissionModel inspectionMission = taskModel.getInspectionMission();
        if (mappingMission != null) {
            List<MappingVertexPoint> vertexList = mappingMission.getVertexList();
            int size = vertexList.size();
            double[] paramList = new double[size * WAYPOINT_PARAM_COLUMN];
            for (int i = 0; i < size; i++) {
                paramList[i * WAYPOINT_PARAM_COLUMN] = i + 1;
                paramList[i * WAYPOINT_PARAM_COLUMN + 1] = 0;
                paramList[i * WAYPOINT_PARAM_COLUMN + 2] = vertexList.get(i).getLatitude();
                paramList[i * WAYPOINT_PARAM_COLUMN + 3] = vertexList.get(i).getLongitude();
                paramList[i * WAYPOINT_PARAM_COLUMN + 4] = vertexList.get(i).getHeight();
                paramList[i * WAYPOINT_PARAM_COLUMN + 5] = 0;
                paramList[i * WAYPOINT_PARAM_COLUMN + 6] = 0;
                paramList[i * WAYPOINT_PARAM_COLUMN + 7] = 0;
                paramList[i * WAYPOINT_PARAM_COLUMN + 8] = 0;
                paramList[i * WAYPOINT_PARAM_COLUMN + 9] = 0;
                paramList[i * WAYPOINT_PARAM_COLUMN + 10] = 0;
                paramList[i * WAYPOINT_PARAM_COLUMN + 11] = 0;
                paramList[i * WAYPOINT_PARAM_COLUMN + 12] = 0;
                paramList[i * WAYPOINT_PARAM_COLUMN + 13] = mappingMission.getGimbalPitch();
                paramList[i * WAYPOINT_PARAM_COLUMN + 14] = 0;
                paramList[i * WAYPOINT_PARAM_COLUMN + 15] = 0;
            }
            return paramList;
        } else if (waypointMission != null) {
            List<WaypointModel> waypointModelList = waypointMission.getWaypointList();
            int length = taskModel.getWaypointMission().getWaypointSize();
            double[] paramList = new double[length * WAYPOINT_PARAM_COLUMN];

            int waypointIndex = 1;
            for (WaypointModel waypoint : waypointModelList) {
                if (WaypointType.WAYPOINT == waypoint.getWaypointType()) {
                    double[] waypointParam = getWaypointParams(waypoint, waypointIndex);
                    System.arraycopy(waypointParam, 0, paramList, (waypointIndex - 1) * WAYPOINT_PARAM_COLUMN, waypointParam.length);
                    waypointIndex++;
                }
            }
            return paramList;
        }
        return new double[]{};
    }

    public static double[] getPoiPointParamList(TaskModel taskModel) {
        WaypointMissionModel waypointMission = taskModel.getWaypointMission();
        if (waypointMission != null) {
            List<POIPoint> poiPointList = waypointMission.getPoiPointList();
            int length = taskModel.getWaypointMission().getPOIPointSize();
            double[] paramList = new double[length * POI_POINT_PARAM_COLUMN];
            int poiIndex = 1;
            for (POIPoint point : poiPointList) {
                double[] poiParam = getPoiPointParams(point);
                System.arraycopy(poiParam, 0, paramList, (poiIndex - 1) * POI_POINT_PARAM_COLUMN, poiParam.length);
                poiIndex++;
            }
            return paramList;
        }
        return new double[]{};
    }


    /*
        航点定义根据接口协议有16个变量，分别为：
        变量 0：当前航点标识（目前等于航点在当前任务中的序号）
        变量 1：当前航点类型，其中：0–普通航点/飞越;1-兴趣点Orbit;4–起飞航点;5–按时间盘旋航点;6-按圈数盘旋航点;7–降落航点
        变量 2：航点坐标，纬度
        变量 3：航点坐标，经度
        变量 4：航点坐标，高度
        变量 5：航点飞行速度，单位米/秒
        变量 6：盘旋时间或盘旋圈数，只针对航点类型为盘旋有用
        变量 7：盘旋半径，单位：米
        变量 8：盘旋方向：0-顺时针;1-逆时针盘旋
        变量 9：兴趣点起始角度 1-360度
        变量10：兴趣点水平角度 1-360度
        变量11：相机动作类型: 0-无，1-拍照，2-定时拍照，3-定距拍照，4-录像
        变量12：相机动作参数，定时和定距的参数
        变量13：相机动作参数，云台俯仰角（-120 -- 0）
        变量14-15：未定义
    */
    private static double[] getWaypointParams(WaypointModel waypoint, int waypointIndex) {
        int waypointType = getWaypointType(waypoint.getMissionActionType());
        int radius = 0;
        int direction = 0;
        if (waypointType == 6) {
            radius = (int) waypoint.getHoverRadius();
            direction = getHoverDirection(waypoint.getHoverDirection());
        } else if (waypointType == 1) {
            radius = (int) waypoint.getShootDistance();
            direction = waypoint.getOrbitDirection().getValue();
        }
        double[] paramList = new double[16];
        paramList[0] = waypointIndex;
        paramList[1] = waypointType;
        paramList[2] = waypoint.getLatitude();
        paramList[3] = waypoint.getLongitude();
        paramList[4] = waypoint.getHeight();
        paramList[5] = waypoint.getSpeed();
        paramList[6] = waypoint.getHoverCylinderNumber();
        paramList[7] = radius;
        paramList[8] = direction;
        paramList[9] = waypointType == 1 ? waypoint.getEnterAngle() * Math.PI / 180 : 0;
        paramList[10] = waypointType == 1 ? waypoint.getShootHorizontalAngle() * Math.PI / 180 : 0;
        if (CollectionUtil.isNotEmpty(waypoint.getMissionActions())) {
            paramList[11] = waypoint.getMissionActions().get(0).getCameraActionType().getValue();
            paramList[12] = waypoint.getMissionActions().get(0).getCameraInterval();
        } else {
            paramList[11] = CameraActionType.NONE.getValue();
            paramList[12] = 0;
        }
        paramList[13] = waypoint.getGimbalPitch();
        paramList[14] = waypoint.getGimbalYaw();
        paramList[15] = 0;
        return paramList;
    }

    /*
        航点定义根据接口协议有17个变量，分别为：
        变量 0：纬度
        变量 1：经度
        变量 2：高度
        变量 3：半径
        变量 4：IP_Type，默认 11
        变量 5：关联航点个数
    */
    public static double[] getPoiPointParams(POIPoint poiPoint) {
        double[] paramList = new double[6];
        paramList[0] = poiPoint.getLatitude();
        paramList[1] = poiPoint.getLongitude();
        paramList[2] = poiPoint.getHeight();
        paramList[3] = poiPoint.getRadius();
        paramList[4] = 11;
        paramList[5] = getPoiLinkPointNum(poiPoint);
        return paramList;
    }

    public static int getWaypointType(MissionActionType missionAction) {
        switch (missionAction) {
            case TIMED_SURROUND:
            case ORDER_SURROUND:
                return 6;
            case ORBIT:
                return 1;
            default://默认用FLY_OVER，0
                return 0;
        }
    }

    public static int getHoverDirection(MissionHoverDirection hoverDirection) {
        return hoverDirection == MissionHoverDirection.ANTICLOCKWISE ? 1 : 0;
    }

    /**
     * 关联航点序号列表，每个观察点最多关联五个航点，数组个数为观察点个数*5
     */
    public static int[] getPoiLinkPointList(TaskModel task) {
        WaypointMissionModel waypointMission = task.getWaypointMission();
        if ((waypointMission == null || CollectionUtil.isEmpty(waypointMission.getPoiPointList()))) {
            return new int[]{};
        }
        List<POIPoint> poiPointList = waypointMission.getPoiPointList();
        int[] result = new int[poiPointList.size() * MAX_POI_LINK_POINT_NUM];
        for (int i = 0; i < poiPointList.size(); i++) {
            POIPoint point = poiPointList.get(i);
            System.arraycopy(getPoiLinkPoints(point), 0, result, i * MAX_POI_LINK_POINT_NUM, MAX_POI_LINK_POINT_NUM);
        }
        return result;
    }

    private static int getPoiLinkPointNum(POIPoint point) {
        if (CollectionUtil.isNotEmpty(point.getLinkPointIdList())) {
            int srcIndex = 0;
            for (PoiLinkPointModel model : point.getLinkPointIdList()) {
                int[] tempModelList = getLinkPointModelList(model);
                srcIndex += tempModelList.length;
            }
            return srcIndex;
        } else {
            return 0;
        }
    }

    /**
     * 关联航点的序号列表
     */
    public static int[] getPoiLinkPoints(POIPoint point) {
        if (CollectionUtil.isNotEmpty(point.getLinkPointIdList())) {
            int[] result = new int[MAX_POI_LINK_POINT_NUM];
            int srcIndex = 0;
            for (PoiLinkPointModel model : point.getLinkPointIdList()) {
                int[] tempModelList = getLinkPointModelList(model);
                System.arraycopy(tempModelList, 0, result, srcIndex, tempModelList.length);
                srcIndex += tempModelList.length;
            }
            return result;
        } else {
            return new int[MAX_POI_LINK_POINT_NUM];
        }
    }

    public static int[] getLinkPointModelList(PoiLinkPointModel model) {
        int[] result = new int[model.getEndIndex() - model.getStartIndex()];
        for (int i = model.getStartIndex(), j = 0; i < model.getEndIndex(); i++, j++) {
            result[j] = i + 1;
        }
        return result;
    }

    public static int[] getSortPoiLinkPoints(POIPoint point) {
        return selectSort(getPoiLinkPoints(point));
    }

    public static int[] selectSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int miniPost = i;
            for (int m = i + 1; m < arr.length; m++) {
                if (arr[m] < arr[miniPost]) {
                    miniPost = m;
                }
            }
            if (arr[i] > arr[miniPost]) {
                int temp;
                temp = arr[i];
                arr[i] = arr[miniPost];
                arr[miniPost] = temp;
            }
        }
        return arr;
    }
}
