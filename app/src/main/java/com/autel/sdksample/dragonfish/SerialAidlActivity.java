package com.autel.sdksample.dragonfish;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.autel.aidl.IBetaWIFiListListener;
import com.autel.aidl.IHardwareManager;
import com.autel.aidl.IHardwareRealTimeInterface;
import com.autel.aidl.ISerialG5_8StatusListener;
import com.autel.aidl.ISerialKeystrokeListener;
import com.autel.aidl.WIFiScanResult;
import com.autel.sdksample.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;


/**
 * 接收平板按键数据之前，请确保先安装过包名为"com.autel.basestation"的BastStationDemo App
 * <p>
 * 将根目录下的app-release_basestation.apk拷贝放到平板的system/app目录下后重启平板，再运行此工程
 */
public class SerialAidlActivity extends AppCompatActivity {

    private static final String TAG = "SerialAidlActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_aidl);


    }


    @Override
    protected void onResume() {
        super.onResume();
        doBindService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        doUnbindService();
    }

    public void start5_8gPairing(View view) {
        try {
            if (null == mService) return;
            mService.start5_8gPairing();
            Log.d(TAG, "start5_8gPairing");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setHardwareRealtimelistener(View view) {
        try {
            if (null == mService) return;
            Log.d(TAG, "setHardwareRealtimelistener");
            mService.addHardwareRealTimeListener(mSerialRealTimeCallback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setSerialKeyStrokeListener(View view) {
        try {
            if (null == mService) return;
            mService.addSerialKeystrokeListener(mSerialKeyStrokeCallback);
            Log.d(TAG, "setSerialKeyStrokeListener");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void addSerialG5_8StatusListener(View view) {
        try {
            if (null == mService) return;
            mService.addSerialG5_8StatusListener(mSerial58gStatusCallback);
            Log.d(TAG, "addSerialG5_8StatusListener");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void removeAllListener(View view) {
        if (null != mService) {
            try {
                mService.removeAllListener();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
    }

    public void scanWifi(View view) {
        if (null != mService) {
            try {
                mService.startScan(mBetaWIFiListCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
    }

    public void connectWifi(View view) {
        if (null != listWIFis && listWIFis.size() > 0) {
            try {
                mService.connect(listWIFis.get(0), "123456789");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    List<WIFiScanResult> listWIFis = new ArrayList<>();

    private final class BetaWIFiListCallBack extends IBetaWIFiListListener.Stub {


        @Override
        public void onScanLists(List<WIFiScanResult> list) throws RemoteException {
            listWIFis.clear();
            listWIFis.addAll(list);
            Log.d(TAG, "onScanLists list.size " + list.size());
            for (WIFiScanResult result :
                    list) {
                Log.d(TAG, "onScanLists list.size " + result.SSID);
            }

        }

        @Override
        public void isConnected(boolean isConnect) throws RemoteException {
            Log.d(TAG, "wifi isConnected " + isConnect);
        }
    }

    private static final class SerialKeyStrokeCallBack extends ISerialKeystrokeListener.Stub {

        @Override
        public void onResponse(String event) throws RemoteException {
            Log.d(TAG, "SerialKeyStrokeCallBack " + event);
        }
    }

    private static final class SerialG5_8StatusCallback extends ISerialG5_8StatusListener.Stub {

        @Override
        public void onConnect(boolean isConnect) throws RemoteException {
            Log.d(TAG, "SerialG5_8StatusCallback " + isConnect);
        }
    }

    private static final class SerialRealTimeCallback extends IHardwareRealTimeInterface.Stub {

        @Override
        public void onRealTimeListener(String data) throws RemoteException {
            Log.d(TAG, "onRealTimeListener -> " + data);
        }
    }

    private IHardwareManager mService;
    private boolean mIsBound;
    private AdditionServiceConnection mServiceConnection;

    private BetaWIFiListCallBack mBetaWIFiListCallback = new BetaWIFiListCallBack();
    private SerialKeyStrokeCallBack mSerialKeyStrokeCallback = new SerialKeyStrokeCallBack();
    private SerialG5_8StatusCallback mSerial58gStatusCallback = new SerialG5_8StatusCallback();
    private SerialRealTimeCallback mSerialRealTimeCallback = new SerialRealTimeCallback();


    /**
     * bind service
     */
    private void doBindService() {
        mServiceConnection = new AdditionServiceConnection();
        Intent intent = new Intent();
        intent.setAction("com.autel.aidlservice.aidl");
        intent.setPackage("com.autel.basestation");
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);

    }

    /**
     * unbind service
     */
    private void doUnbindService() {
        if (mIsBound) {
            unbindService(mServiceConnection);
            mServiceConnection = null;
            mIsBound = false;
        }
    }

    class AdditionServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 连接的时候获取本地代理，这样我们就可以调用 service 中的方法了。
            mService = IHardwareManager.Stub.asInterface((IBinder) service);
            mIsBound = true;
            try {
                //设置死亡代理
                service.linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "onServiceConnected: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mIsBound = false;
            Log.d(TAG, "onServiceDisconnected: ");
        }
    }

    /**
     * 监听Binder是否死亡
     */
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mService == null) {
                return;
            }
            mService.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mService = null;
            //重新绑定
            doBindService();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mService) {
            try {
                mService.removeAllListener();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
    }
}
