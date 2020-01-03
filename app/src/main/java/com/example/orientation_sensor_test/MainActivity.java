package com.example.orientation_sensor_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.orientation_sensor_test.View.CompassView;
import com.example.orientation_sensor_test.View.DirectionView;
import com.example.orientation_sensor_test.View.Level_Horizontal_View;
import com.example.orientation_sensor_test.View.Level_Vertical_View;
import com.example.orientation_sensor_test.View.Round_Bar_View;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager mSensorManager;
    private TextView warnmessage;
    boolean passUP =false; //確認手機向上
    boolean passDown =false; //確認手機向下
    boolean passLeft =false; //確認手機向左
    boolean passRight =false; //確認手機向右
    boolean passTurnLeft =false; //確認手機左轉
    boolean passTurnRight =false; //確認手機右轉
    boolean delay = false; //為了延遲 顯示出"OK"
    float xAngle_Text; // 測試 X 軸 用的起始值
    float yAngle ;//獲取與 y 軸的夾角
    float zAngle ;//獲取與 z 軸的夾角
    float xAngle ;//獲取與 x 軸的夾角
    String TestInt="0"; //測試步驟
    int DEGREE = 30; // 測試通過的分數的源始值
    float XDEGREE = 0; //記錄x軸的角度
    private ProgressBar mProgressBar; //進度條
    private TextView mPercentage;
    Level_Horizontal_View Level_Horizontal;
    Level_Vertical_View Level_Vertical;
    Round_Bar_View Round_Bar;
    DirectionView Direction;
    CompassView mPointer;
    private TextView XYZ_tv;
    private boolean mStopDrawing;
    private float mDirection;
    private float mTargetDirection;
    private final float MAX_ROATE_DEGREE = 1.0f;
    private AccelerateInterpolator mInterpolator;
    protected final Handler mHandler = new Handler();
    protected Runnable mCompassViewUpdater = new Runnable() {
        @Override
        public void run() {
            if (mPointer != null && !mStopDrawing) {
                if (mDirection != mTargetDirection) {

                    // calculate the short routine
                    float to = mTargetDirection;
                    if (to - mDirection > 180) {
                        to -= 360;
                    } else if (to - mDirection < -180) {
                        to += 360;
                    }

                    // limit the max speed to MAX_ROTATE_DEGREE
                    float distance = to - mDirection;
                    if (Math.abs(distance) > MAX_ROATE_DEGREE) {
                        distance = distance > 0 ? MAX_ROATE_DEGREE : (-1.0f * MAX_ROATE_DEGREE);
                    }

                    // need to slow down if the distance is short
                    mDirection = normalizeDegree(mDirection
                            + ((to - mDirection) * mInterpolator.getInterpolation(Math
                            .abs(distance) > MAX_ROATE_DEGREE ? 0.4f : 0.3f)));
                    mPointer.updateDirection(mDirection);
                }
                mHandler.postDelayed(mCompassViewUpdater, 20);
            }
        }
    };
    private float normalizeDegree(float degree) {
        return (degree + 720) % 360;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mDirection = 0.0f;
        mTargetDirection = 0.0f;
        mInterpolator = new AccelerateInterpolator();
        mStopDrawing = true;
        warnmessage = (TextView) findViewById(R.id.warnmessage);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mPercentage = (TextView) findViewById(R.id.progress_tv);
        Level_Horizontal = (Level_Horizontal_View) findViewById(R.id.Horizontal_View);
        Level_Vertical = (Level_Vertical_View) findViewById(R.id.Vertical_View);
        Round_Bar = (Round_Bar_View) findViewById(R.id.Bar_View);
        Direction = (DirectionView) findViewById(R.id.Direction_View);
        mPointer = (CompassView) findViewById(R.id.compass_pointer);
        XYZ_tv = (TextView) findViewById(R.id. XYZ_tv);
        Level_Vertical.setVisibility(View.VISIBLE);
        Level_Horizontal.setVisibility(View.GONE);
        Round_Bar.setVisibility(View.GONE);
        Level_Vertical.Test_paint=true;
        mProgressBar.setMax(100);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Level_Vertical.MAX_ANGLE=DEGREE;
        Level_Horizontal.MAX_ANGLE=DEGREE;
        Direction.MAX_ANGLE= DEGREE;
    }
    @Override
    public void onResume() {
        super.onResume();
        // 為系統的方向傳感器註冊監聽器
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE ),
                SensorManager.SENSOR_DELAY_GAME);
        mStopDrawing = false;
        mHandler.postDelayed(mCompassViewUpdater, 20);
    }

    @Override
    protected void onPause() {
        // 取消註冊
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        // 取消註冊
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 獲取觸發event的傳感器類型
        int sensorType = event.sensor.getType();
        switch (sensorType) {
            case Sensor.TYPE_ORIENTATION:
                float[] values = event.values;
                // 獲取與Y軸的夾角
                yAngle = values[1];
                // 獲取與Z軸的夾角
                zAngle = values[2];
                // 獲取與X軸的夾角
                xAngle = values[0];
                Log.v(TAG,"\n Z"+ String.valueOf(zAngle));
                Log.v(TAG, "\n Y"+String.valueOf(yAngle));
                Log.v(TAG, "\n X"+String.valueOf(xAngle));
                XYZ_tv.setText("\n"+" X : "+(int)xAngle+"\n"+" Y : "+(int)yAngle+"\n"+" Z : "+(int)zAngle+"\n");
                float direction = event.values[0] * -1.0f;
                mTargetDirection = normalizeDegree(direction);
                Direction.yAngle= values[1];
                Direction.zAngle= values[2];
                if(!delay) {//為了延遲 顯示出"OK"
                    if (TestInt.equals("0")) {//手機向上
                        Level_Vertical.zAngle= zAngle;
                        float Q = zAngle;
                        if(Q<0){//防止進度條負數
                            Q=0;
                        }
                        float A= (Q/DEGREE)*100;//百分比
                        int B = (int) A;
                        download(B);//顯示進度
                        warnmessage.setText("請吧手機向上");
                        if (zAngle >= DEGREE && !passUP) {//夾角大於通過值同時是第一次
                            passUP = true;
                            delay=true;//延遲開始
                            warnmessage.setText("ok");
                            handler.postDelayed(runnable, 1000);//延遲 1 秒
                        }
                    } else if (TestInt.equals("1")) {//手機向下
                        Level_Vertical.zAngle= zAngle;
                        float Q = (zAngle*-1);//源始是負數
                        if(Q<0){//防止進度條負數
                            Q=0;
                        }
                        float A= (Q/DEGREE)*100;//百分比
                        int B = (int) A;
                        download(B);//顯示進度
                        warnmessage.setText("請吧手機向下");
                        if (zAngle <= 0-DEGREE && !passDown) {//夾角大於通過值同時是第一次
                            passDown = true;
                            delay=true;//延遲開始
                            warnmessage.setText("ok");
                            handler.postDelayed(runnable, 1000);//延遲 1 秒
                        }
                    } else if (TestInt.equals("2")) {//手機向左
                        Level_Horizontal.yAngle= yAngle;
                        float Q = yAngle;
                        if(Q<0){//防止進度條負數
                            Q=0;
                        }
                        float A= (Q/DEGREE)*100;//百分比
                        int B = (int) A;
                        download(B);//顯示進度
                        warnmessage.setText("請吧手機向左");
                        if (yAngle >= DEGREE && !passLeft) {//夾角大於通過值同時是第一次
                            passLeft = true;
                            delay=true;//延遲開始
                            warnmessage.setText("ok");
                            handler.postDelayed(runnable, 1000);//延遲 1 秒
                        }
                    } else if (TestInt.equals("3")) {//手機向右
                        Level_Horizontal.yAngle= yAngle;
                        float Q = (yAngle*-1);//源始是負數
                        if(Q<0){//防止進度條負數
                            Q=0;
                        }
                        float A= (Q/DEGREE)*100;//百分比
                        int B = (int) A;
                        download(B);//顯示進度
                        warnmessage.setText("請吧手機向右");
                        if (yAngle <= 0-DEGREE && !passRight) {//夾角大於通過值同時是第一次
                            passRight = true;
                            delay=true;//延遲開始
                            warnmessage.setText("ok");
                            handler.postDelayed(runnable, 1000);//延遲 1 秒
                        }
                    }else if (TestInt.equals("4")) {//手機轉左
                        Log.v(TAG, "\n xAngle "+xAngle+" xAngle_Text "+xAngle_Text+"...................................................");
                        if (xAngle_Text <= 355) { //防止通過值大於360
                            if (xAngle >= xAngle_Text + 1) {
                                XDEGREE++; //記錄x軸的角度
                                xAngle_Text++; //起始值增加
                            }
                        }else {
                            if (xAngle+360 >= xAngle_Text + 1 && xAngle+360<=xAngle_Text + 10) {
                                XDEGREE++; //記錄x軸的角度
                                xAngle_Text++; //起始值增加
                            }
                        }
                        Round_Bar.i=(int) XDEGREE;
                        Log.v(TAG, "\n XDEGREE "+XDEGREE+" DEGREE "+DEGREE+" ((XDEGREE)/DEGREE)*100= "+(XDEGREE/DEGREE)*100);
                        float A= (XDEGREE/DEGREE)*100;//百分比
                        int B = (int) A;
                        download(B);//顯示進度
                        warnmessage.setText("請吧手機轉右");
                        if (XDEGREE > DEGREE && !passTurnLeft) {//夾角大於通過值同時是第一次
                            XDEGREE=0;
                            passTurnLeft = true;
                            delay=true;//延遲開始
                            warnmessage.setText("ok");
                            handler.postDelayed(runnable, 1000);//延遲 1 秒
                        }
                    }else if (TestInt.equals("5")) {//手機轉右
                        Log.v(TAG, "\n xAngle "+xAngle+" xAngle_Text "+xAngle_Text+"...................................................");
                        if (xAngle_Text >= 5) { //防止通過值小於0
                            if (xAngle <= xAngle_Text - 1 ) {
                                XDEGREE++;//記錄x軸的角度
                                xAngle_Text--;//起始值減少
                            }
                        }else {
                            if (xAngle-360 <= xAngle_Text - 1 && xAngle-360 >= xAngle_Text -10 ) {
                                XDEGREE++;//記錄x軸的角度
                                xAngle_Text--;//起始值減少
                            }
                        }
                        Round_Bar.i=(int) -XDEGREE;
                        Log.v(TAG, "\n XDEGREE "+XDEGREE+" DEGREE "+DEGREE+" ((XDEGREE)/DEGREE)*100= "+(XDEGREE/DEGREE)*100);
                        float A= (XDEGREE/DEGREE)*100;//百分比
                        int B = (int) A;
                        download(B);//顯示進度
                        warnmessage.setText("請吧手機轉左");
                        if (XDEGREE > DEGREE && !passTurnRight) {//夾角大於通過值同時是第一次
                            XDEGREE=0;
                            passTurnRight = true;
                            delay = true;//延遲開始
                            warnmessage.setText("ok");
                            handler.postDelayed(runnable, 1000);//延遲 1 秒
                        }
                    }
                    Log.v(TAG, " xAngle_Text \n"+String.valueOf( xAngle_Text)+ "   xAngle_Text");
                }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {//測試步驟
            if(TestInt.equals("0")){
                TestInt="1";
                Level_Vertical.Test_paint= false;
            }else if (TestInt.equals("1")){
                Level_Horizontal.Test_paint=true;
                Level_Vertical.setVisibility(View.GONE);
                Level_Horizontal.setVisibility(View.VISIBLE);
                TestInt="2";
            }else if (TestInt.equals("2")){
                Level_Horizontal.Test_paint=false;
                TestInt="3";
            }else if (TestInt.equals("3")){
                Level_Vertical.setVisibility(View.GONE);
                Level_Horizontal.setVisibility(View.GONE);
                Round_Bar.setVisibility(View.VISIBLE);
                Round_Bar.Direction=true;
                Round_Bar.Stop=DEGREE;
                xAngle_Text=xAngle;// 記錄 X 軸 用的起始值
                TestInt="4";
            }else if (TestInt.equals("4")) {
                Round_Bar.Direction=false;
                Round_Bar.Stop=DEGREE;
                xAngle_Text=xAngle;// 記錄 X 軸 用的起始值
                TestInt="5";
            }else{
                Round_Bar.setVisibility(View.GONE);
                Level_Vertical.Test_paint= true;
                Level_Vertical.setVisibility(View.VISIBLE);
                passUP =false;
                passDown =false;
                passLeft =false;
                passRight =false;
                passTurnLeft =false;
                passTurnRight =false;
                TestInt="0";
            }
            delay=false; //顯示出 OK 1秒
        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:    //返回键
                return true;   //这里由于break会退出，所以我们自己要处理掉 不返回上一层
        }
        return super.onKeyDown(keyCode, event);
    }
    public void download(final int S){
        mPercentage.setText(S + "%");
        mProgressBar.setProgress(S);
    }
}
