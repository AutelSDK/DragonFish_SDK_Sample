package com.autel.sdksample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.autel.internal.sdk.AutelBaseApplication;
import com.autel.sdksample.util.ThreadUtils;
import com.autel.sdksample.utils.Singleton;
import com.autel.util.log.AutelLog;

import java.util.HashMap;
import java.util.Map;


/**
 * android设备电池电量变化管理类
 */
public class PhoneBatteryManager {

    private final Map<String, BatteryChangeListener> listeners = new HashMap<>();
    private int current, total;

    private final static Singleton<PhoneBatteryManager> mBetaWifiManager = new Singleton<PhoneBatteryManager>() {
        @Override
        protected PhoneBatteryManager create() {
            return new PhoneBatteryManager();
        }
    };

    public static PhoneBatteryManager getInstance() {
        return mBetaWifiManager.get();
    }

    private PhoneBatteryManager() {
        registerBatteryReceiver();
    }

    private void registerBatteryReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        AutelBaseApplication.getAppContext().registerReceiver(batteryReceiver, filter);
    }

    public boolean isBatteryCharging() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = AutelBaseApplication.getAppContext().registerReceiver(null, filter);

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        AutelLog.d("isBatteryCharging", "isCharging " + isCharging);
        return !isCharging;
    }

    public int getCurrentBattery() {
        return current;
    }

    public Map<String, BatteryChangeListener> getListeners() {
        return listeners;
    }

    private final BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            current = intent.getExtras().getInt("level");// 获得当前电量
            total = intent.getExtras().getInt("scale");// 获得总电量
            if (current >= 0) {
                ThreadUtils.runOnUiThread(() -> {
                    for (Map.Entry<String, BatteryChangeListener> entry : listeners.entrySet()) {
                        entry.getValue().batteryChange(current, total);
                    }
                });
            }
        }
    };

    public synchronized void addBatteryChangeListener(String className, BatteryChangeListener listener) {
        listeners.put(className, listener);
        if (current >= 0) {
            ThreadUtils.runOnUiThread(() -> {
                if (null != listener) {
                    listener.batteryChange(current, total);
                }
            });
        }
    }

    public synchronized void removeBatteryChangeListener(String className) {
        listeners.remove(className);
    }

    public interface BatteryChangeListener {
        void batteryChange(int current, int total);
    }
}
