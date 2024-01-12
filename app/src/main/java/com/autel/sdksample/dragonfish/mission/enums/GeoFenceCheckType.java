package com.autel.sdksample.dragonfish.mission.enums;

/**
 * <p> 功能描述：    <\br>
 * <p>
 * 详细描述：
 *
 * @Author: 龚寿生
 * @CreateDate: 2022/11/23 10:58
 * @UpdateUser: 最后一次更新者
 * @UpdateDate: 2022/11/23 10:58
 * @UpdateRemark: 最后一次更新说明
 * @Version: 1.0 版本
 */
public enum GeoFenceCheckType {
    MISSION(0), //任务
    MANUAL(1), //手动模式
    QUICK_TASK(2); //快速任务

    private final int value;

    GeoFenceCheckType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static GeoFenceCheckType find(int value) {
        switch (value) {
            case 1:
                return MANUAL;
            case 2:
                return QUICK_TASK;
            default:
                return MISSION;
        }
    }
}
