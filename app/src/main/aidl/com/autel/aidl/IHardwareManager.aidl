// IHardwareManager.aidl
package com.autel.aidl;
import com.autel.aidl.WIFiScanResult;
//import android.net.wifi.ScanResult;
import com.autel.aidl.ISerialKeystrokeListener;
import com.autel.aidl.ISerialG5_8StatusListener;
import com.autel.aidl.IHardwareRealTimeInterface;
import com.autel.aidl.IBetaWIFiListListener;
import java.util.List;
// Declare any non-default types here with import statements


interface IHardwareManager {

    //扫描Beta Wifi
    void startScan(IBetaWIFiListListener listener);
    //连接wifi
    void connect(in WIFiScanResult ssid,String pwd);
    //监听平板按键消息事件
    //监听平板按键消息事件
    void addSerialKeystrokeListener(ISerialKeystrokeListener listener);
    //监听5.8g连接状态消息
    void addSerialG5_8StatusListener(ISerialG5_8StatusListener listener);
    //监听平板串口拨轮数据
    void addHardwareRealTimeListener(IHardwareRealTimeInterface listener);
     //移除监听平板按键消息事件
        void removeAllListener();
    //5.8g配对
    void start5_8gPairing();


}
