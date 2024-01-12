package com.autel.sdksample.dragonfish.mission.model;

import androidx.annotation.NonNull;


import java.util.Objects;

public class PoiLinkPointModel {
    private Long id;

    private int startIndex;

    private int endIndex;

    private boolean isSelected;

    public PoiLinkPointModel() {
    }

    public PoiLinkPointModel(int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        isSelected = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PoiLinkPointModel)) return false;
        PoiLinkPointModel that = (PoiLinkPointModel) o;
        return getStartIndex() == that.getStartIndex() &&
                getEndIndex() == that.getEndIndex() &&
                isSelected() == that.isSelected() &&
                Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getStartIndex(), getEndIndex(), isSelected());
    }

    @NonNull
    @Override
    protected PoiLinkPointModel clone() throws CloneNotSupportedException {
        return (PoiLinkPointModel) super.clone();
    }
}