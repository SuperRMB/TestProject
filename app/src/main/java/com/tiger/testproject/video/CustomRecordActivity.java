package com.tiger.testproject.video;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tiger.testproject.R;

/**
 * Created by pc on 2017/3/20.
 *
 * @author liuzhongjun
 */

public class CustomRecordActivity extends AppCompatActivity implements Camera.PreviewCallback, View.OnClickListener {

    private static final String TAG = "CustomRecordActivity";
    private final int PERMISSION_REQUEST_CODE = 0x001;
    private static final String[] permissionManifest = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //UI
    private RecordedButton mRecordControl;
    private ImageView iv_change_flash;
    private ImageView iv_change_camera;
    private FocusSurfaceView surfaceView;
    private SurfaceHolder mSurfaceHolder;


    private int maxDuration = 15000;//最大时长

    private MediaRecorderBase mediaRecorderBase;
    private RelativeLayout rl_bottom;
    private ImageView iv_back;
    private ImageView iv_finish;
    private RelativeLayout rl_bottom2;
    private ImageView iv_close;
    private ImageView iv_next;

    //录像机状态标识
    private int mRecorderState;
    /*******初始化状态**********/
    public static final int STATE_INIT = 0;
    /*******录像状态**********/
    public static final int STATE_RECORDING = 1;
    /*******录像暂停状态**********/
    public static final int STATE_PAUSE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_custom);
        initView();
        permissionCheck();
        initData();
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private void permissionCheck() {
        if (Build.VERSION.SDK_INT >= 23) {
            boolean permissionState = true;
            for (String permission : permissionManifest) {
                if (ContextCompat.checkSelfPermission(this, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    permissionState = false;
                }
            }
            if (!permissionState) {
                ActivityCompat.requestPermissions(this, permissionManifest, PERMISSION_REQUEST_CODE);
            } else {

            }
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    if (Manifest.permission.CAMERA.equals(permissions[i])) {

                    } else if (Manifest.permission.RECORD_AUDIO.equals(permissions[i])) {

                    }
                }
            }
        }
    }


    private void initView() {
        surfaceView = findViewById(R.id.record_surfaceView);
        mRecordControl = findViewById(R.id.record_control);
        iv_change_flash = findViewById(R.id.iv_change_flash);
        iv_change_camera = findViewById(R.id.iv_change_camera);

        rl_bottom = findViewById(R.id.rl_bottom);
        iv_back = findViewById(R.id.iv_back);
        iv_finish = findViewById(R.id.iv_finish);

        rl_bottom2 = findViewById(R.id.rl_bottom2);
        iv_close = findViewById(R.id.iv_close);
        iv_next = findViewById(R.id.iv_next);

        iv_back.setOnClickListener(this);
        iv_finish.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        iv_next.setOnClickListener(this);

        //配置SurfaceHolder
        mSurfaceHolder = surfaceView.getHolder();
        // 设置Surface不需要维护自己的缓冲区
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 设置分辨率
        mSurfaceHolder.setFixedSize(320, 280);
        // 设置该组件不会让屏幕自动关闭
        mSurfaceHolder.setKeepScreenOn(true);
        //回调接口
        mSurfaceHolder.addCallback(mSurfaceCallBack);
    }

    long start_time;

    private void initData() {
        mRecordControl.setMax(maxDuration);

        mRecordControl.setOnGestureListener(new RecordedButton.OnGestureListener() {
            @Override
            public void onLongClick() {
                start_time = System.currentTimeMillis();
                System.out.println("======onLongClick=======" + start_time);


                if (mRecorderState == STATE_INIT) {//初始状态开始录制
                    mRecordControl.setSplit();
                    mediaRecorderBase.startRecord();
                    progress();
                    mRecorderState = STATE_RECORDING;
                }else if (mRecorderState == STATE_PAUSE){//暂停状态开始录制

                    mRecorderState = STATE_RECORDING;
                }
            }
            @Override
            public void onClick() {
                System.out.println("======onClick=======");
            }
            @Override
            public void onLift() {
                System.out.println("======onLift=======");


                if (mRecorderState == STATE_RECORDING){//录制状态，暂停
                    mHandler.removeMessages(0);
                    mediaRecorderBase.pauseRecord();
                    mRecorderState = STATE_PAUSE;
                }

            }
            @Override
            public void onOver() {
                mHandler.removeCallbacksAndMessages(null);
                mRecordControl.closeButton();
                mediaRecorderBase.stopRecord();
                long end_time = System.currentTimeMillis() - start_time;
                System.out.println("======onOver======="+end_time);
            }
        });


        iv_change_flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaRecorderBase.changeFlash()) {
                    iv_change_flash.setImageResource(R.mipmap.video_flash_open);
                } else {
                    iv_change_flash.setImageResource(R.mipmap.video_flash_close);
                }
            }
        });

        iv_change_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaRecorderBase.switchCamera();
                iv_change_flash.setImageResource(R.mipmap.video_flash_close);
            }
        });
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0){
                mRecordControl.setProgress(seconds*50);
                mRecordControl.cleanSplit();
                progress();
            }
        }
    };

    int seconds = 0;

    public void progress(){
        seconds++;
        mHandler.sendEmptyMessageDelayed(0,50);
    }


    private SurfaceHolder.Callback mSurfaceCallBack = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            mediaRecorderBase = new MediaRecorderBase(CustomRecordActivity.this, surfaceView,mSurfaceHolder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
            if (mSurfaceHolder.getSurface() == null) {
                return;
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            if (mediaRecorderBase != null)
                mediaRecorderBase.releaseCamera();
        }
    };


    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        camera.addCallbackBuffer(data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:

                break;
        }
    }
}
