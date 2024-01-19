# DragonFish_SDK_Sample

说明：
使用SDK前需要先将根目录下的app-release_basestation.apk拷贝放到平板的system/app目录下后重启平板，再运行此工程
adb remount
adb push app-release_basestation.apk目录 /system/app/
adb reboot


Follow the steps below to add “SDK package” to the project. (Note: this is currently supported only in the Android Studio environment):
1. Add “url https://dl.bintray.com/auteloss/android” to the application-level build.gradle file
2. Add “api(name: "autel-sdk-release_V2.0.5", ext: "aar")” to the module-level build.gradle file. Currently, the latest SDK version is V2.0.5.

## Note:
Before using the SDK, you need to copy the app-release_basestation.apk in the root directory to the system/app directory of the tablet,
restart the tablet, and then run the project
adb remount
adb push app-release_basestation_V1.2.apk    directory /system/app/
adb reboot

2024-1-15 SDK V2.0.6更新记录
1、增加预设备降点功能，详见DFWayPointActivity.java中onAllMissionTest方法调用对应接口生成aut文件
2、增加去往备降点接口，详见DFFlyControllerActivity.java中setForceLand方法示例
/**
* 降落到预设备降点
* @param landIndex 降落点序号 从1开始~备降点的个数
* @param callback
*/
void land(int landIndex, CallbackWithNoParam callback);
3、需要更新对应V3飞控仿真版本，及真机版本方可验证此功能

2024-1-15 SDK V2.0.5更新记录
1、更新app-release_basestation_V1.2.apk,增加平板系统开机后自启功能，无需手动启动app-release_basestation.apk
2、支持新航线算法规划，及新航线算法规划demo示例，详见DFWayPointActivity.java
3、更新此版本后，仿真环境及真机验证也需要更新到新航线固件版本才能正常使用
4、增加左右波轮控制云台功能实现，详见SerialAidlActivity.java中SerialRealTimeCallback 回调实现

2024-1-3 更新记录
1、更新app-release_basestation_V1.1.apk,增加aidl 支持平板左右波轮  需要更新此Apk 及 IHardwareRealTimeInterface.aidl文件
interface IHardwareRealTimeInterface {
//实时上报的平板波轮数据
void onRealTimeWheelListener(int leftWheel,int rightWheel);
}
2、绑定service服务包名修改
Intent intent = new Intent();
intent.setAction("com.autel.aidlservice.aidl");
intent.setPackage("com.autel");
bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
