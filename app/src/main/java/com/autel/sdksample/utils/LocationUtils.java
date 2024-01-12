package com.autel.sdksample.utils;



import com.autel.sdksample.base.mission.AutelLatLng;
import com.autel.sdksample.dragonfish.mission.model.Coordinate3DModel;

import java.util.ArrayList;

/**
 * <p> 功能描述：经纬度 合法校验类<br>
 * <p>
 * 详细描述：
 *
 * @UpdateUser: gss
 * @UpdateDate: 2021/12/30
 * @UpdateRemark: 优化警告问题
 * @Version: 1.0 版本
 */
public class LocationUtils {
    public static boolean isValidLatitude(double latitude) {
        return latitude > -90 && latitude < 90 && latitude != 0f;
    }

    public static boolean isValidLongitude(double longitude) {
        return longitude > -180 && longitude < 180 && longitude != 0f;
    }

    public static boolean isValidLatlng(double latitude, double longitude) {
        return isValidLatitude(latitude) && isValidLongitude(longitude);
    }

    /**
     *是否是无效的经纬度 true:无效经纬度，false：
     */
    public static boolean isInvalidLatlng(AutelLatLng latLng) {
        return !isValidLatitude(latLng.getLatitude()) || !isValidLongitude(latLng.getLongitude());
    }

    public static boolean isValidDLatitude(int dLat) {
        return dLat >= 0 && dLat < 90;
    }

    public static boolean isValidDLongitude(int dLng) {
        return dLng >= 0 && dLng < 180;
    }

    public static boolean isValidMValue(int mValue) {
        return mValue >= 0 && mValue < 60;
    }

    public static boolean isValidSValue(double sValue) {
        return sValue >= 0 && sValue < 60;
    }


    public  static ArrayList<AutelLatLng> convertToLatLngList(ArrayList<Coordinate3DModel> list) {
        ArrayList<AutelLatLng> data = new ArrayList<>();
        if (null != list && list.size() > 0) {
            for (Coordinate3DModel coordinate3D : list) {
                data.add(new AutelLatLng(coordinate3D.getLatitude(), coordinate3D.getLongitude()));
            }
        }
        return data;
    }

    public static ArrayList<Coordinate3DModel> convertToCoordinate3DList(ArrayList<AutelLatLng> list) {
        ArrayList<Coordinate3DModel> data = new ArrayList<>();
        for (AutelLatLng coordinate3D : list) {
            data.add(new Coordinate3DModel(coordinate3D.getLatitude(), coordinate3D.getLongitude(), (float) coordinate3D.getAltitude()));
        }
        return data;
    }
}
