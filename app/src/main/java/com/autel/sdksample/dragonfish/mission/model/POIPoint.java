package com.autel.sdksample.dragonfish.mission.model;

import androidx.annotation.NonNull;

import com.autel.lib.enums.MissionConstant;
import com.autel.sdksample.dragonfish.mission.enums.AltitudeCorrectionType;
import com.autel.sdksample.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class POIPoint extends BasePointModel  {
    private Long id;

    /* 高程校正方式 */
    private AltitudeCorrectionType correctionType = AltitudeCorrectionType.NONE;

    /* 坐标经纬度、相对高度、绝对海拔 */
    private double latitude;
    private double longitude;
    private float height;
    private float altitude;

    /* 半径 */
    private float radius = MissionConstant.DEFAULT_POI_FLY_RADIUS;

    /* 关联航点id的列表 */
    private List<PoiLinkPointModel> linkPointIdList = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AltitudeCorrectionType getCorrectionType() {
        return correctionType;
    }

    public void setCorrectionType(AltitudeCorrectionType correctionType) {
        this.correctionType = correctionType;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getAltitude() {
        return altitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public List<PoiLinkPointModel> getLinkPointIdList() {
        return linkPointIdList;
    }

    public void setLinkPointIdList(List<PoiLinkPointModel> linkPointIdList) {
        this.linkPointIdList = linkPointIdList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof POIPoint)) return false;
        POIPoint poiPoint = (POIPoint) o;
        return Double.compare(poiPoint.getLatitude(), getLatitude()) == 0 &&
                Double.compare(poiPoint.getLongitude(), getLongitude()) == 0 &&
                Float.compare(poiPoint.getHeight(), getHeight()) == 0 &&
                Float.compare(poiPoint.getAltitude(), getAltitude()) == 0 &&
                Float.compare(poiPoint.getRadius(), getRadius()) == 0 &&
                Objects.equals(getId(), poiPoint.getId()) &&
                getCorrectionType() == poiPoint.getCorrectionType() &&
                Objects.equals(linkPointIdList, poiPoint.linkPointIdList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCorrectionType(), getLatitude(), getLongitude(), getHeight(), getAltitude(), getRadius(), linkPointIdList);
    }

    @NonNull
    @Override
    protected POIPoint clone() {
        try {
            POIPoint poi = (POIPoint) super.clone();
            List<PoiLinkPointModel> modelList = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(linkPointIdList)) {
                for (PoiLinkPointModel model : linkPointIdList) {
                    modelList.add(model.clone());
                }
            }
            poi.setLinkPointIdList(modelList);
            return poi;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}