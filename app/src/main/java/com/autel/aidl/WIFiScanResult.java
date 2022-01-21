package com.autel.aidl;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class WIFiScanResult implements Parcelable {

    /**
     * The network name.
     */
    public String SSID;
    /**
     * The address of the access point.
     */
    public String BSSID;
    /*
     * This field is equivalent to the |flags|, rather than the |capabilities| field
     * of the per-BSS scan results returned by WPA supplicant. See the definition of
     * |struct wpa_bss| in wpa_supplicant/bss.h for more details.
     */
    /**
     * Describes the authentication, key management, and encryption schemes
     * supported by the access point.
     */
    public String capabilities;


    public WIFiScanResult(String ssid,String bSSID,String capabilities){
        this.SSID = ssid;
        this.BSSID = bSSID;
        this.capabilities = capabilities;
    }

    protected WIFiScanResult(Parcel in) {
        SSID = in.readString();
        BSSID = in.readString();
        capabilities = in.readString();
    }

    public static final Creator<WIFiScanResult> CREATOR = new Creator<WIFiScanResult>() {
        @Override
        public WIFiScanResult createFromParcel(Parcel in) {
            return new WIFiScanResult(in);
        }

        @Override
        public WIFiScanResult[] newArray(int size) {
            return new WIFiScanResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(SSID);
        dest.writeString(BSSID);
        dest.writeString(capabilities);
    }
}
