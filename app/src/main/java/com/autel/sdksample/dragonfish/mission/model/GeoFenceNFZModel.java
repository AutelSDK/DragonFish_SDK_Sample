package com.autel.sdksample.dragonfish.mission.model;


import androidx.annotation.NonNull;

import com.autel.lib.model.mission.enums.GeoFenceAreaShape;
import com.autel.lib.model.mission.enums.GeoFenceAreaType;
import com.autel.sdksample.base.mission.AutelLatLng;
import com.autel.sdksample.utils.CollectionUtil;
import com.autel.sdksample.utils.LocationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GeoFenceNFZModel{

    private Long id;

    /**
     * 用户id
     */
    private String userId;
    /**
     * 名称
     */
    private String name;
    /**
     * 颜色
     */
    private String areaColor = GeoFenceAreaType.GEO_FENCE.getColor();

    /**
     * 区域类型。0：可飞区；1：禁飞区；2：强制可飞区。
     */
    private GeoFenceAreaType areaType = GeoFenceAreaType.GEO_FENCE;
    /**
     * 形状类型。0：圆形：1：多边形；
     */
    private GeoFenceAreaShape areaShape = GeoFenceAreaShape.CIRCLE;
    /**
     * 地图层级（暂时未用到 预留）
     */
    private int areaLevel;
    /**
     * 多边形顶点数量（多边形属性）
     */
    private int polygonNum;
    /**
     * 中心点维度（圆形属性）
     */
    private double latitude;
    /**
     * 中心点经度（圆形属性）
     */
    private double longitude;
    /**
     * 半径（圆形属性）
     */
    private float radius = 2000f;
    /**
     * 高度下限
     */
    private int minHeight;
    /**
     * 高度上限
     */
    private int maxHeight;
    /**
     * 高度
     */
    private float height = 8_000;
    /**
     * 高度是否有效（如果无效，表示height无限大）
     */
    private boolean isHeightValid;
    /**
     * 多边形顶点列表（多边形属性）
     */
    private ArrayList<Coordinate3DModel> latLngs = new ArrayList<>();
    /**
     * 有效时间的起始时间（UTC时间）
     */
    private long effectiveTimeStart;
    /**
     * 有效时间的结束时间（UTC时间）
     */
    private long effectiveTimeEnd;

    /**
     * 数据生成的时间（UTC时间）
     */
    private long createTime;
    /**
     * 最后一次更新的时间（UTC时间）
     */
    private long latestUpdateTime;
    /**
     * 唯一标识
     */
    private String uuid;
    /**
     * 数据状态，用于告知服务器数据状态。
     * 0:本地数据（未上传过服务器）;1：未更新，不需要操作。2：数据发生更新；3：数据已被删除。
     */
    private int updateStatus;

    /*地图截图存储路径*/
    private String mapScreenshotPath;

    private boolean isHasMapDraw = false;

    public void initAreaType(int value) {
        setAreaType(GeoFenceAreaType.find(value));
    }

    public void initAreaShape(int value) {
        setAreaShape(GeoFenceAreaShape.find(value));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GeoFenceAreaType getAreaType() {
        return areaType;
    }

    public void setAreaType(GeoFenceAreaType areaType) {
        this.areaType = areaType;
        setAreaColor(areaType.getColor());
    }

    public GeoFenceAreaShape getAreaShape() {
        return areaShape;
    }

    public void setAreaShape(GeoFenceAreaShape areaShape) {
        this.areaShape = areaShape;
    }

    public int getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(int areaLevel) {
        this.areaLevel = areaLevel;
    }

    public int getPolygonNum() {
        return polygonNum;
    }

    public void setPolygonNum(int polygonNum) {
        this.polygonNum = polygonNum;
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

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }


    public boolean isHeightValid() {
        return isHeightValid;
    }

    public void setHeightValid(boolean heightValid) {
        isHeightValid = heightValid;
    }

    public ArrayList<Coordinate3DModel> getLatLngs() {
        return latLngs;
    }

    public void setLatLngs(ArrayList<Coordinate3DModel> latLngs) {
        this.latLngs = latLngs;
        if (latLngs != null) {
            polygonNum = latLngs.size();
        } else {
            polygonNum = 0;
        }
    }

    public String getAreaColor() {
        return areaColor;
    }

    public void setAreaColor(String areaColor) {
        this.areaColor = areaColor;
    }

    public long getEffectiveTimeEnd() {
        return effectiveTimeEnd;
    }

    public void setEffectiveTimeEnd(long effectiveTimeEnd) {
        this.effectiveTimeEnd = effectiveTimeEnd;
    }

    public long getEffectiveTimeStart() {
        return effectiveTimeStart;
    }

    public void setEffectiveTimeStart(long effectiveTimeStart) {
        this.effectiveTimeStart = effectiveTimeStart;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLatestUpdateTime() {
        return latestUpdateTime;
    }

    public void setLatestUpdateTime(long latestUpdateTime) {
        this.latestUpdateTime = latestUpdateTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(int updateStatus) {
        this.updateStatus = updateStatus;
    }

    public String getMapScreenshotPath() {
        return mapScreenshotPath;
    }

    public void setMapScreenshotPath(String mapScreenshotPath) {
        this.mapScreenshotPath = mapScreenshotPath;
    }

    public AutelLatLng getAutelLatLng() {
        return new AutelLatLng(getLatitude(), getLongitude());
    }

    public void setAutelLatLng(AutelLatLng autelLatLng) {
        latitude = autelLatLng.getLatitude();
        longitude = autelLatLng.getLongitude();
    }

    public void setAutelLatLngs(ArrayList<AutelLatLng> list) {
        latLngs = LocationUtils.convertToCoordinate3DList(list);
        polygonNum = latLngs.size();
    }

    public ArrayList<AutelLatLng> getAutelLatLngs() {
        return LocationUtils.convertToLatLngList(latLngs);
    }

    public boolean isHasMapDraw() {
        return isHasMapDraw;
    }

    public void setHasMapDraw(boolean hasMapDraw) {
        isHasMapDraw = hasMapDraw;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GeoFenceNFZModel that = (GeoFenceNFZModel) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @NonNull
    @Override
    public GeoFenceNFZModel clone() {
        GeoFenceNFZModel geoFenceNFZModel = new GeoFenceNFZModel();
        geoFenceNFZModel.setName(name);
        geoFenceNFZModel.setAreaType(areaType);
        geoFenceNFZModel.setAreaShape(areaShape);
        geoFenceNFZModel.setAreaLevel(areaLevel);
        geoFenceNFZModel.setAreaColor(areaColor);
        geoFenceNFZModel.setPolygonNum(polygonNum);
        geoFenceNFZModel.setLatitude(latitude);
        geoFenceNFZModel.setLongitude(longitude);
        geoFenceNFZModel.setRadius(radius);
        geoFenceNFZModel.setMinHeight(minHeight);
        geoFenceNFZModel.setMaxHeight(maxHeight);
        geoFenceNFZModel.setHeightValid(isHeightValid);
        geoFenceNFZModel.setEffectiveTimeEnd(effectiveTimeEnd);
        geoFenceNFZModel.setEffectiveTimeStart(effectiveTimeStart);
        geoFenceNFZModel.setCreateTime(createTime);
        geoFenceNFZModel.setLatestUpdateTime(latestUpdateTime);
        geoFenceNFZModel.setUserId(userId);
        geoFenceNFZModel.setUuid(uuid);
        geoFenceNFZModel.setUpdateStatus(updateStatus);
        geoFenceNFZModel.setMapScreenshotPath(mapScreenshotPath);
        geoFenceNFZModel.setHasMapDraw(isHasMapDraw);
        geoFenceNFZModel.setLatLngs(latLngs);
        return geoFenceNFZModel;
    }

    @Override
    public String toString() {
        return "GeoFenceModel{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", areaColor='" + areaColor + '\'' +
                ", areaType=" + areaType +
                ", areaShape=" + areaShape +
                ", areaLevel=" + areaLevel +
                ", polygonNum=" + polygonNum +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", radius=" + radius +
                ", lowerLimit=" + minHeight +
                ", upperLimit=" + maxHeight +
                ", isHeightValid=" + isHeightValid +
                ", latLngs=" + latLngs +
                ", effectiveTimeStart=" + effectiveTimeStart +
                ", effectiveTimeEnd=" + effectiveTimeEnd +
                ", createTime=" + createTime +
                ", latestUpdateTime=" + latestUpdateTime +
                ", uuid='" + uuid + '\'' +
                ", updateStatus=" + updateStatus +
                ", mapScreenshotPath='" + mapScreenshotPath + '\'' +
                ", isHasMapDraw=" + isHasMapDraw +
                '}';
    }
}
