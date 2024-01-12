package com.autel.sdksample.dragonfish.mission;

import static com.autel.lib.enums.MissionConstant.PIXEL_SIZE_XT701;
import static com.autel.lib.enums.MissionConstant.PIXEL_SIZE_XT706;
import static com.autel.lib.enums.MissionConstant.PIXEL_SIZE_XT708;
import static com.autel.lib.enums.MissionConstant.PIXEL_SIZE_XT709;
import static com.autel.lib.jniHelper.NativeHelper.getPathPlanningParameter;
import static com.autel.sdksample.utils.MissionSaveUtils.getDroneLocation;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.autel.common.CallbackWithNoParam;
import com.autel.common.CallbackWithOneParam;
import com.autel.common.CallbackWithOneParamProgress;
import com.autel.common.battery.cruiser.CruiserBatteryInfo;
import com.autel.common.camera.CameraProduct;
import com.autel.common.error.AutelError;
import com.autel.common.flycontroller.AutoSafeState;
import com.autel.common.flycontroller.FlightErrorState;
import com.autel.common.flycontroller.FlyMode;
import com.autel.common.flycontroller.ModelType;
import com.autel.common.flycontroller.SafeCheck;
import com.autel.common.flycontroller.cruiser.CruiserFlyControllerInfo;
import com.autel.common.mission.AutelCoordinate3D;
import com.autel.common.mission.AutelMission;
import com.autel.common.mission.RealTimeInfo;
import com.autel.common.mission.base.DirectionLatLng;
import com.autel.common.mission.base.DistanceModel;
import com.autel.common.mission.cruiser.CruiserWaypointFinishedAction;
import com.autel.common.mission.cruiser.CruiserWaypointMission;
import com.autel.common.product.AutelProductType;
import com.autel.common.remotecontroller.RemoteControllerInfo;
import com.autel.internal.sdk.mission.cruiser.CruiserWaypointRealTimeInfoImpl;
import com.autel.lib.enums.MissionConstant;
import com.autel.lib.jniHelper.NativeHelper;
import com.autel.lib.jniHelper.PathPlanningParameter;
import com.autel.lib.jniHelper.PathPlanningResult;
import com.autel.lib.jniHelper.SubMissionInfo;
import com.autel.sdk.battery.CruiserBattery;
import com.autel.sdk.flycontroller.CruiserFlyController;
import com.autel.sdk.mission.MissionManager;
import com.autel.sdk.product.BaseProduct;
import com.autel.sdk.remotecontroller.AutelRemoteController;
import com.autel.sdk10.utils.BytesUtils;
import com.autel.sdksample.PhoneBatteryManager;
import com.autel.sdksample.R;
import com.autel.sdksample.TestApplication;
import com.autel.sdksample.base.util.FileUtils;
import com.autel.sdksample.dragonfish.mission.enums.CameraActionType;
import com.autel.sdksample.dragonfish.mission.enums.DroneHeadingControl;
import com.autel.sdksample.dragonfish.mission.enums.MissionType;
import com.autel.sdksample.dragonfish.mission.enums.WaypointType;
import com.autel.sdksample.dragonfish.mission.model.BaseMissionModel;
import com.autel.sdksample.dragonfish.mission.model.CameraActionItem;
import com.autel.sdksample.dragonfish.mission.model.TaskModel;
import com.autel.sdksample.dragonfish.mission.model.WaypointMissionModel;
import com.autel.sdksample.dragonfish.mission.model.WaypointModel;
import com.autel.sdksample.dragonfish.rxrunnable.IOUiRunnable;
import com.autel.sdksample.utils.RealTimePathPlaningUtils;
import com.autel.util.log.AutelLog;
import com.autel.video.NetWorkProxyJni;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;


public class DFWayPointActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DFWayPointActivity";
    private CruiserWaypointMission autelMission;
    private CruiserFlyController mEvoFlyController;
    private FlyMode flyMode = FlyMode.UNKNOWN;
    private CruiserBattery battery;
    private MissionManager missionManager;
    private float lowBatteryPercent = 15f;
    private boolean isBatteryOk = false; //当前电量是否合适
    //    private boolean isCompassOk = false; //当前指南针状态是否OK
