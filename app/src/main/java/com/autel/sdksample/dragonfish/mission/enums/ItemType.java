package com.autel.sdksample.dragonfish.mission.enums;

public enum ItemType {
    GO_EXIT,                        //退回到任务列表
    CREATE_TASK,                    //点击新建任务按钮
    SAVE_MISSION,                   //保存任务
    GENERATE_MISSION,               //生成航线
    EXPAND_TASK,                    //任务展开、
    RETURN_HEIGHT,                  //返航点高度
    SET_HOME_TO_AIRCRAFT,           //返航点设置
    TAKEOFF_RADIUS,                 //上升盘旋半径
    LANDING_RADIUS,                 //下降盘旋半径
    GROUND_RESOLUTION,              //地面分辨率
    HEADING_ANGLE,                  //主航线角度
    COURSE_LAP,                     //主航线重叠率
    SIDE_LAP,                       //旁向重叠率
    WAYPOINT_CIRCLE_RADIUS,          //航点环绕半径
    WAYPOINT_ACTION_FLY_OVER,       //航点动作：飞跃
    WAYPOINT_ACTION_HOVER,          //航点动作：悬停
    FLY_SPEED,                       //飞行速度
    SHOOT_DISTANCE,                  //兴趣点拍摄距离
    HORIZONTAL_ANGLE,                //兴趣点水平角度
    QUICK_POINT_RADIUS,              //快速任务点半径
    POI_CIRCLE_RADIUS,               //兴趣点环绕半径
    UNKNOWN
}
