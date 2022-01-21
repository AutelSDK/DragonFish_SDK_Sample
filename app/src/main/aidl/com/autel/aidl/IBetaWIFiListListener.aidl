// IBetaWIFiListListener.aidl
package com.autel.aidl;
import com.autel.aidl.WIFiScanResult;
// Declare any non-default types here with import statements

interface IBetaWIFiListListener {
    void onScanLists(in List<WIFiScanResult> list);
    void isConnected(in boolean isConnect);
}
