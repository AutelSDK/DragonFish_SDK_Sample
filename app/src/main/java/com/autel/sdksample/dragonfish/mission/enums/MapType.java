package com.autel.sdksample.dragonfish.mission.enums;

import android.text.TextUtils;

public enum MapType {
    MAPBOX("MAPBOX"),
    UNKNOWN("Unknown");

    private final String mapType;

    MapType(String type) {
        this.mapType = type;
    }

    public String value() {
        return mapType;
    }

    public static MapType getMapType(String mapType) {
        if (TextUtils.equals(mapType, MAPBOX.value())) {
            return MAPBOX;
        }
        return UNKNOWN;
    }
}
