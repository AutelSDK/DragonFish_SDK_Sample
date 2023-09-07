# DragonFish_SDK_Sample

## 编译步骤：
Follow the steps below to add “SDK package” to the project. (Note: this is currently supported only in the Android Studio environment):
1. Add “url https://dl.bintray.com/auteloss/android” to the application-level build.gradle file
2. Add “compile com.autel: autel-sdk:2.0.11.57@aar” to the module-level build.gradle file. Currently, the latest SDK version is 2.0.11.57.

## 说明：
使用SDK前需要先将根目录下的app-release_basestation.apk拷贝放到平板的system/app目录下后重启平板，再运行此工程
adb remount
adb push app-release_basestation.apk目录 /system/app/
adb reboot
