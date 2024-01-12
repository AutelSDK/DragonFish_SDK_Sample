package com.autel.sdksample.dragonfish.mission.enums;

import com.autel.common.product.AutelProductType;

public enum DroneType {

    DRAGONFISH(2, "dragonfish"),
    UNKNOWN(-1, "Unknown");

    private int value;
    private String desc;

    DroneType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static DroneType find(int value) {
        if (value == 2) {
            return DRAGONFISH;
        }
        return UNKNOWN;
    }

    public static DroneType getDroneType(AutelProductType productType) {
        switch (productType) {
            case DRAGONFISH:
            case DRAGONFISH_7_5_VTOL:
            case DRAGONFISH_15_VTOL:
            case DRAGONFISH_30_VTOL:
            case DRAGONFISH_75_VTOL:
                return DroneType.DRAGONFISH;
            default:
                return DroneType.UNKNOWN;
        }
    }

}
