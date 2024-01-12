package com.autel.sdksample.dragonfish.mission.enums;

/**
 * <p> 功能描述：    <\br>
 * <p>
 * 详细描述：
 *
 * @Author: 龚寿生
 * @CreateDate: 2023/8/15 17:00
 * @UpdateUser: 最后一次更新者
 * @UpdateDate: 2023/8/15 17:00
 * @UpdateRemark: 最后一次更新说明
 * @Version: 1.0 版本
 */
public enum OnlinePlanType {
    /**
     * 在线规划航线类型：
     * 1.返航
     * 2.迫降
     * 3.临时任务
     * 4.航点切换
     * 5.断点
     * 6.快速任务（盘旋）
     * 7.返回任务航线
     */
    RETURN_HOME(1),
    FORCED_LAND(2),
    TEMP_MISSION(3),
    SWITCH_POINT(4),
    BREAK_POINT(5),
    HOVER(6),
    RETURN_MISSION(7);

    private final int value;

    OnlinePlanType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
