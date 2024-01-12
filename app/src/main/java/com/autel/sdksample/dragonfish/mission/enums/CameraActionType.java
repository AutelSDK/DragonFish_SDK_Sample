package com.autel.sdksample.dragonfish.mission.enums;


import com.autel.sdksample.R;
import com.autel.sdksample.utils.ResourcesUtils;

public enum CameraActionType {
    NONE(0, ResourcesUtils.getString(R.string.none_action)),
    TAKE_PHOTO(1, ResourcesUtils.getString(R.string.take_photo)),
    TIMELAPSE(2, ResourcesUtils.getString(R.string.time_lapse)),
    DISTANCE(3, ResourcesUtils.getString(R.string.distance_lapse)),
    RECORD(4, ResourcesUtils.getString(R.string.take_record)),
    UNKNOWN(-1, ResourcesUtils.getString(R.string.unknown));

    private final int value;
    private String desc;

    CameraActionType(int i) {
        this.value = i;
    }

    CameraActionType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static CameraActionType find(int value) {
        if (NONE.value == value) {
            return NONE;
        }
        if (TAKE_PHOTO.value == value) {
            return TAKE_PHOTO;
        }
        if (TIMELAPSE.value == value) {
            return TIMELAPSE;
        }
        if (DISTANCE.value == value) {
            return DISTANCE;
        }
        if (RECORD.value == value) {
            return RECORD;
        }
        return UNKNOWN;
    }

    public String getKmlKey() {
        if (NONE.value == value) {
            return "None";
        }
        if (TAKE_PHOTO.value == value) {
            return "TakePhoto";
        }
        if (TIMELAPSE.value == value) {
            return "TimeLapse";
        }
        if (DISTANCE.value == value) {
            return "Distance";
        }
        if (RECORD.value == value) {
            return "Record";
        }
        return "UNKNOWN";
    }
}
