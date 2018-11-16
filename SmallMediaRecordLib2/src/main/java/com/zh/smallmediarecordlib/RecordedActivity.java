package com.zh.smallmediarecordlib;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zh.smallmediarecordlib.api.IMediaRecorder;
import com.zh.smallmediarecordlib.util.Utils;
import com.zh.smallmediarecordlib.widget.FocusSurfaceView;
import com.zh.smallmediarecordlib.widget.MyVideoView;
import com.zh.smallmediarecordlib.widget.RecordedButton;

import java.io.File;

/**
 * Created by zhanghe on 2018/10/17.
 *
 * 处理录制时长
 * 处理视频合成后返回地址
 * 处理预览视频删除本地视频
 */

public class RecordedActivity extends BaseActivity implements View.OnClickListener,IMediaRecorder {

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
    private RelativeLayout rl_bottom;
    private ImageView iv_back;
    private ImageView iv_finish;
    private RelativeLayout rl_bottom2;
    private ImageView iv_close;
    private ImageView iv_next;
    private TextView dialogTextView;
    private MyVideoView vv_play;
    private RelativeLayout rl_top;
    private TextView tv_hint;


    private SurfaceHolder mSurfaceHolder;
    private MediaRecorderBase mediaRecorderBase;

    private int maxDuration = 15000;//最大时长
    //录像机状态标识
    private int mRecorderState;
    /*******初始化状态**********/
    public static final int STATE_INIT = 0;
    /*******录像状态**********/
    public static final int STATE_RECORDING = 1;
    /*******录像暂停状态**********/
    public static final int STATE_PAUSE = 2;

    private SurfaceHolder.Callback mSurfaceCallBack = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            mediaRecorderBase = new MediaRecorderBase(RecordedActivity.this, surfaceView,
                    RecordedActivity.this);
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
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_record);
        if (!Utils.checkCameraHardware(this)){
            Toast.makeText(this,"相机不存在或者被别的应用占用",Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        initView();
        permissionCheck();
        initData();
    }



    private void initView() {
        surfaceView = findViewById(R.id.record_surfaceView);
        mRecordControl = findViewById(R.id.record_control);
        iv_change_flash = findViewById(R.id.iv_change_flash);
        iv_change_camera = findViewById(R.id.iv_change_camera);
        rl_top = findViewById(R.id.rl_top);
        vv_play = findViewById(R.id.vv_play);

        tv_hint = findViewById(R.id.tv_hint);
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
                gone(rl_bottom);
                if (mRecorderState == STATE_INIT) {//初始状态开始录制
//                    mRecordControl.setSplit();
//                    initRecord();
                    startRecord();
                    progress();
                    mRecorderState = STATE_RECORDING;
                }else if (mRecorderState == STATE_PAUSE){//暂停状态开始录制
                    progress();
                    startRecord();
                    mRecorderState = STATE_RECORDING;
                }
            }

            @Override
            public void onClick() {
                System.out.println("======onClick=======");
            }

            @Override
            public void onLift() {
                long end_time = System.currentTimeMillis() - start_time;
                System.out.println("======onLift======="+end_time);
                visible(rl_bottom);
                if (mRecorderState == STATE_RECORDING){//录制状态，暂停
                    mHandler.removeMessages(0);
                    pauseRecord();
                    mRecorderState = STATE_PAUSE;
                }
            }
            @Override
            public void onOver() {
                mRecorderState = STATE_INIT;
                mHandler.removeCallbacksAndMessages(null);
                mRecordControl.closeButton();
                stopRecord();
                long end_time = System.currentTimeMillis() - start_time;
                System.out.println("======onOver======="+end_time);
            }
        });


        iv_change_flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeFlash()) {
                    iv_change_flash.setImageResource(R.mipmap.video_flash_open);
                } else {
                    iv_change_flash.setImageResource(R.mipmap.video_flash_close);
                }
            }
        });

        iv_change_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               switchCamera();
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_finish){
            completeRecord();
            UIState(3);
        }else if (v.getId() == R.id.iv_close){
            if (vv_play.isPlaying())
                vv_play.pause();
            UIState(1);
        }else if (v.getId() == R.id.iv_back){

        }
    }

    /**
     * ui状态
     * 1 预览状态
     * 2 录像暂停状态
     * 3 预览视频状态
     * @param state_code
     */
    private void UIState(int state_code){
        if ( state_code == 1 ){
            visible(tv_hint,mRecordControl,rl_top);
            gone(rl_bottom,rl_bottom2,vv_play);
            mRecordControl.cleanSplit();
            mRecordControl.setProgress(0);
            new File(mediaRecorderBase.currentVideoFilePath).delete();
            seconds = 0;
            initRecord();
        }else if (state_code==2){
            visible(tv_hint,mRecordControl,rl_top,rl_bottom);
            gone(rl_bottom2,vv_play);
        }else if (state_code == 3){
            gone(rl_top,mRecordControl,tv_hint,rl_bottom);
            visible(rl_bottom2,vv_play);
            if (mediaRecorderBase != null){
                mediaRecorderBase.releaseCamera();
            }
        }

    }

    /**
     * 改变灯光
     *
     * @return
     */
    public boolean changeFlash() {
        if (mediaRecorderBase != null){
            return mediaRecorderBase.changeFlash();
        }
        return false;
    }

    /**
     * 切换前后相机
     */
    public void switchCamera() {
        if (mediaRecorderBase != null){
            mediaRecorderBase.switchCamera();
        }
    }

    /**
     *初始化录制
     */
    private void initRecord(){
        staticToast("初始化录制");
        if (mediaRecorderBase != null){
            mediaRecorderBase.initRecord();
        }
    }

    /**
     * 开始录制
     */
    private void startRecord(){
        staticToast("开始录制");
        if (mediaRecorderBase != null){
            mediaRecorderBase.startRecord();
        }
    }

    /**
     * 暂停录制
     */
    private void pauseRecord(){
        staticToast("暂停录制");
        if (mediaRecorderBase != null){
            mediaRecorderBase.pauseRecord();
        }
    }

    /**
     * 停止录制
     */
    private void stopRecord(){
        staticToast("停止录制");
        if (mediaRecorderBase != null){
            mediaRecorderBase.stopRecord();
        }
    }

    /**
     * 完成录制
     */
    private void completeRecord(){
        staticToast("完成录制");
        if (mediaRecorderBase != null){
            mediaRecorderBase.completeRecord();
        }
    }


    /**
     * 开始合成视频
     */
    @Override
    public void startMergeRecordVideo() {
        dialogTextView = showProgressDialog();
        dialogTextView.setText("视频合成中");
    }

    /**
     * 完成合成
     *
     * @param url
     */
    @Override
    public void completeMergeRecordVideo(String url) {
        System.out.println("====完成合成========="+url);
        closeProgressDialog();
        if (TextUtils.isEmpty(url)){
            Toast.makeText(mContext, "视频合成失败", Toast.LENGTH_SHORT).show();
        }else {
            visible(rl_bottom2,vv_play);
            gone(rl_bottom,rl_top,tv_hint,mRecordControl);
            playVideo(url);
        }
    }

    private void playVideo(String url){
        vv_play.setVideoPath(url);
        vv_play.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                vv_play.start();
            }
        });
        if(vv_play.isPrepared()){
            vv_play.setLooping(true);
            vv_play.start();
        }
    }
}

