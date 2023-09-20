# DragonFish_SDK_Sample

## 编译步骤：
Follow the steps below to add “SDK package” to the project. (Note: this is currently supported only in the Android Studio environment):
1. Add implementation 'com.tencent.mars:mars-xlog:1.2.5' to the module-level build.gradle file.
2. Add api(name: "autel-sdk-release_V2.0.2", ext: "aar") {
   exclude module: 'okio'
   exclude module: 'okhttp'
   } to the module-level build.gradle file. Currently, the latest SDK version is 2.0.2.

## 说明：
使用SDK前需要先将根目录下的app-release_basestation.apk拷贝放到平板的system/app目录下后重启平板，再运行此工程
adb remount
adb push app-release_basestation.apk目录 /system/app/
adb reboot

SDK开发使用事项
1、需要先启动BaseStationDemo App连接上基站Wifi;
2、杀掉官方提供的Voyager app，通过adb shell进入后,ps | grep cruiser 查找进程后，kill掉此进程 或者卸载掉Voyager app;