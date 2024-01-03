// IHardwareRealTimeInterface.aidl
package com.autel.aidl;

// Declare any non-default types here with import statements

interface IHardwareRealTimeInterface {
     //实时上报的平板波轮数据
    void onRealTimeWheelListener(int leftWheel,int rightWheel);
}
