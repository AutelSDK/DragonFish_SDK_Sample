package com.autel.sdksample.dragonfish.mission.model;


public abstract class BasePointModel{
    private int index;
    private int pointCount; //用于兴趣点，表示连接航点数量
    private boolean isSelect = false;
    private int distance;
    private boolean isLock = false;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPointCount() {
        return pointCount;
    }

    public void setPointCount(int pointCount) {
        this.pointCount = pointCount;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    @Override
    public String toString() {
        return "BasePointModel{" +
                "index=" + index +
                ", pointCount=" + pointCount +
                ", isSelect=" + isSelect +
                ", distance=" + distance +
                ", isLock=" + isLock +
                '}';
    }
}
