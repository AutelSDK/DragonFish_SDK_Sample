# DragonFish_SDK_Sample
## Compile instructions:
Follow the steps below to add “SDK package” to the project. (Note: this is currently supported only in the Android Studio environment):
1. Add “url https://dl.bintray.com/auteloss/android” to the application-level build.gradle file
2. Add “compile com.autel: autel-sdk:2.0.11.57@aar” to the module-level build.gradle file. Currently, the latest SDK version is 2.0.11.57.

## Note: 
Before using the SDK, you need to copy the app-release_basestation.apk in the root directory to the system/app directory of the tablet, 
restart the tablet, and then run the project 
adb remount 
adb push app-release_basestation.apk    directory /system/app/
adb reboot