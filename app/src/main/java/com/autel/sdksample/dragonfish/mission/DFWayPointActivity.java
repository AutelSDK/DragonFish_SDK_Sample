package com.autel.sdksample.dragonfish.mission;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.autel.common.CallbackWithNoParam;
import com.autel.common.CallbackWithOneParam;
import com.autel.common.CallbackWithOneParamProgress;
import com.autel.common.battery.cruiser.CruiserBatteryInfo;
import com.autel.common.error.AutelError;
import com.autel.common.flycontroller.AutoSafeState;
import com.autel.common.flycontroller.FlightErrorState;
import com.autel.common.flycontroller.ModelType;
import com.autel.common.flycontroller.SafeCheck;
import com.autel.common.flycontroller.cruiser.CruiserFlyControllerInfo;
import com.autel.common.mission.AutelCoordinate3D;
import com.autel.common.mission.AutelMission;
import com.autel.common.mission.MissionType;
import com.autel.common.mission.RealTimeInfo;
import com.autel.common.mission.base.DirectionLatLng;
import com.autel.common.mission.base.DistanceModel;
import com.autel.common.mission.cruiser.CruiserWaypointFinishedAction;
import com.autel.common.mission.cruiser.CruiserWaypointMission;
import com.autel.common.product.AutelProductType;
import com.autel.common.remotecontroller.RemoteControllerInfo;
import com.autel.internal.sdk.mission.cruiser.CruiserWaypointRealTimeInfoImpl;
import com.autel.lib.jniHelper.NativeHelper;
import com.autel.lib.jniHelper.PathPlanningResult;
import com.autel.sdk.battery.CruiserBattery;
import com.autel.sdk.flycontroller.CruiserFlyController;
import com.autel.sdk.mission.MissionManager;
import com.autel.sdk.product.BaseProduct;
import com.autel.sdk.remotecontroller.AutelRemoteController;
import com.autel.sdk10.utils.BytesUtils;
import com.autel.sdksample.R;
import com.autel.sdksample.TestApplication;
import com.autel.sdksample.base.util.FileUtils;
import com.autel.sdksample.dragonfish.rxrunnable.IOUiRunnable;
import com.autel.util.log.AutelLog;

import java.io.File;
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
    private CruiserBattery battery;
    private AutelRemoteController remoteController;
    private MissionManager missionManager;
