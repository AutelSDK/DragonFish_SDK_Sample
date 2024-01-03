# DragonFish_SDK_Sample

说明：
使用SDK前需要先将根目录下的app-release_basestation.apk拷贝放到平板的system/app目录下后重启平板，再运行此工程
adb remount
adb push app-release_basestation.apk目录 /system/app/
adb reboot


Follow the steps below to add “SDK package” to the project. (Note: this is currently supported only in the Android Studio environment):
1. Add “url https://dl.bintray.com/auteloss/android” to the application-level build.gradle file
2. Add “compile com.autel: autel-sdk:2.0.11.57@aar” to the module-level build.gradle file. Currently, the latest SDK version is 2.0.11.57.

## Note:
Before using the SDK, you need to copy the app-release_basestation.apk in the root directory to the system/app directory of the tablet,
restart the tablet, and then run the project
adb remount
adb push app-release_basestation.apk    directory /system/app/
adb reboot

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
