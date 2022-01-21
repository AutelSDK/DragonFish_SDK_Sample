// ISerialKeystrokeListener.aidl
package com.autel.aidl;

//RemoteControllerNavigateButtonEvent
// Declare any non-default types here with import statements

interface ISerialKeystrokeListener {
    void onResponse(String event);
}