//    private float lowBatteryPercent = 15f;
//    private boolean isBatteryOk = false; //当前电量是否合适
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
        if (null != product && product.getType() == AutelProductType.DRAGONFISH) {
            missionManager = product.getMissionManager();
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
            battery.setBatteryStateListener(new CallbackWithOneParam<CruiserBatteryInfo>() {
                @Override
                public void onSuccess(CruiserBatteryInfo batteryState) {
                    AutelLog.d(" batteryState " + batteryState.getRemainingPercent());
//                    isBatteryOk = batteryState.getRemainingPercent() > lowBatteryPercent;
                }

                @Override
                public void onFailure(AutelError autelError) {

                }
            });

            mEvoFlyController = (CruiserFlyController) product.getFlyController();
            mEvoFlyController.setFlyControllerInfoListener(new CallbackWithOneParam<CruiserFlyControllerInfo>() {

                @Override
                public void onSuccess(CruiserFlyControllerInfo evoFlyControllerInfo) {
//                    isCompassOk = evoFlyControllerInfo.getFlyControllerStatus().isCompassValid();
//                    isCanTakeOff = evoFlyControllerInfo.getFlyControllerStatus().isCanTakeOff();
//
//                    isImuOk = evoFlyControllerInfo.getFlyControllerStatus().isIMU0Valid() && evoFlyControllerInfo.getFlyControllerStatus().isIMU1Valid();
//
//                    isGpsOk = evoFlyControllerInfo.getFlyControllerStatus().isGpsValid();
                    if (null != evoFlyControllerInfo && null != evoFlyControllerInfo.getFlyControllerStatus()) {
                        SafeCheck safeCheck = evoFlyControllerInfo.getFlyControllerStatus().getSafeCheck();
                        //飞行器自检完成
                        if (safeCheck == SafeCheck.COMPLETE) {
                            if (!isDroneCheckFinish) {
                                isDroneCheckFinish = true;
                                getAutoCheckResult(ModelType.ALL);
                            }
                        }
                    }


                }

                @Override
                public void onFailure(AutelError autelError) {

                }
            });

            remoteController = product.getRemoteController();
            remoteController.setInfoDataListener(new CallbackWithOneParam<RemoteControllerInfo>() {
                @Override
                public void onSuccess(RemoteControllerInfo remoteControllerInfo) {
//                    isImageTransOk = remoteControllerInfo.getDSPPercentage() >= 30;
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
                    Toast.makeText(getApplicationContext(),"autoCheck finish "+isDroneOk,Toast.LENGTH_LONG).show();

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
            double missionType = 1;//任务类型，1-航点任务，6-矩形/多边形任务
            //长度/高度单位均为米

            //长度为3，飞机的纬度、经度、起飞高度
            double[] droneLocation = new double[]{22.59638835580453, 113.99613850526757, 40.0};
            //长度为3，返航点的纬度、经度、返航高度
            double[] homeLocation = new double[]{22.59638835580453, 113.99613850526757, 50.0};
            //长度为4，上升盘旋点的纬度、经度、高度、盘旋半径
            double[] launchLocation = new double[]{22.59638835580453, 113.99318883642341, 100.0, 120.0};
            //长度为4，下降盘旋点的纬度、经度、高度、盘旋半径
            double[] landingLocation = new double[]{22.59291695879857, 113.99787910849454, 100.0, 120.0};
            //长度为8（两个点），如果没有可以全设为0，只用于矩形和多边形，矩形/多边形与上升下降盘旋点之间的点的纬度、经度、高度、是否使用该航点(0-使用，1-不使用)
            double[] avoidPosition = new double[]{22.598295333564423, 113.99354868480384, 100.0, 1.0,
                    22.598772827314363, 113.99867325644607, 100.0, 1.0};

            char waypointLen = 2;//航点的个数/矩形多边形是顶点的个数
            int poiPointLen = 2;//观察点的个数

            //以下参数针对矩形、多边形任务,航点任务时全置为 0 就可以了
            double UAVTurnRad = 120;//飞机转弯半径，默认 120 米
            double UAVFlyVel = 17;//飞行速度(单位m/s)
            double UserFPKIsDef = 1;//是否用户自定义主航线角度，0-自动，1-手动
            double UserFlyPathA = 0;//用户自定义主航线角度，UserFPKIsDef为1时生效
            double WidthSid = 140.56;//旁向扫描宽度,//2*height*tan(HFOV/2)需要自行计算得出
            double OverlapSid = 0.7;//旁向重叠率（0-1）
            double WidthHead = 78.984;//航向扫描宽度,//2*height*tan(VFOV/2)需要自行计算得出
            double OverlapHead = 0.8;//航向重叠率（0-1）
            double UAVFlyAlt = 100;//飞行高度

            /*
                航点定义根据接口协议有16个变量，分别为：
                变量 0：当前航点标识（目前等于航点在当前任务中的序号）
                变量 1：当前航点类型，其中：0–普通航点/飞越;1-兴趣点Orbit;4–起飞航点;5–按时间盘旋航点;6-按圈数盘旋航点;7–降落航点
                变量 2：航点坐标，纬度
                变量 3：航点坐标，经度
                变量 4：航点坐标，高度
                变量 5：航点飞行速度，单位米/秒
                变量 6：盘旋时间或盘旋圈数，只针对航点类型为盘旋有用
                变量 7：盘旋半径，单位：米
                变量 8：盘旋方向：0-顺时针;1-逆时针盘旋
                变量 9：兴趣点起始角度 1-360度
                变量10：兴趣点水平角度 1-360度
                变量11：相机动作类型: 0-无，1-拍照，2-定时拍照，3-定距拍照，4-录像
                变量12：相机动作参数，定时和定距的参数
                变量13：相机动作参数，云台俯仰角（-120 -- 0）
                变量14-15：未定义
            */
            //航点任务
            double[] waypointParamList = new double[]{1.0, 0.0, 22.597737289727164, 113.9974874391902, 100.0, 17.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -90.0, 0.0, 0.0,
                    2.0, 0.0, 22.59897542587946, 114.00336684129968, 100.0, 17.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -90.0, 0.0, 0.0};
            //矩形任务,顶点个数必须大于等于 4 个
//                double[] waypointParamList = new double[]{1.0, 0.0, 22.59808119092429, 113.9951432761672, 100.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -90.0, 0.0, 0.0,
//                        2.0, 0.0, 22.59808119092429, 113.9971040869537, 100.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -90.0, 0.0, 0.0,
//                        3.0, 0.0, 22.596611380444926, 113.9971040869537, 100.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -90.0, 0.0, 0.0,
//                        4.0, 0.0, 22.596611380444926, 113.9951432761672, 100.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -90.0, 0.0, 0.0};

            /*
                航点定义根据接口协议有17个变量，分别为：
                变量 0：纬度
                变量 1：经度
                变量 2：高度
                变量 3：半径
                变量 4：IP_Type，默认 11
                变量 5：关联航点个数
            */
            double[] poiParamList = new double[]{22.601550713371807, 113.99913365283817, 0.0, 120.0, 11.0, 1.0,
                    22.600490797193245, 113.99435713952568, 20.0, 120.0, 11.0, 0.0};

            //关联航点序号列表，每个观察点最多关联五个航点，数组个数为观察点个数*5
            int[] linkPoints = new int[]{2, 0, 0, 0, 0, 0, 0, 0, 0, 0};

            //是否使用地形跟随
            boolean isEnableTopographyFollow = true;

            //返回0表示成功，返回非0表示失败
            int res = NativeHelper.writeMissionFile(filePath, missionType,
                    droneLocation, homeLocation,
                    launchLocation, landingLocation,
                    avoidPosition, UAVTurnRad,
                    UAVFlyVel, UserFPKIsDef,
                    UserFlyPathA, WidthSid,
                    OverlapSid, WidthHead,
                    OverlapHead, UAVFlyAlt,
                    waypointLen, waypointParamList,
                    poiPointLen, poiParamList, linkPoints, isEnableTopographyFollow ? 1 : 0);
            AutelLog.d("NativeHelper", " writeMissionFile result -> " + res);
        });

        testWaypoint.setOnClickListener(v -> {
            //飞机当前位置
            double[] drone = new double[]{22.59651, 113.9972969, 0};//经纬高
            //返航点位置
            double[] homePoint = new double[]{22.59651, 113.9972969, 100.0};//经纬高
            //上升盘旋点
            double[] upHomePoint = new double[]{22.59651, 113.99434723115584, 100.0, 120.0};//经、纬、高、盘旋半径
            //下降盘旋点
            double[] downHomePoint = new double[]{22.59651, 114.00024656884415, 100, 120.0};//经、纬、高、盘旋半径


            /**
             * waypointParams：航点参数每16个值为一组，以下是以两个航点为例子；
             参数说明：航点定义根据接口协议有16个变量，分别为：
             航点定义根据接口协议有16个变量，分别为：
             变量 0：当前航点标识（目前等于航点在当前任务中的序号）
             变量 1：当前航点类型，其中：0–普通航点/飞越;1-兴趣点Orbit;4–起飞航点;5–按时间盘旋航点;6-按圈数盘旋航点;7–降落航点
             变量 2：航点坐标，纬度
             变量 3：航点坐标，经度
             变量 4：航点坐标，高度
             变量 5：航点飞行速度，单位米/秒
             变量 6：盘旋时间或盘旋圈数，只针对航点类型为盘旋有用
             变量 7：盘旋半径，单位：米
             变量 8：盘旋方向：0-顺时针;1-逆时针盘旋
             变量 9：兴趣点起始角度 1-360度
             变量10：兴趣点水平角度 1-360度
             变量11：相机动作类型: 0-无，1-拍照，2-定时拍照，3-定距拍照，4-录像
             变量12：相机动作参数，定时(单位s)和定距（单位m）的参数
             变量13：相机动作参数，云台俯仰角（-120 -- 0）
             变量14-15：未定义
             */
            double[] waypointParams = new double[]{1.0, 0.0, 22.59794923247847, 113.9946704742452, 100.0, 17.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                    2.0, 0.0, 22.593907884795755, 113.99646218984662, 100.0, 17.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
            PathPlanningResult result = NativeHelper.getWaypointMissionPath(drone, homePoint, upHomePoint, downHomePoint, waypointParams);
            int errorCode = result.getErrorCode();//是否规划任务成功，0-成功，1-失败
            double flyLength = result.getFlyLength();//航线总距离
            double flyTime = result.getFlyTime();//预计飞行总时间
            double pictNum = result.getPictNum();//预计拍照数量
            double optCourseAngle = result.getOptCourseAngle();//自动规划主航线角度时使用的主航线角度
            List<AutelCoordinate3D> latLngList = result.getLatLngList();//整条航线所有点的纬经高
            List<DirectionLatLng> directionLatLngList = result.getDirectionLatLngList();//航线中箭头的纬经度
            List<DistanceModel> distanceModelList = result.getDistanceModelList();//航线中两个航点的距离的显示位置的纬度、经度、距离
            List<AutelCoordinate3D> plusList = result.getPlusList();//两个航点间加号的纬度、经度

            AutelLog.debug_i("NativeHelper:", "flyTime = " + flyTime
                    + ", flyLength = " + flyLength + ", picNum = " + pictNum
                    + ",errorCode = " + errorCode);

        });
        testMapping.setOnClickListener(v -> {
            //飞机当前位置
            double[] drone = new double[]{22.59651, 113.9972969, 0};//纬经高
            //返航点位置
            double[] homePoint = new double[]{22.59651, 113.9972969, 100.0};//纬经高
            //上升盘旋点
            double[] upHomePoint = new double[]{22.59651, 113.99434723115584, 100.0, 120.0};//纬、经、高、盘旋半径
            //下降盘旋点
            double[] downHomePoint = new double[]{22.59651, 114.00024656884415, 100, 120.0};//纬、经、高、盘旋半径
            //途经点1 （上升盘旋点到任务之间添加）
            double[] startAvoid = new double[]{22.595300191562032, 113.98885025388489, 100, 1};//纬、经、高、是否有效（0-无效，1-有效）
            //途经点2
            double[] endAvoid = new double[]{22.592050563109837, 113.99623427307421, 100, 1};//纬、经、高、是否有效（0-无效，1-有效）

            //长度为8（两个点），如果没有可以全设为0，只用于矩形和多边形，矩形/多边形与上升下降盘旋点之间的点的纬度、经度、高度、是否使用该航点(0-使用，1-不使用)
            double[] avoidPoints = Arrays.copyOf(startAvoid, startAvoid.length + endAvoid.length);
            //将b数组添加到已经含有a数组的c数组中去
            System.arraycopy(endAvoid, 0, avoidPoints, startAvoid.length, endAvoid.length);
            //矩形或多边形顶点坐标(经、纬、高)
            double[] vertexs = new double[]{22.603459238667625, 113.99525530891242, 100.0
                    , 22.603459238667625, 113.9972294147372, 100.0
                    , 22.601993332010267, 113.9972294147372, 100.0
                    , 22.601993332010267, 113.99525530891242, 100.0};
            //航线高度
            float height = 100f;
            //航线速度
            float speed = 17.0f;
            //旁向重叠率
            double sideRate = 0.8f;
            //主航线重叠率
            float courseRate = 0.7f;
            //主航线角度 0:自动，1：用户自定义航向角度
            int userDefineAngle = 0;
            //当userDefineAngle为1时有效
            int courseAngle = 30;
            //飞机转弯半径，默认要设置120
            int turningRadius = 120;
            //旁向扫描宽度
            double sideScanWidth = 140.56235f;//2*height*tan(HFOV/2)需要自行计算得出
            //航向扫描宽度
            double courseScanWidth = 78.98377f;//2*height*tan(VFOV/2)

            PathPlanningResult result = NativeHelper.getMappingMissionPath(drone, homePoint, upHomePoint, downHomePoint,
                    vertexs, avoidPoints, height, speed, sideRate, courseRate
                    , userDefineAngle, courseAngle, turningRadius
                    , sideScanWidth, courseScanWidth);
            double area = result.getArea();//矩形，多边形的面积
            double flyLength = result.getFlyLength();//航线总距离
            double flyTime = result.getFlyTime();//预计飞行总时间
            double pictNum = result.getPictNum();//预计拍照数量
            double optCourseAngle = result.getOptCourseAngle();//自动规划主航线角度时使用的主航线角度
            List<AutelCoordinate3D> whiteLatLngList = result.getWhiteLatLngList();//矩形/多边形区域内转折点的纬经高
            List<AutelCoordinate3D> latLngList = result.getLatLngList();//整条航线所有点的纬经高
            List<DirectionLatLng> directionLatLngList = result.getDirectionLatLngList();//航线中箭头的纬经度
            List<DistanceModel> distanceModelList = result.getDistanceModelList();//航线中两个航点的距离的显示位置的纬度、经度、距离
            List<AutelCoordinate3D> plusList = result.getPlusList();//两个航点间加号的纬度、经度

            AutelLog.d("NativeHelper", " result " + result.getArea() + " " + result.getErrorCode());

        });
    }

    private void initData() {
        autelMission = new CruiserWaypointMission();

        autelMission.missionId =  BytesUtils.getInt(UUID.randomUUID().toString().replace("-", "").getBytes()); //任务id
        autelMission.missionType = MissionType.Waypoint; //任务类型(Waypoint(航点)、RECTANGLE(矩形)、POLYGON(多边形))
        autelMission.finishedAction = CruiserWaypointFinishedAction.RETURN_HOME;
    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        switch (id) {
            case R.id.autoCheck: {
                //飞行之前，必须进行必要的飞行检查，自检结果会在自检完成后通过 getAutoCheckResult()查询

                autoCheck(ModelType.ALL);
            }
            break;
            case R.id.prepare: {
                if (!isDroneOk) {
                    Toast.makeText(DFWayPointActivity.this, R.string.mission_aircraft_malfunctioning, Toast.LENGTH_LONG).show();
                    return;
                }

                doPrepare();
            }
            break;

            case R.id.start: {
                if (flyState != FlyState.Prepare) {
                    Toast.makeText(DFWayPointActivity.this, R.string.mission_current_state_cannot_execute, Toast.LENGTH_LONG).show();
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
                    Toast.makeText(DFWayPointActivity.this, getString(R.string.mission_current_state_cannot_execute), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(DFWayPointActivity.this, getString(R.string.mission_current_state_cannot_execute), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(DFWayPointActivity.this, getString(R.string.mission_current_state_cannot_execute), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(DFWayPointActivity.this, getString(R.string.mission_current_state_cannot_execute), Toast.LENGTH_LONG).show();
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
        if (flyState != FlyState.None) {
            Toast.makeText(DFWayPointActivity.this, getString(R.string.mission_current_state_cannot_execute), Toast.LENGTH_LONG).show();
            return;
        }
        if (null != missionManager) {
            missionManager.prepareMission(autelMission, filePath, new CallbackWithOneParamProgress<Boolean>() {
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
}