//    private boolean isImuOk = false; //当前IMU是否OK
//    private boolean isGpsOk = false; //当前gps是否OK
//    private boolean isImageTransOk = false; //当前图传信号是否OK
//    private boolean isCanTakeOff = false; //是否能起飞
    Button writeMissionTestData;
    Button testWaypoint;
    Button testMapping;

    private String filePath = FileUtils.getMissionFilePath() + "mission.aut";
    private boolean isDroneCheckFinish;
    private boolean isDroneOk;


    enum FlyState {
        Prepare, Start, Pause, None
    }

    private FlyState flyState = FlyState.None;

    private int id = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTitle("WayPoint");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evo2_waypoint);

        BaseProduct product = ((TestApplication) getApplicationContext()).getCurrentProduct();
        if (null != product
                && (product.getType() != AutelProductType.UNKNOWN)) {
            missionManager = product.getMissionManager();
            //设置任务执行时实时任务数据状态监听
            missionManager.setRealTimeInfoListener(new CallbackWithOneParam<RealTimeInfo>() {
                @Override
                public void onSuccess(RealTimeInfo realTimeInfo) {
                    CruiserWaypointRealTimeInfoImpl info = (CruiserWaypointRealTimeInfoImpl) realTimeInfo;
                    AutelLog.d("MissionRunning", "timeStamp:" + info.timeStamp + ",speed:" + info.speed + ",isArrived:" + info.isArrived +
                            ",isDirecting:" + info.isDirecting + ",waypointSequence:" + info.waypointSequence + ",actionSequence:" + info.actionSequence +
                            ",photoCount:" + info.photoCount + ",MissionExecuteState:" + info.executeState + ",missionID:" + info.missionID);
                }

                @Override
                public void onFailure(AutelError autelError) {

                }
            });

            battery = (CruiserBattery) product.getBattery();
            battery.getLowBatteryNotifyThreshold(new CallbackWithOneParam<Float>() {
                @Override
                public void onSuccess(Float aFloat) {
//                    lowBatteryPercent = aFloat;
                }

                @Override
                public void onFailure(AutelError autelError) {

                }
            });
            //监听飞机电池电量变化
            battery.setBatteryStateListener(new CallbackWithOneParam<CruiserBatteryInfo>() {
                @Override
                public void onSuccess(CruiserBatteryInfo batteryState) {
                    AutelLog.d(" batteryState uav battery-> " + batteryState.getRemainingPercent());
//                    isBatteryOk = batteryState.getRemainingPercent() > 15;
                }

                @Override
                public void onFailure(AutelError autelError) {

                }
            });
            //监听遥控器电量变化
            PhoneBatteryManager.getInstance().addBatteryChangeListener(TAG,
                    (current, total) -> {
                        AutelLog.debug_i(TAG, "pad battery-> " + current);
                        isBatteryOk = current > 15;
                    }
            );

            mEvoFlyController = (CruiserFlyController) product.getFlyController();
            mEvoFlyController.setFlyControllerInfoListener(new CallbackWithOneParam<CruiserFlyControllerInfo>() {

                @Override
                public void onSuccess(CruiserFlyControllerInfo evoFlyControllerInfo) {
                    flyMode = evoFlyControllerInfo.getFlyControllerStatus().getFlyMode();
//                    isCompassOk = evoFlyControllerInfo.getFlyControllerStatus().isCompassValid();
//                    isCanTakeOff = evoFlyControllerInfo.getFlyControllerStatus().isCanTakeOff();
//
//                    isImuOk = evoFlyControllerInfo.getFlyControllerStatus().isIMU0Valid() && evoFlyControllerInfo.getFlyControllerStatus().isIMU1Valid();
//
//                    isGpsOk = evoFlyControllerInfo.getFlyControllerStatus().isGpsValid();
                    AutelLog.d(TAG, "setFlyControllerInfoListener flyMode->" + flyMode);
                    if (null != evoFlyControllerInfo && null != evoFlyControllerInfo.getFlyControllerStatus()) {
                        SafeCheck safeCheck = evoFlyControllerInfo.getFlyControllerStatus().getSafeCheck();
                        //飞行器自检完成
                        if (safeCheck == SafeCheck.COMPLETE) {
                            if (!isDroneCheckFinish) {
                                isDroneCheckFinish = true;
                                //查询自检结果
                                getAutoCheckResult(ModelType.ALL);
                            }
                        }
                    }


                }

                @Override
                public void onFailure(AutelError autelError) {

                }
            });

        }
        writeMissionTestData = (Button) findViewById(R.id.writeMissionTestData);
        testWaypoint = (Button) findViewById(R.id.testWaypoint);
        testMapping = (Button) findViewById(R.id.testMapping);
        AutelLog.d("init missionManager" + missionManager);
        initView();
        initData();
    }

    private void getAutoCheckResult(ModelType modelType) {
        if (null != mEvoFlyController) {
            new IOUiRunnable<AutoSafeState>() {
                int retryCount = 0;

                @Override
                protected Observable<AutoSafeState> generateObservable() {
                    return mEvoFlyController.toRx().getAutoSafeCheck(modelType);
                }

                @Override
                public void onNext(@NonNull final AutoSafeState safeState) {
                    super.onNext(safeState);
                    isDroneOk = safeState.isLeftSteerNormal()
                            && safeState.isRightSteerNormal()
                            && safeState.isBehindSteerNormal()
                            && safeState.isAirSpeedNormal()
                            && safeState.isImu1Normal()
                            && safeState.isImu2Normal()
                            && (safeState.isGPSNormal() || safeState.isRTKNormal())
                            && safeState.isMagnetometer1normal()
                            && safeState.isMagnetometer2normal()
                            && safeState.isUltrasonicNormal()
                            && safeState.isBarometerNormal()
                            && safeState.isBatteryNormal()
                            && safeState.isGimbalNormal()
                            && safeState.isRemoteControllerNormal();//900M连接异常
                    AutelLog.debug_i(TAG, "showAutoCheckResult isBehindSteerNormal:" + safeState.isBehindSteerNormal()
                            + " isLeftSteerNormal " + safeState.isLeftSteerNormal()
                            + " isRightSteerNormal " + safeState.isRightSteerNormal()
                            + " isFontMotorNormal " + safeState.isFontMotorNormal()
                            + " isLeftMotorNormal " + safeState.isLeftMotorNormal()
                            + " isRightMotorNormal " + safeState.isRightMotorNormal()
                            + " isAirSpeedNormal " + safeState.isAirSpeedNormal()
                            + " isBarometerNormal " + safeState.isBarometerNormal()
                            + " isBatteryNormal " + safeState.isBatteryNormal()
                            + " isGimbalNormal " + safeState.isGimbalNormal()
                            + " isGPSNormal " + safeState.isGPSNormal()
                            + " isRTKNormal " + safeState.isRTKNormal()
                            + " isMagnetometer1normal " + safeState.isMagnetometer1normal()
                            + " isMagnetometer2normal " + safeState.isMagnetometer2normal()
                            + " isImu1Normal " + safeState.isImu1Normal()
                            + " isImu2Normal " + safeState.isImu2Normal()
                            + " isUltrasonicNormal " + safeState.isUltrasonicNormal()
                            + " isGPSNormal " + safeState.isGPSNormal()
                            + " isRTKNormal " + safeState.isRTKNormal()
//                + " isPayloadNormal " + safeState.isPayloadNormal()
                            + " isRemoteControllerNormal " + safeState.isRemoteControllerNormal()
//                            + " flightMode " + mRequestManager.getApplicationState().getFlightType()//需要先将遥控器档位拨至A档
                            + " isDroneOk " + isDroneOk);
                    Toast.makeText(getApplicationContext(), "autoCheck finish " + isDroneOk, Toast.LENGTH_LONG).show();

                }

                @Override
                public void onError(@NonNull Throwable e) {
                    super.onError(e);
                    retryCount++;
                    if (retryCount < 3) {
                        getAutoCheckResult(modelType);
                    } else {

                    }
                }
            }.execute();
        }
    }

    private void initView() {
        findViewById(R.id.prepare).setOnClickListener(this);
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.pause).setOnClickListener(this);
        findViewById(R.id.resume).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.download).setOnClickListener(this);
        findViewById(R.id.autoCheck).setOnClickListener(this);
        writeMissionTestData.setOnClickListener(v -> {

            File myDir = new File(FileUtils.getMissionFilePath());
            if (!myDir.exists()) {
                myDir.mkdirs();
            }


            String waypointMission = "{\n" +
                    "\t\"forceLandInfo\": [],\n" +
                    "\t\"introInfo\": {\n" +
                    "\t\t\"CycleMode\": 0,\n" +
                    "\t\t\"EndSubID\": 1,\n" +
                    "\t\t\"EndWPID\": 1.0,\n" +
                    "\t\t\"MaxVz\": 3.0,\n" +
                    "\t\t\"MinRadius\": 200.0,\n" +
                    "\t\t\"StartSubID\": 1,\n" +
                    "\t\t\"StartWPID\": 1.0,\n" +
                    "\t\t\"forceLandNum\": 0,\n" +
                    "\t\t\"initAlt\": 0.0,\n" +
                    "\t\t\"initLat\": 22.51400425525294,\n" +
                    "\t\t\"initLon\": 113.92571262532796,\n" +
                    "\t\t\"planningType\": 11,\n" +
                    "\t\t\"subMisNum\": 1\n" +
                    "\t},\n" +
                    "\t\"landInfo\": {\n" +
                    "\t\t\"altType\": 2,\n" +
                    "\t\t\"approachAlt\": 100.0,\n" +
                    "\t\t\"approachLat\": 22.514004534760666,\n" +
                    "\t\t\"approachLon\": 113.93263822751037,\n" +
                    "\t\t\"approachR\": 200.0,\n" +
                    "\t\t\"approachVel\": 20.0,\n" +
                    "\t\t\"homeAlt\": 0.0,\n" +
                    "\t\t\"homeAltType\": 2,\n" +
                    "\t\t\"homeLat\": 22.51400425525294,\n" +
                    "\t\t\"homeLon\": 113.92571262532796,\n" +
                    "\t\t\"transAlt\": 40.0\n" +
                    "\t},\n" +
                    "\t\"launchInfo\": {\n" +
                    "\t\t\"altType\": 2,\n" +
                    "\t\t\"departureAlt\": 100.0,\n" +
                    "\t\t\"departureLat\": 22.514004534760666,\n" +
                    "\t\t\"departureLon\": 113.91878702314557,\n" +
                    "\t\t\"departureR\": 200.0,\n" +
                    "\t\t\"departureVel\": 20.0,\n" +
                    "\t\t\"launchAlt\": 0.0,\n" +
                    "\t\t\"launchLat\": 22.51400425525294,\n" +
                    "\t\t\"launchLon\": 113.92571262532796,\n" +
                    "\t\t\"transAlt\": 40.0\n" +
                    "\t},\n" +
                    "\t\"missionInfo\": [{\n" +
                    "\t\t\"FinishMove\": 1,\n" +
                    "\t\t\"IANum\": 0,\n" +
                    "\t\t\"InterestArea\": [],\n" +
                    "\t\t\"LinkLostMove\": 2,\n" +
                    "\t\t\"WPNum\": 6,\n" +
                    "\t\t\"subMissionInfo\": {\n" +
                    "\t\t\t\"airLineDir\": 0.0,\n" +
                    "\t\t\t\"baseAlt\": 0.0,\n" +
                    "\t\t\t\"focalLength\": 25.6,\n" +
                    "\t\t\t\"overlapAlong\": 0,\n" +
                    "\t\t\t\"overlapSide\": 0,\n" +
                    "\t\t\t\"pixNumX\": 22489,\n" +
                    "\t\t\t\"pixNumY\": 12637,\n" +
                    "\t\t\t\"resolution\": 0.0,\n" +
                    "\t\t\t\"sensorOrient\": 1,\n" +
                    "\t\t\t\"sensorSizeX\": 35.983963,\n" +
                    "\t\t\t\"sensorSizeY\": 20.219847\n" +
                    "\t\t},\n" +
                    "\t\t\"subMissionType\": 1,\n" +
                    "\t\t\"wpInfo\": [{\n" +
                    "\t\t\t\"actionParam1\": 2.0,\n" +
                    "\t\t\t\"gimbalPitch\": -42.0,\n" +
                    "\t\t\t\"gimbalYaw\": 71.0,\n" +
                    "\t\t\t\"payloadAction\": 2,\n" +
                    "\t\t\t\"wpAlt\": 100.0,\n" +
                    "\t\t\t\"wpAltType\": 2,\n" +
                    "\t\t\t\"wpClimbMode\": 1,\n" +
                    "\t\t\t\"wpIndex\": 1,\n" +
                    "\t\t\t\"wpLat\": 22.517180545105873,\n" +
                    "\t\t\t\"wpLon\": 113.92888891518089,\n" +
                    "\t\t\t\"wpRadius\": 200.0,\n" +
                    "\t\t\t\"wpReserved1\": 0.0,\n" +
                    "\t\t\t\"wpTurnMode\": 2,\n" +
                    "\t\t\t\"wpTurnParam1\": 1.0,\n" +
                    "\t\t\t\"wpType\": 4,\n" +
                    "\t\t\t\"wpVel\": 20.0\n" +
                    "\t\t}, {\n" +
                    "\t\t\t\"actionParam1\": 30.0,\n" +
                    "\t\t\t\"gimbalPitch\": -31.0,\n" +
                    "\t\t\t\"gimbalYaw\": -59.0,\n" +
                    "\t\t\t\"payloadAction\": 3,\n" +
                    "\t\t\t\"wpAlt\": 100.0,\n" +
                    "\t\t\t\"wpAltType\": 2,\n" +
                    "\t\t\t\"wpClimbMode\": 1,\n" +
                    "\t\t\t\"wpIndex\": 2,\n" +
                    "\t\t\t\"wpLat\": 22.51922217962033,\n" +
                    "\t\t\t\"wpLon\": 113.92471539799595,\n" +
                    "\t\t\t\"wpRadius\": 200.0,\n" +
                    "\t\t\t\"wpReserved1\": 0.0,\n" +
                    "\t\t\t\"wpTurnMode\": 2,\n" +
                    "\t\t\t\"wpTurnParam1\": 1.0,\n" +
                    "\t\t\t\"wpType\": 4,\n" +
                    "\t\t\t\"wpVel\": 20.0\n" +
                    "\t\t}, {\n" +
                    "\t\t\t\"actionParam1\": 0.0,\n" +
                    "\t\t\t\"gimbalPitch\": -38.0,\n" +
                    "\t\t\t\"gimbalYaw\": 96.0,\n" +
                    "\t\t\t\"payloadAction\": 4,\n" +
                    "\t\t\t\"wpAlt\": 100.0,\n" +
                    "\t\t\t\"wpAltType\": 2,\n" +
                    "\t\t\t\"wpClimbMode\": 1,\n" +
                    "\t\t\t\"wpIndex\": 3,\n" +
                    "\t\t\t\"wpLat\": 22.519885273960526,\n" +
                    "\t\t\t\"wpLon\": 113.93204621923553,\n" +
                    "\t\t\t\"wpRadius\": 200.0,\n" +
                    "\t\t\t\"wpReserved1\": 0.0,\n" +
                    "\t\t\t\"wpTurnMode\": 2,\n" +
                    "\t\t\t\"wpTurnParam1\": 1.0,\n" +
                    "\t\t\t\"wpType\": 4,\n" +
                    "\t\t\t\"wpVel\": 20.0\n" +
                    "\t\t}, {\n" +
                    "\t\t\t\"actionParam1\": 0.0,\n" +
                    "\t\t\t\"gimbalPitch\": -42.0,\n" +
                    "\t\t\t\"gimbalYaw\": 20.0,\n" +
                    "\t\t\t\"payloadAction\": 0,\n" +
                    "\t\t\t\"wpAlt\": 100.0,\n" +
                    "\t\t\t\"wpAltType\": 2,\n" +
                    "\t\t\t\"wpClimbMode\": 1,\n" +
                    "\t\t\t\"wpIndex\": 4,\n" +
                    "\t\t\t\"wpLat\": 22.516633741529645,\n" +
                    "\t\t\t\"wpLon\": 113.93664236660521,\n" +
                    "\t\t\t\"wpRadius\": 200.0,\n" +
                    "\t\t\t\"wpReserved1\": 0.0,\n" +
                    "\t\t\t\"wpTurnMode\": 2,\n" +
                    "\t\t\t\"wpTurnParam1\": 1.0,\n" +
                    "\t\t\t\"wpType\": 4,\n" +
                    "\t\t\t\"wpVel\": 20.0\n" +
                    "\t\t}, {\n" +
                    "\t\t\t\"actionParam1\": 0.0,\n" +
                    "\t\t\t\"gimbalPitch\": -42.0,\n" +
                    "\t\t\t\"gimbalYaw\": 20.0,\n" +
                    "\t\t\t\"payloadAction\": 0,\n" +
                    "\t\t\t\"wpAlt\": 100.0,\n" +
                    "\t\t\t\"wpAltType\": 2,\n" +
                    "\t\t\t\"wpClimbMode\": 1,\n" +
                    "\t\t\t\"wpIndex\": 5,\n" +
                    "\t\t\t\"wpLat\": 22.513700066361963,\n" +
                    "\t\t\t\"wpLon\": 113.9360415517898,\n" +
                    "\t\t\t\"wpRadius\": 200.0,\n" +
                    "\t\t\t\"wpReserved1\": 0.0,\n" +
                    "\t\t\t\"wpTurnMode\": 4,\n" +
                    "\t\t\t\"wpTurnParam1\": 2.0,\n" +
                    "\t\t\t\"wpType\": 4,\n" +
                    "\t\t\t\"wpVel\": 20.0\n" +
                    "\t\t}, {\n" +
                    "\t\t\t\"actionParam1\": 0.0,\n" +
                    "\t\t\t\"gimbalPitch\": -42.0,\n" +
                    "\t\t\t\"gimbalYaw\": 20.0,\n" +
                    "\t\t\t\"payloadAction\": 0,\n" +
                    "\t\t\t\"wpAlt\": 100.0,\n" +
                    "\t\t\t\"wpAltType\": 2,\n" +
                    "\t\t\t\"wpClimbMode\": 1,\n" +
                    "\t\t\t\"wpIndex\": 6,\n" +
                    "\t\t\t\"wpLat\": 22.512379932020906,\n" +
                    "\t\t\t\"wpLon\": 113.94115085257494,\n" +
                    "\t\t\t\"wpRadius\": 200.0,\n" +
                    "\t\t\t\"wpReserved1\": 0.0,\n" +
                    "\t\t\t\"wpTurnMode\": 3,\n" +
                    "\t\t\t\"wpTurnParam1\": 10.0,\n" +
                    "\t\t\t\"wpType\": 4,\n" +
                    "\t\t\t\"wpVel\": 20.0\n" +
                    "\t\t}]\n" +
                    "\t}]\n" +
                    "}";
            PathPlanningParameter pathPlanningParameter = new Gson().fromJson(waypointMission, PathPlanningParameter.class);//getPathPlanningParameter(taskModel, droneLocation, subMissionInfo);

            //返回0表示成功，返回非0表示失败
            int res = NativeHelper.writeNewMissionFile(filePath, pathPlanningParameter);
            AutelLog.d("NativeHelper", " writeMissionFile result -> " + res);
            Toast.makeText(this, "writeMissionFile result -> " + res, Toast.LENGTH_SHORT).show();
        });

        testWaypoint.setOnClickListener(v -> {

            //测试数据 6个航点 前四个航点分别设置了相机动作定时拍照，定距拍照，录像，后三个都是无动作，第五个航点设置转弯模式为定圈盘旋，第六个航点设置转弯模式为定时盘旋
            //具体字段含义见工程目录下面"航线规划算法文档" <<“龙鱼”在线规划输入输出定义V6_8月15日>>
            String waypointMission = "{\n" +
                    "\t\"forceLandInfo\": [],\n" +
                    "\t\"introInfo\": {\n" +
                    "\t\t\"CycleMode\": 0,\n" +
                    "\t\t\"EndSubID\": 1,\n" +
                    "\t\t\"EndWPID\": 1.0,\n" +
                    "\t\t\"MaxVz\": 3.0,\n" +
                    "\t\t\"MinRadius\": 200.0,\n" +
                    "\t\t\"StartSubID\": 1,\n" +
                    "\t\t\"StartWPID\": 1.0,\n" +
                    "\t\t\"forceLandNum\": 0,\n" +
                    "\t\t\"initAlt\": 0.0,\n" +
                    "\t\t\"initLat\": 22.51400425525294,\n" +
                    "\t\t\"initLon\": 113.92571262532796,\n" +
                    "\t\t\"planningType\": 11,\n" +
                    "\t\t\"subMisNum\": 1\n" +
                    "\t},\n" +
                    "\t\"landInfo\": {\n" +
                    "\t\t\"altType\": 2,\n" +
                    "\t\t\"approachAlt\": 100.0,\n" +
                    "\t\t\"approachLat\": 22.514004534760666,\n" +
                    "\t\t\"approachLon\": 113.93263822751037,\n" +
                    "\t\t\"approachR\": 200.0,\n" +
                    "\t\t\"approachVel\": 20.0,\n" +
                    "\t\t\"homeAlt\": 0.0,\n" +
                    "\t\t\"homeAltType\": 2,\n" +
                    "\t\t\"homeLat\": 22.51400425525294,\n" +
                    "\t\t\"homeLon\": 113.92571262532796,\n" +
                    "\t\t\"transAlt\": 40.0\n" +
                    "\t},\n" +
                    "\t\"launchInfo\": {\n" +
                    "\t\t\"altType\": 2,\n" +
                    "\t\t\"departureAlt\": 100.0,\n" +
                    "\t\t\"departureLat\": 22.514004534760666,\n" +
                    "\t\t\"departureLon\": 113.91878702314557,\n" +
                    "\t\t\"departureR\": 200.0,\n" +
                    "\t\t\"departureVel\": 20.0,\n" +
                    "\t\t\"launchAlt\": 0.0,\n" +
                    "\t\t\"launchLat\": 22.51400425525294,\n" +
                    "\t\t\"launchLon\": 113.92571262532796,\n" +
                    "\t\t\"transAlt\": 40.0\n" +
                    "\t},\n" +
                    "\t\"missionInfo\": [{\n" +
                    "\t\t\"FinishMove\": 1,\n" +
                    "\t\t\"IANum\": 0,\n" +
                    "\t\t\"InterestArea\": [],\n" +
                    "\t\t\"LinkLostMove\": 2,\n" +
                    "\t\t\"WPNum\": 6,\n" +
                    "\t\t\"subMissionInfo\": {\n" +
                    "\t\t\t\"airLineDir\": 0.0,\n" +
                    "\t\t\t\"baseAlt\": 0.0,\n" +
                    "\t\t\t\"focalLength\": 25.6,\n" +
                    "\t\t\t\"overlapAlong\": 0,\n" +
                    "\t\t\t\"overlapSide\": 0,\n" +
                    "\t\t\t\"pixNumX\": 22489,\n" +
                    "\t\t\t\"pixNumY\": 12637,\n" +
                    "\t\t\t\"resolution\": 0.0,\n" +
                    "\t\t\t\"sensorOrient\": 1,\n" +
                    "\t\t\t\"sensorSizeX\": 35.983963,\n" +
                    "\t\t\t\"sensorSizeY\": 20.219847\n" +
                    "\t\t},\n" +
                    "\t\t\"subMissionType\": 1,\n" +
                    "\t\t\"wpInfo\": [{\n" +
                    "\t\t\t\"actionParam1\": 2.0,\n" +
                    "\t\t\t\"gimbalPitch\": -42.0,\n" +
                    "\t\t\t\"gimbalYaw\": 71.0,\n" +
                    "\t\t\t\"payloadAction\": 2,\n" +
                    "\t\t\t\"wpAlt\": 100.0,\n" +
                    "\t\t\t\"wpAltType\": 2,\n" +
                    "\t\t\t\"wpClimbMode\": 1,\n" +
                    "\t\t\t\"wpIndex\": 1,\n" +
                    "\t\t\t\"wpLat\": 22.517180545105873,\n" +
                    "\t\t\t\"wpLon\": 113.92888891518089,\n" +
                    "\t\t\t\"wpRadius\": 200.0,\n" +
                    "\t\t\t\"wpReserved1\": 0.0,\n" +
                    "\t\t\t\"wpTurnMode\": 2,\n" +
                    "\t\t\t\"wpTurnParam1\": 1.0,\n" +
                    "\t\t\t\"wpType\": 4,\n" +
                    "\t\t\t\"wpVel\": 20.0\n" +
                    "\t\t}, {\n" +
                    "\t\t\t\"actionParam1\": 30.0,\n" +
                    "\t\t\t\"gimbalPitch\": -31.0,\n" +
                    "\t\t\t\"gimbalYaw\": -59.0,\n" +
                    "\t\t\t\"payloadAction\": 3,\n" +
                    "\t\t\t\"wpAlt\": 100.0,\n" +
                    "\t\t\t\"wpAltType\": 2,\n" +
                    "\t\t\t\"wpClimbMode\": 1,\n" +
                    "\t\t\t\"wpIndex\": 2,\n" +
                    "\t\t\t\"wpLat\": 22.51922217962033,\n" +
                    "\t\t\t\"wpLon\": 113.92471539799595,\n" +
                    "\t\t\t\"wpRadius\": 200.0,\n" +
                    "\t\t\t\"wpReserved1\": 0.0,\n" +
                    "\t\t\t\"wpTurnMode\": 2,\n" +
                    "\t\t\t\"wpTurnParam1\": 1.0,\n" +
                    "\t\t\t\"wpType\": 4,\n" +
                    "\t\t\t\"wpVel\": 20.0\n" +
                    "\t\t}, {\n" +
                    "\t\t\t\"actionParam1\": 0.0,\n" +
                    "\t\t\t\"gimbalPitch\": -38.0,\n" +
                    "\t\t\t\"gimbalYaw\": 96.0,\n" +
                    "\t\t\t\"payloadAction\": 4,\n" +
                    "\t\t\t\"wpAlt\": 100.0,\n" +
                    "\t\t\t\"wpAltType\": 2,\n" +
                    "\t\t\t\"wpClimbMode\": 1,\n" +
                    "\t\t\t\"wpIndex\": 3,\n" +
                    "\t\t\t\"wpLat\": 22.519885273960526,\n" +
                    "\t\t\t\"wpLon\": 113.93204621923553,\n" +
                    "\t\t\t\"wpRadius\": 200.0,\n" +
                    "\t\t\t\"wpReserved1\": 0.0,\n" +
                    "\t\t\t\"wpTurnMode\": 2,\n" +
                    "\t\t\t\"wpTurnParam1\": 1.0,\n" +
                    "\t\t\t\"wpType\": 4,\n" +
                    "\t\t\t\"wpVel\": 20.0\n" +
                    "\t\t}, {\n" +
                    "\t\t\t\"actionParam1\": 0.0,\n" +
                    "\t\t\t\"gimbalPitch\": -42.0,\n" +
                    "\t\t\t\"gimbalYaw\": 20.0,\n" +
                    "\t\t\t\"payloadAction\": 0,\n" +
                    "\t\t\t\"wpAlt\": 100.0,\n" +
                    "\t\t\t\"wpAltType\": 2,\n" +
                    "\t\t\t\"wpClimbMode\": 1,\n" +
                    "\t\t\t\"wpIndex\": 4,\n" +
                    "\t\t\t\"wpLat\": 22.516633741529645,\n" +
                    "\t\t\t\"wpLon\": 113.93664236660521,\n" +
                    "\t\t\t\"wpRadius\": 200.0,\n" +
                    "\t\t\t\"wpReserved1\": 0.0,\n" +
                    "\t\t\t\"wpTurnMode\": 2,\n" +
                    "\t\t\t\"wpTurnParam1\": 1.0,\n" +
                    "\t\t\t\"wpType\": 4,\n" +
                    "\t\t\t\"wpVel\": 20.0\n" +
                    "\t\t}, {\n" +
                    "\t\t\t\"actionParam1\": 0.0,\n" +
                    "\t\t\t\"gimbalPitch\": -42.0,\n" +
                    "\t\t\t\"gimbalYaw\": 20.0,\n" +
                    "\t\t\t\"payloadAction\": 0,\n" +
                    "\t\t\t\"wpAlt\": 100.0,\n" +
                    "\t\t\t\"wpAltType\": 2,\n" +
                    "\t\t\t\"wpClimbMode\": 1,\n" +
                    "\t\t\t\"wpIndex\": 5,\n" +
                    "\t\t\t\"wpLat\": 22.513700066361963,\n" +
                    "\t\t\t\"wpLon\": 113.9360415517898,\n" +
                    "\t\t\t\"wpRadius\": 200.0,\n" +
                    "\t\t\t\"wpReserved1\": 0.0,\n" +
                    "\t\t\t\"wpTurnMode\": 4,\n" +
                    "\t\t\t\"wpTurnParam1\": 2.0,\n" +
                    "\t\t\t\"wpType\": 4,\n" +
                    "\t\t\t\"wpVel\": 20.0\n" +
                    "\t\t}, {\n" +
                    "\t\t\t\"actionParam1\": 0.0,\n" +
                    "\t\t\t\"gimbalPitch\": -42.0,\n" +
                    "\t\t\t\"gimbalYaw\": 20.0,\n" +
                    "\t\t\t\"payloadAction\": 0,\n" +
                    "\t\t\t\"wpAlt\": 100.0,\n" +
                    "\t\t\t\"wpAltType\": 2,\n" +
                    "\t\t\t\"wpClimbMode\": 1,\n" +
                    "\t\t\t\"wpIndex\": 6,\n" +
                    "\t\t\t\"wpLat\": 22.512379932020906,\n" +
                    "\t\t\t\"wpLon\": 113.94115085257494,\n" +
                    "\t\t\t\"wpRadius\": 200.0,\n" +
                    "\t\t\t\"wpReserved1\": 0.0,\n" +
                    "\t\t\t\"wpTurnMode\": 3,\n" +
                    "\t\t\t\"wpTurnParam1\": 10.0,\n" +
                    "\t\t\t\"wpType\": 4,\n" +
                    "\t\t\t\"wpVel\": 20.0\n" +
                    "\t\t}]\n" +
                    "\t}]\n" +
                    "}";

            PathPlanningResult pathPlanningResult = RealTimePathPlaningUtils.getMissionPath(waypointMission);
            Log.d(TAG, "testWaypoint: pathPlanningResult -> " + pathPlanningResult);
            Toast.makeText(this, "testWaypoint: pathPlanningResult -> " + pathPlanningResult, Toast.LENGTH_SHORT).show();

        });
        testMapping.setOnClickListener(v -> {
            //多边形测试数据，顶点个数必须大于等于 4 个
            //具体字段含义见工程目录下面"航线规划算法文档" <<“龙鱼”在线规划输入输出定义V6_8月15日>>
            String mappingJson = "{\n" +
                    "\t\"forceLandInfo\": [],\n" +
                    "\t\"introInfo\": {\n" +
                    "\t\t\"CycleMode\": 0,\n" +
                    "\t\t\"EndSubID\": 1,\n" +
                    "\t\t\"EndWPID\": 1.0,\n" +
                    "\t\t\"MaxVz\": 3.0,\n" +
                    "\t\t\"MinRadius\": 200.0,\n" +
                    "\t\t\"StartSubID\": 1,\n" +
                    "\t\t\"StartWPID\": 1.0,\n" +
                    "\t\t\"forceLandNum\": 0,\n" +
                    "\t\t\"initAlt\": 0.0,\n" +
                    "\t\t\"initLat\": 22.597084613515303,\n" +
                    "\t\t\"initLon\": 114.05184496791044,\n" +
                    "\t\t\"planningType\": 11,\n" +
                    "\t\t\"subMisNum\": 1\n" +
                    "\t},\n" +
                    "\t\"landInfo\": {\n" +
                    "\t\t\"altType\": 2,\n" +
                    "\t\t\"approachAlt\": 100.0,\n" +
                    "\t\t\"approachLat\": 22.597084893911017,\n" +
                    "\t\t\"approachLon\": 114.0587747424179,\n" +
                    "\t\t\"approachR\": 200.0,\n" +
                    "\t\t\"approachVel\": 19.820269,\n" +
                    "\t\t\"homeAlt\": 0.0,\n" +
                    "\t\t\"homeAltType\": 2,\n" +
                    "\t\t\"homeLat\": 22.597084613515303,\n" +
                    "\t\t\"homeLon\": 114.05184496791044,\n" +
                    "\t\t\"transAlt\": 40.0\n" +
                    "\t},\n" +
                    "\t\"launchInfo\": {\n" +
                    "\t\t\"altType\": 1,\n" +
                    "\t\t\"departureAlt\": 820.0,\n" +
                    "\t\t\"departureLat\": 22.597084893911017,\n" +
                    "\t\t\"departureLon\": 114.04491519340303,\n" +
                    "\t\t\"departureR\": 200.0,\n" +
                    "\t\t\"departureVel\": 19.820269,\n" +
                    "\t\t\"launchAlt\": 0.0,\n" +
                    "\t\t\"launchLat\": 22.597084613515303,\n" +
                    "\t\t\"launchLon\": 114.05184496791044,\n" +
                    "\t\t\"transAlt\": 40.0\n" +
                    "\t},\n" +
                    "\t\"missionInfo\": [{\n" +
                    "\t\t\"FinishMove\": 1,\n" +
                    "\t\t\"IANum\": 0,\n" +
                    "\t\t\"InterestArea\": [],\n" +
                    "\t\t\"LinkLostMove\": 2,\n" +
                    "\t\t\"WPNum\": 4,\n" +
                    "\t\t\"subMissionInfo\": {\n" +
                    "\t\t\t\"airLineDir\": 10.0,\n" +
                    "\t\t\t\"baseAlt\": 20.0,\n" +
                    "\t\t\t\"focalLength\": 25.6,\n" +
                    "\t\t\t\"overlapAlong\": 80,\n" +
                    "\t\t\t\"overlapSide\": 70,\n" +
                    "\t\t\t\"pixNumX\": 22489,\n" +
                    "\t\t\t\"pixNumY\": 12637,\n" +
                    "\t\t\t\"resolution\": 5.0,\n" +
                    "\t\t\t\"sensorOrient\": 1,\n" +
                    "\t\t\t\"sensorSizeX\": 35.983963,\n" +
                    "\t\t\t\"sensorSizeY\": 20.219847\n" +
                    "\t\t},\n" +
                    "\t\t\"subMissionType\": 2,\n" +
                    "\t\t\"wpInfo\": [{\n" +
                    "\t\t\t\"actionParam1\": 0.0,\n" +
                    "\t\t\t\"gimbalPitch\": -90.0,\n" +
                    "\t\t\t\"gimbalYaw\": 0.0,\n" +
                    "\t\t\t\"payloadAction\": 1,\n" +
                    "\t\t\t\"wpAlt\": 800.00006,\n" +
                    "\t\t\t\"wpAltType\": 2,\n" +
                    "\t\t\t\"wpClimbMode\": 1,\n" +
                    "\t\t\t\"wpIndex\": 1,\n" +
                    "\t\t\t\"wpLat\": 22.60661348307411,\n" +
                    "\t\t\t\"wpLon\": 114.0486686780575,\n" +
                    "\t\t\t\"wpRadius\": 200.0,\n" +
                    "\t\t\t\"wpReserved1\": 0.0,\n" +
                    "\t\t\t\"wpTurnMode\": 2,\n" +
                    "\t\t\t\"wpTurnParam1\": 0.0,\n" +
                    "\t\t\t\"wpType\": 5,\n" +
                    "\t\t\t\"wpVel\": 19.820269\n" +
                    "\t\t}, {\n" +
                    "\t\t\t\"actionParam1\": 0.0,\n" +
                    "\t\t\t\"gimbalPitch\": -90.0,\n" +
                    "\t\t\t\"gimbalYaw\": 0.0,\n" +
                    "\t\t\t\"payloadAction\": 1,\n" +
                    "\t\t\t\"wpAlt\": 800.00006,\n" +
                    "\t\t\t\"wpAltType\": 2,\n" +
                    "\t\t\t\"wpClimbMode\": 1,\n" +
                    "\t\t\t\"wpIndex\": 2,\n" +
                    "\t\t\t\"wpLat\": 22.60661348307411,\n" +
                    "\t\t\t\"wpLon\": 114.06137383746925,\n" +
                    "\t\t\t\"wpRadius\": 200.0,\n" +
                    "\t\t\t\"wpReserved1\": 0.0,\n" +
                    "\t\t\t\"wpTurnMode\": 2,\n" +
                    "\t\t\t\"wpTurnParam1\": 0.0,\n" +
                    "\t\t\t\"wpType\": 5,\n" +
                    "\t\t\t\"wpVel\": 19.820269\n" +
                    "\t\t}, {\n" +
                    "\t\t\t\"actionParam1\": 0.0,\n" +
                    "\t\t\t\"gimbalPitch\": -90.0,\n" +
                    "\t\t\t\"gimbalYaw\": 0.0,\n" +
                    "\t\t\t\"payloadAction\": 1,\n" +
                    "\t\t\t\"wpAlt\": 800.00006,\n" +
                    "\t\t\t\"wpAltType\": 2,\n" +
                    "\t\t\t\"wpClimbMode\": 1,\n" +
                    "\t\t\t\"wpIndex\": 3,\n" +
                    "\t\t\t\"wpLat\": 22.593908323662365,\n" +
                    "\t\t\t\"wpLon\": 114.06137383746925,\n" +
                    "\t\t\t\"wpRadius\": 200.0,\n" +
                    "\t\t\t\"wpReserved1\": 0.0,\n" +
                    "\t\t\t\"wpTurnMode\": 2,\n" +
                    "\t\t\t\"wpTurnParam1\": 0.0,\n" +
                    "\t\t\t\"wpType\": 5,\n" +
                    "\t\t\t\"wpVel\": 19.820269\n" +
                    "\t\t}, {\n" +
                    "\t\t\t\"actionParam1\": 0.0,\n" +
                    "\t\t\t\"gimbalPitch\": -90.0,\n" +
                    "\t\t\t\"gimbalYaw\": 0.0,\n" +
                    "\t\t\t\"payloadAction\": 1,\n" +
                    "\t\t\t\"wpAlt\": 800.00006,\n" +
                    "\t\t\t\"wpAltType\": 2,\n" +
                    "\t\t\t\"wpClimbMode\": 1,\n" +
                    "\t\t\t\"wpIndex\": 4,\n" +
                    "\t\t\t\"wpLat\": 22.593908323662365,\n" +
                    "\t\t\t\"wpLon\": 114.0486686780575,\n" +
                    "\t\t\t\"wpRadius\": 200.0,\n" +
                    "\t\t\t\"wpReserved1\": 0.0,\n" +
                    "\t\t\t\"wpTurnMode\": 2,\n" +
                    "\t\t\t\"wpTurnParam1\": 0.0,\n" +
                    "\t\t\t\"wpType\": 5,\n" +
                    "\t\t\t\"wpVel\": 19.820269\n" +
                    "\t\t}]\n" +
                    "\t}]\n" +
                    "}";
            PathPlanningResult pathPlanningResult = RealTimePathPlaningUtils.getMissionPath(mappingJson);
            Log.d(TAG, "testMapping: pathPlanningResult -> " + pathPlanningResult);
            Toast.makeText(this, "testMapping: pathPlanningResult -> " + pathPlanningResult, Toast.LENGTH_SHORT).show();


        });
    }

    public void testGoHomeMission(View view) {
        //返航任务测试数据
        String goHomeJson = "{\n" +
                "\t\"forceLandInfo\": [],\n" +
                "\t\"introInfo\": {\n" +
                "\t\t\"CycleMode\": 0,\n" +
                "\t\t\"EndSubID\": 1,\n" +
                "\t\t\"EndWPID\": 1.0,\n" +
                "\t\t\"MaxVz\": 3.0,\n" +
                "\t\t\"MinRadius\": 138.0,\n" +
                "\t\t\"StartSubID\": 1,\n" +
                "\t\t\"StartWPID\": 1.0,\n" +
                "\t\t\"forceLandNum\": 0,\n" +
                "\t\t\"initAlt\": 0.0,\n" +
                "\t\t\"initLat\": 22.587813884666033,\n" +
                "\t\t\"initLon\": 114.0177957652308,\n" +
                "\t\t\"planningType\": 2,\n" +
                "\t\t\"subMisNum\": 0\n" +
                "\t},\n" +
                "\t\"landInfo\": {\n" +
                "\t\t\"altType\": 1,\n" +
                "\t\t\"approachAlt\": 100.0,\n" +
                "\t\t\"approachLat\": 22.584020803787475,\n" +
                "\t\t\"approachLon\": 114.01358436944409,\n" +
                "\t\t\"approachR\": 138.0,\n" +
                "\t\t\"approachVel\": 20.0,\n" +
                "\t\t\"homeAlt\": 0.0,\n" +
                "\t\t\"homeAltType\": 2,\n" +
                "\t\t\"homeLat\": 22.587813884666033,\n" +
                "\t\t\"homeLon\": 114.0177957652308,\n" +
                "\t\t\"transAlt\": 100.0\n" +
                "\t},\n" +
                "\t\"launchInfo\": {\n" +
                "\t\t\"altType\": 0,\n" +
                "\t\t\"departureAlt\": 0.0,\n" +
                "\t\t\"departureLat\": 0.0,\n" +
                "\t\t\"departureLon\": 0.0,\n" +
                "\t\t\"departureR\": 0.0,\n" +
                "\t\t\"departureVel\": 0.0,\n" +
                "\t\t\"launchAlt\": 0.0,\n" +
                "\t\t\"launchLat\": 0.0,\n" +
                "\t\t\"launchLon\": 0.0,\n" +
                "\t\t\"transAlt\": 0.0\n" +
                "\t},\n" +
                "\t\"missionInfo\": []\n" +
                "}";
        PathPlanningResult pathPlanningResult = RealTimePathPlaningUtils.getMissionPath(goHomeJson);
        Log.d(TAG, "testGoHomeMission: pathPlanningResult -> " + pathPlanningResult);
        Toast.makeText(this, "testGoHomeMission: pathPlanningResult -> " + pathPlanningResult, Toast.LENGTH_SHORT).show();

    }
    public void testLand(View view) {
        String onLineJson = "{\n" +
                "\t\"forceLandInfo\": [],\n" +
                "\t\"introInfo\": {\n" +
                "\t\t\"CycleMode\": 0,\n" +
                "\t\t\"EndSubID\": 1,\n" +
                "\t\t\"EndWPID\": 1.0,\n" +
                "\t\t\"MaxVz\": 3.0,\n" +
                "\t\t\"MinRadius\": 138.0,\n" +
                "\t\t\"StartSubID\": 1,\n" +
                "\t\t\"StartWPID\": 1.0,\n" +
                "\t\t\"forceLandNum\": 0,\n" +
                "\t\t\"initAlt\": 10.0,\n" +
                "\t\t\"initLat\": 22.601954185582244,\n" +
                "\t\t\"initLon\": 114.00908924274069,\n" +
                "\t\t\"planningType\": 2,\n" +
                "\t\t\"subMisNum\": 0\n" +
                "\t},\n" +
                "\t\"landInfo\": {\n" +
                "\t\t\"altType\": 1,\n" +
                "\t\t\"approachAlt\": 100.0,\n" +
                "\t\t\"approachLat\": 22.595354103352392,\n" +
                "\t\t\"approachLon\": 114.00598790081068,\n" +
                "\t\t\"approachR\": 138.0,\n" +
                "\t\t\"approachVel\": 20.0,\n" +
                "\t\t\"homeAlt\": 10.0,\n" +
                "\t\t\"homeAltType\": 2,\n" +
                "\t\t\"homeLat\": 22.601954185582244,\n" +
                "\t\t\"homeLon\": 114.00908924274069,\n" +
                "\t\t\"transAlt\": 100.0\n" +
                "\t},\n" +
                "\t\"launchInfo\": {\n" +
                "\t\t\"altType\": 0,\n" +
                "\t\t\"departureAlt\": 0.0,\n" +
                "\t\t\"departureLat\": 0.0,\n" +
                "\t\t\"departureLon\": 0.0,\n" +
                "\t\t\"departureR\": 0.0,\n" +
                "\t\t\"departureVel\": 0.0,\n" +
                "\t\t\"launchAlt\": 0.0,\n" +
                "\t\t\"launchLat\": 0.0,\n" +
                "\t\t\"launchLon\": 0.0,\n" +
                "\t\t\"transAlt\": 0.0\n" +
                "\t},\n" +
                "\t\"missionInfo\": []\n" +
                "}";
        PathPlanningResult pathPlanningResult = RealTimePathPlaningUtils.getMissionPath(onLineJson);
        Log.d(TAG, "onLineJson: pathPlanningResult -> " + pathPlanningResult);
        Toast.makeText(this, "onLineJson: pathPlanningResult -> " + pathPlanningResult, Toast.LENGTH_SHORT).show();

    }

    public void onLine8QuickMission(View view) {
        String QuickJson = "{\n" +
                "\t\"forceLandInfo\": [],\n" +
                "\t\"introInfo\": {\n" +
                "\t\t\"CycleMode\": 0,\n" +
                "\t\t\"EndSubID\": 1,\n" +
                "\t\t\"EndWPID\": 1.0,\n" +
                "\t\t\"MaxVz\": 3.0,\n" +
                "\t\t\"MinRadius\": 138.0,\n" +
                "\t\t\"StartSubID\": 1,\n" +
                "\t\t\"StartWPID\": 1.0,\n" +
                "\t\t\"forceLandNum\": 0,\n" +
                "\t\t\"initAlt\": 10.0,\n" +
                "\t\t\"initLat\": 22.601954185582244,\n" +
                "\t\t\"initLon\": 114.00908924274069,\n" +
                "\t\t\"planningType\": 2,\n" +
                "\t\t\"subMisNum\": 0\n" +
                "\t},\n" +
                "\t\"landInfo\": {\n" +
                "\t\t\"altType\": 1,\n" +
                "\t\t\"approachAlt\": 100.0,\n" +
                "\t\t\"approachLat\": 22.595354103352392,\n" +
                "\t\t\"approachLon\": 114.00598790081068,\n" +
                "\t\t\"approachR\": 138.0,\n" +
                "\t\t\"approachVel\": 20.0,\n" +
                "\t\t\"homeAlt\": 10.0,\n" +
                "\t\t\"homeAltType\": 2,\n" +
                "\t\t\"homeLat\": 22.601954185582244,\n" +
                "\t\t\"homeLon\": 114.00908924274069,\n" +
                "\t\t\"transAlt\": 100.0\n" +
                "\t},\n" +
                "\t\"launchInfo\": {\n" +
                "\t\t\"altType\": 0,\n" +
                "\t\t\"departureAlt\": 0.0,\n" +
                "\t\t\"departureLat\": 0.0,\n" +
                "\t\t\"departureLon\": 0.0,\n" +
                "\t\t\"departureR\": 0.0,\n" +
                "\t\t\"departureVel\": 0.0,\n" +
                "\t\t\"launchAlt\": 0.0,\n" +
                "\t\t\"launchLat\": 0.0,\n" +
                "\t\t\"launchLon\": 0.0,\n" +
                "\t\t\"transAlt\": 0.0\n" +
                "\t},\n" +
                "\t\"missionInfo\": []\n" +
                "}";
        PathPlanningResult pathPlanningResult = RealTimePathPlaningUtils.getMissionPath(QuickJson);
        Log.d(TAG, "QuickJson: pathPlanningResult -> " + pathPlanningResult);
        Toast.makeText(this, "QuickJson: pathPlanningResult -> " + pathPlanningResult, Toast.LENGTH_SHORT).show();

    }

    public void onLineMission(View view) {
        //在线任务规划测试数据
        String landJson = "{\n" +
                "\t\"forceLandInfo\": [],\n" +
                "\t\"introInfo\": {\n" +
                "\t\t\"CycleMode\": 0,\n" +
                "\t\t\"EndSubID\": 1,\n" +
                "\t\t\"EndWPID\": 1.0,\n" +
                "\t\t\"MaxVz\": 3.0,\n" +
                "\t\t\"MinRadius\": 138.0,\n" +
                "\t\t\"StartSubID\": 1,\n" +
                "\t\t\"StartWPID\": 1.0,\n" +
                "\t\t\"forceLandNum\": 0,\n" +
                "\t\t\"initAlt\": 15.05,\n" +
                "\t\t\"initLat\": 22.5988179,\n" +
                "\t\t\"initLon\": 113.998746,\n" +
                "\t\t\"planningType\": 8,\n" +
                "\t\t\"subMisNum\": 1\n" +
                "\t},\n" +
                "\t\"landInfo\": {\n" +
                "\t\t\"altType\": 2,\n" +
                "\t\t\"approachAlt\": 100.0,\n" +
                "\t\t\"approachLat\": 0.0,\n" +
                "\t\t\"approachLon\": 0.0,\n" +
                "\t\t\"approachR\": 200.0,\n" +
                "\t\t\"approachVel\": 20.0,\n" +
                "\t\t\"homeAlt\": 0.0,\n" +
                "\t\t\"homeAltType\": 2,\n" +
                "\t\t\"homeLat\": 22.5988179,\n" +
                "\t\t\"homeLon\": 113.998746,\n" +
                "\t\t\"transAlt\": 40.0\n" +
                "\t},\n" +
                "\t\"launchInfo\": {\n" +
                "\t\t\"altType\": 2,\n" +
                "\t\t\"departureAlt\": 99.0,\n" +
                "\t\t\"departureLat\": 0.0,\n" +
                "\t\t\"departureLon\": 0.0,\n" +
                "\t\t\"departureR\": 200.0,\n" +
                "\t\t\"departureVel\": 20.0,\n" +
                "\t\t\"launchAlt\": 15.0,\n" +
                "\t\t\"launchLat\": 22.5988179,\n" +
                "\t\t\"launchLon\": 113.998746,\n" +
                "\t\t\"transAlt\": 40.0\n" +
                "\t},\n" +
                "\t\"missionInfo\": [{\n" +
                "\t\t\"FinishMove\": 1,\n" +
                "\t\t\"IANum\": 0,\n" +
                "\t\t\"InterestArea\": [],\n" +
                "\t\t\"LinkLostMove\": 2,\n" +
                "\t\t\"WPNum\": 4,\n" +
                "\t\t\"subMissionInfo\": {\n" +
                "\t\t\t\"airLineDir\": 0.0,\n" +
                "\t\t\t\"baseAlt\": 0.0,\n" +
                "\t\t\t\"focalLength\": 25.6,\n" +
                "\t\t\t\"overlapAlong\": 0,\n" +
                "\t\t\t\"overlapSide\": 0,\n" +
                "\t\t\t\"pixNumX\": 22489,\n" +
                "\t\t\t\"pixNumY\": 12637,\n" +
                "\t\t\t\"resolution\": 0.0,\n" +
                "\t\t\t\"sensorOrient\": 1,\n" +
                "\t\t\t\"sensorSizeX\": 35.983963,\n" +
                "\t\t\t\"sensorSizeY\": 20.219847\n" +
                "\t\t},\n" +
                "\t\t\"subMissionType\": 1,\n" +
                "\t\t\"wpInfo\": [{\n" +
                "\t\t\t\"actionParam1\": 0.0,\n" +
                "\t\t\t\"gimbalPitch\": 0.0,\n" +
                "\t\t\t\"gimbalYaw\": 0.0,\n" +
                "\t\t\t\"payloadAction\": 0,\n" +
                "\t\t\t\"wpAlt\": 99.0,\n" +
                "\t\t\t\"wpAltType\": 2,\n" +
                "\t\t\t\"wpClimbMode\": 1,\n" +
                "\t\t\t\"wpIndex\": 1,\n" +
                "\t\t\t\"wpLat\": 22.58452011970654,\n" +
                "\t\t\t\"wpLon\": 114.00103928728367,\n" +
                "\t\t\t\"wpRadius\": 138.0,\n" +
                "\t\t\t\"wpReserved1\": 0.0,\n" +
                "\t\t\t\"wpTurnMode\": 2,\n" +
                "\t\t\t\"wpTurnParam1\": 1.0,\n" +
                "\t\t\t\"wpType\": 4,\n" +
                "\t\t\t\"wpVel\": 20.0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"actionParam1\": 0.0,\n" +
                "\t\t\t\"gimbalPitch\": 0.0,\n" +
                "\t\t\t\"gimbalYaw\": 0.0,\n" +
                "\t\t\t\"payloadAction\": 0,\n" +
                "\t\t\t\"wpAlt\": 99.0,\n" +
                "\t\t\t\"wpAltType\": 2,\n" +
                "\t\t\t\"wpClimbMode\": 1,\n" +
                "\t\t\t\"wpIndex\": 2,\n" +
                "\t\t\t\"wpLat\": 22.587112365237203,\n" +
                "\t\t\t\"wpLon\": 114.00751655967525,\n" +
                "\t\t\t\"wpRadius\": 138.0,\n" +
                "\t\t\t\"wpReserved1\": 0.0,\n" +
                "\t\t\t\"wpTurnMode\": 2,\n" +
                "\t\t\t\"wpTurnParam1\": 1.0,\n" +
                "\t\t\t\"wpType\": 4,\n" +
                "\t\t\t\"wpVel\": 20.0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"actionParam1\": 0.0,\n" +
                "\t\t\t\"gimbalPitch\": 0.0,\n" +
                "\t\t\t\"gimbalYaw\": 0.0,\n" +
                "\t\t\t\"payloadAction\": 0,\n" +
                "\t\t\t\"wpAlt\": 99.0,\n" +
                "\t\t\t\"wpAltType\": 2,\n" +
                "\t\t\t\"wpClimbMode\": 1,\n" +
                "\t\t\t\"wpIndex\": 3,\n" +
                "\t\t\t\"wpLat\": 22.591007067097564,\n" +
                "\t\t\t\"wpLon\": 114.012666509604,\n" +
                "\t\t\t\"wpRadius\": 138.0,\n" +
                "\t\t\t\"wpReserved1\": 0.0,\n" +
                "\t\t\t\"wpTurnMode\": 2,\n" +
                "\t\t\t\"wpTurnParam1\": 1.0,\n" +
                "\t\t\t\"wpType\": 4,\n" +
                "\t\t\t\"wpVel\": 20.0\n" +
                "\t\t}, {\n" +
                "\t\t\t\"actionParam1\": 0.0,\n" +
                "\t\t\t\"gimbalPitch\": 0.0,\n" +
                "\t\t\t\"gimbalYaw\": 0.0,\n" +
                "\t\t\t\"payloadAction\": 0,\n" +
                "\t\t\t\"wpAlt\": 99.0,\n" +
                "\t\t\t\"wpAltType\": 2,\n" +
                "\t\t\t\"wpClimbMode\": 1,\n" +
                "\t\t\t\"wpIndex\": 4,\n" +
                "\t\t\t\"wpLat\": 22.595771077158858,\n" +
                "\t\t\t\"wpLon\": 114.01179980991151,\n" +
                "\t\t\t\"wpRadius\": 138.0,\n" +
                "\t\t\t\"wpReserved1\": 0.0,\n" +
                "\t\t\t\"wpTurnMode\": 2,\n" +
                "\t\t\t\"wpTurnParam1\": 1.0,\n" +
                "\t\t\t\"wpType\": 4,\n" +
                "\t\t\t\"wpVel\": 20.0\n" +
                "\t\t}]\n" +
                "\t}]\n" +
                "}";
        PathPlanningResult pathPlanningResult = RealTimePathPlaningUtils.getMissionPath(landJson);
        Log.d(TAG, "onLineMission: pathPlanningResult -> " + pathPlanningResult);
        Toast.makeText(this, "onLineMission: pathPlanningResult -> " + pathPlanningResult, Toast.LENGTH_SHORT).show();

    }


    private void initData() {
        autelMission = new CruiserWaypointMission();

        autelMission.missionId = BytesUtils.getInt(UUID.randomUUID().toString().replace("-", "").getBytes()); //任务id
        autelMission.missionType = com.autel.common.mission.MissionType.Waypoint; //任务类型(Waypoint(航点)、RECTANGLE(矩形)、POLYGON(多边形))
        autelMission.finishedAction = CruiserWaypointFinishedAction.RETURN_HOME;
    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        switch (id) {
            case R.id.autoCheck: {
                //飞行之前，必须进行必要的飞行检查，自检结果会在自检完成后通过 getAutoCheckResult()查询
                AutelLog.debug_i(TAG, "autoCheck start ");
                autoCheck(ModelType.ALL);
            }
            break;
            case R.id.prepare: {
                if (!isDroneOk) {
                    Toast.makeText(DFWayPointActivity.this, "飞行器故障，不能执行", Toast.LENGTH_LONG).show();
                    return;
                }

                doPrepare();
            }
            break;

            case R.id.start: {
                if (flyState != FlyState.Prepare) {
                    Toast.makeText(DFWayPointActivity.this, "当前状态，不能执行", Toast.LENGTH_LONG).show();
                    return;
                }
                if (null != missionManager) {
                    missionManager.startMission(new CallbackWithOneParam<Pair<Boolean, FlightErrorState>>() {
                        @Override
                        public void onSuccess(Pair<Boolean, FlightErrorState> booleanFlightErrorStatePair) {
                            flyState = FlyState.Start;
                            //booleanFlightErrorStatePair.first == true 说明可以安全起飞
                            Toast.makeText(DFWayPointActivity.this, "start result " + booleanFlightErrorStatePair.first, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(AutelError autelError) {

                        }
                    });

                }
            }
            break;

            case R.id.pause: {
                if (flyState != FlyState.Start) {
                    Toast.makeText(DFWayPointActivity.this, "当前状态，不能执行", Toast.LENGTH_LONG).show();
                    return;
                }
                if (null != missionManager) {
                    missionManager.pauseMission(new CallbackWithNoParam() {
                        @Override
                        public void onSuccess() {
                            flyState = FlyState.Pause;
                            Toast.makeText(DFWayPointActivity.this, "pause success", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(AutelError autelError) {

                        }
                    });
                }
            }
            break;

            case R.id.resume: {
                if (flyState != FlyState.Pause) {
                    Toast.makeText(DFWayPointActivity.this, "当前状态，不能执行", Toast.LENGTH_LONG).show();
                    return;
                }
                if (null != missionManager) {
                    missionManager.resumeMission(new CallbackWithNoParam() {
                        @Override
                        public void onSuccess() {
                            flyState = FlyState.Start;
                            Toast.makeText(DFWayPointActivity.this, "continue success", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(AutelError autelError) {

                        }
                    });
                }
            }
            break;

            case R.id.cancel: {
                if (flyState == FlyState.None) {
                    Toast.makeText(DFWayPointActivity.this, "当前状态，不能执行", Toast.LENGTH_LONG).show();
                    return;
                }
                if (null != missionManager) {
                    missionManager.cancelMission(new CallbackWithNoParam() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(DFWayPointActivity.this, "cancel success", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(AutelError autelError) {

                        }
                    });
                }
            }
            break;

            case R.id.download: {
                if (flyState == FlyState.None) {
                    Toast.makeText(DFWayPointActivity.this, "当前状态，不能执行", Toast.LENGTH_LONG).show();
                    return;
                }
                if (null != missionManager) {
                    missionManager.downloadMission(new CallbackWithOneParamProgress<AutelMission>() {
                        @Override
                        public void onProgress(float v) {

                        }

                        @Override
                        public void onSuccess(AutelMission autelMission) {
                            Toast.makeText(DFWayPointActivity.this, "download success", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(AutelError autelError) {

                        }
                    });
                }
            }
            break;
        }
    }

    public void autoCheck(final ModelType modelType) {
        isDroneCheckFinish = false;
        isDroneOk = false;
        if (null != mEvoFlyController) {
            new IOUiRunnable<Boolean>() {
                @Override
                protected Observable<Boolean> generateObservable() {
                    return mEvoFlyController.toRx().autoSafeCheck(modelType);
                }

                @Override
                public void onNext(@NonNull final Boolean success) {
                    super.onNext(success);


                }

                @Override
                public void onError(@NonNull Throwable e) {
                    super.onError(e);

                }
            }.execute();
        }
    }

    private void doPrepare() {
        if (flyMode != FlyMode.DISARM) {
            Toast.makeText(DFWayPointActivity.this, "当前飞行模式，不能执行", Toast.LENGTH_LONG).show();
            return;
        }
        if (null != missionManager) {
            autelMission.localMissionFilePath = filePath;
            missionManager.prepareMission(autelMission, new CallbackWithOneParamProgress<Boolean>() {
                @Override
                public void onProgress(float v) {
                    AutelLog.d(TAG, " prepareMission onProgress " + v);
                }

                @Override
                public void onSuccess(Boolean aBoolean) {
                    flyState = FlyState.Prepare;
                    AutelLog.d("prepareMission success");
                    Toast.makeText(DFWayPointActivity.this, "prepare success", Toast.LENGTH_LONG).show();


                }

                @Override
                public void onFailure(AutelError autelError) {
                    AutelLog.d("prepareMission onFailure");
                    Toast.makeText(DFWayPointActivity.this, "prepare failed", Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    private boolean flyCheck() {

//        if (!isBatteryOk) {
//            Toast.makeText(DFWayPointActivity.this, "当前电池电量不足", Toast.LENGTH_LONG).show();
//            return false;
//        }
//
//        if (!isImuOk) {
//            Toast.makeText(DFWayPointActivity.this, "IMU异常", Toast.LENGTH_LONG).show();
//            return false;
//        }
//
//        if (!isGpsOk) {
//            Toast.makeText(DFWayPointActivity.this, "GPS异常", Toast.LENGTH_LONG).show();
//            return false;
//        }
//
//        if (!isCompassOk) {
//            Toast.makeText(DFWayPointActivity.this, "指南针异常", Toast.LENGTH_LONG).show();
//            return false;
//        }
//
//        if (!isImageTransOk) {
//            Toast.makeText(DFWayPointActivity.this, "图传信号异常", Toast.LENGTH_LONG).show();
//            return false;
//        }
//        if (!isCanTakeOff) {
//            Toast.makeText(DFWayPointActivity.this, "飞行器不能起飞", Toast.LENGTH_LONG).show();
//            return false;
//        }

        return true;
    }

    /**
     * 任务类型1：自由航点航线 2：区域测绘航线 3：八字盘旋航线
     *
     * @param focalLength
     * @param sensorSizeX
     * @param sensorSizeY
     * @param pixel
     * @return
     */
    private static SubMissionInfo getSubMissionInfo(float focalLength, float sensorSizeX, float sensorSizeY, float pixel) {
        SubMissionInfo subMissionInfo = new SubMissionInfo();
        subMissionInfo.setFocalLength(focalLength);
        subMissionInfo.setSensorSizeX(sensorSizeX);
        subMissionInfo.setSensorSizeY(sensorSizeY);
        subMissionInfo.setPixNumX((int) (sensorSizeX / pixel * 1000));
        subMissionInfo.setPixNumY((int) (sensorSizeY / pixel * 1000));
        return subMissionInfo;
    }


    public float getCameraFocalLength() {
        //需要判断相机类型 如果是XL718 为24
//        if (mApplicationState.getCameraState().getCameraProduct() == CameraProduct.XL718) {
//            return 24;
//        }
        return 25.6f;
    }

    public float getSensorSizeX() {
        //40为HFOV 水平视角,需要从相机心跳里面实时获取到具体数据
        return (float) (getCameraFocalLength() * Math.tan(40 * Math.PI / 360) * 2);
    }

    public float getSensorSizeY() {
        //65.8 VFOV 垂直视角,需要从相机心跳里面实时获取到具体数据
        return (float) (getCameraFocalLength() * Math.tan(65.8 * Math.PI / 360) * 2);
    }

    public float getPixel() {
        //需要判断相机类型 根据不同的相机类型返回不通的像素大小
//        if (mApplicationState.getCameraState().getMainCameraProduct() == CameraProduct.XT706) {
//            return PIXEL_SIZE_XT706;
//        } else if (mApplicationState.getCameraState().getMainCameraProduct() == CameraProduct.XT708) {
//            return PIXEL_SIZE_XT708;
//        } else if (mApplicationState.getCameraState().getMainCameraProduct() == CameraProduct.XT709) {
//            return PIXEL_SIZE_XT709;
//        }
        return PIXEL_SIZE_XT701;
    }

}
