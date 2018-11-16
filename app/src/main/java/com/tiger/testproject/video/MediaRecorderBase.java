package com.tiger.testproject.video;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tiger.testproject.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhanghe on 2018/10/16.
 */

public class MediaRecorderBase {

    public static final String TAG = MediaRecorderBase.class.getName();

    private Camera mCamera;
    private Context mContext;
    private FocusSurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private MediaRecorder mediaRecorder;
    public String currentVideoFilePath;
    private String saveVideoPath = "";
    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

    private MediaRecorder.OnErrorListener OnErrorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mediaRecorder, int what, int extra) {
            try {
                if (mediaRecorder != null) {
                    mediaRecorder.reset();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public MediaRecorderBase(Context context, FocusSurfaceView surfaceView, SurfaceHolder surfaceHolder){
        mContext = context;
        mSurfaceView = surfaceView;
        mSurfaceHolder = surfaceHolder;
        initCamera();
    }

    public Camera getCamera() {
        return mCamera;
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    /**
     * 初始化摄像头
     */
    private void initCamera() {
        if (mCamera != null) {
            releaseCamera();
        }

        mCamera = Camera.open(mCameraId);
        if (mSurfaceView != null){mSurfaceView.setTouchFocus(mCamera);}
        if (mCamera == null) {
            Toast.makeText(mContext, "未能获取到相机！", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            //将相机与SurfaceHolder绑定
            mCamera.setPreviewDisplay(mSurfaceHolder);
            //配置CameraParams
            configCameraParams();
            //启动相机预览
            mCamera.startPreview();
        } catch (IOException e) {
            //有的手机会因为兼容问题报错，这就需要开发者针对特定机型去做适配了
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }



    /**
     * 设置摄像头为竖屏
     *
     * @author lip
     * @date 2015-3-16
     */
    private void configCameraParams() {
        Camera.Parameters params = mCamera.getParameters();
        //设置相机的横竖屏(竖屏需要旋转90°)
        if (mContext.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            params.set("orientation", "portrait");
            mCamera.setDisplayOrientation(90);
        } else {
            params.set("orientation", "landscape");
            mCamera.setDisplayOrientation(0);
        }
        //设置聚焦模式
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        //缩短Recording启动时间
        params.setRecordingHint(true);
        //影像稳定能力
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            if (params.isVideoStabilizationSupported())
                params.setVideoStabilization(true);
        }
        mCamera.setParameters(params);
    }


    /**
     * 释放摄像头资源
     *
     * @author liuzhongjun
     * @date 2016-2-5
     */
    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 开始录制视频
     */
    private boolean start_Record() {

        initCamera();
        //录制视频前必须先解锁Camera
        mCamera.unlock();
        configMediaRecorder();
        try {
            //开始录制
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 停止录制视频
     */
    private void stop_Record() {
        // 设置后不会崩
        mediaRecorder.setOnErrorListener(null);
        mediaRecorder.setPreviewDisplay(null);
        //停止录制
        mediaRecorder.stop();
        mediaRecorder.reset();
        //释放资源
        mediaRecorder.release();
        mediaRecorder = null;
    }

    private void pause_Record() {
        //正在录制的视频，点击后暂停
        //取消自动对焦
        cancelAutoFocus();
        stop_Record();
    }

    public void startRecord(){
        if (getSDPath(mContext) == null)
            return;
        //视频文件保存路径，configMediaRecorder方法中会设置
        currentVideoFilePath = getSDPath(mContext) + getVideoName();
        //开始录制视频
        start_Record();
    }

    public void stopRecord(){
        //停止视频录制
        stop_Record();
        //先给Camera加锁后再释放相机
        mCamera.lock();
        releaseCamera();
        //判断是否进行视频合并
        if ("".equals(saveVideoPath)) {
            saveVideoPath = currentVideoFilePath;
        } else {
            mergeRecordVideoFile();
        }
    }

    public void pauseRecord(){
        pause_Record();
        //判断是否进行视频合并
        if ("".equals(saveVideoPath)) {
            saveVideoPath = currentVideoFilePath;
        } else {
            mergeRecordVideoFile();
        }
    }

    /**
     * 配置MediaRecorder()
     */
    private void configMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.reset();
        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setOnErrorListener(OnErrorListener);

        //使用SurfaceView预览
        mediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

        //1.设置采集声音
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置采集图像
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        //2.设置视频，音频的输出格式 mp4
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        //3.设置音频的编码格式
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        //设置图像的编码格式
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        //设置立体声
//        mediaRecorder.setAudioChannels(2);
        //设置最大录像时间 单位：毫秒
//        mediaRecorder.setMaxDuration(60 * 1000);
        //设置最大录制的大小 单位，字节
//        mediaRecorder.setMaxFileSize(1024 * 1024);
        //音频一秒钟包含多少数据位
        CamcorderProfile mProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
        mediaRecorder.setAudioEncodingBitRate(44100);
        if (mProfile.videoBitRate > 2 * 1024 * 1024)
            mediaRecorder.setVideoEncodingBitRate(2 * 1024 * 1024);
        else
            mediaRecorder.setVideoEncodingBitRate(1024 * 1024);
        mediaRecorder.setVideoFrameRate(mProfile.videoFrameRate);

        //设置选择角度，顺时针方向，因为默认是逆向90度的，这样图像就是正常显示了,这里设置的是观看保存后的视频的角度
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT){
            mediaRecorder.setOrientationHint(270);
        }else {
            mediaRecorder.setOrientationHint(90);
        }

        //设置录像的分辨率
        mediaRecorder.setVideoSize(720, 480);

        //设置录像视频输出地址
        mediaRecorder.setOutputFile(currentVideoFilePath);
    }

    /**
     * 改变闪光灯
     * @return
     */
    public boolean changeFlash() {
        boolean flashOn = false;
        if (flashEnable()) {
            Camera.Parameters params = mCamera.getParameters();
            if (Camera.Parameters.FLASH_MODE_TORCH.equals(params.getFlashMode())) {
                params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                flashOn = false;
            } else {
                params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                flashOn = true;
            }
            mCamera.setParameters(params);
        }
        return flashOn;
    }

    public boolean flashEnable() {
        return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
                && mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK;

    }

    /** 切换前置/后置摄像头 */
    public void switchCamera() {
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            switchCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
        } else {
            switchCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
        }
    }

    /** 切换前置/后置摄像头 */
    public void switchCamera(int cameraFacingFront) {
        mCameraId = cameraFacingFront;
        releaseCamera();
        initCamera();
    }

    public void cancelAutoFocus(){
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success)
                    mCamera.cancelAutoFocus();
            }
        });
    }





    /**
     * 合并录像视频方法
     */
    private void mergeRecordVideoFile() {
        new AsyncTask<Void,Void,String>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                TextView textView = showProgressDialog();
                textView.setText("视频合成中");
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    String[] str = new String[]{saveVideoPath,currentVideoFilePath};
                    //将2个视频文件合并到 append.mp4文件下
//                    VideoUtils.appendVideo(mContext, getSDPath(mContext) + "append.mp4", str);
                    File reName = new File(saveVideoPath);
                    File f = new File(getSDPath(mContext) + "append.mp4");
                    //再将合成的append.mp4视频文件 移动到 saveVideoPath 路径下
                    f.renameTo(reName);
                    if (reName.exists()) {
                        f.delete();
                        new File(currentVideoFilePath).delete();
                    }
                    return currentVideoFilePath;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                closeProgressDialog();
                if (TextUtils.isEmpty(s)){
                    Toast.makeText(mContext, "视频合成失败", Toast.LENGTH_SHORT).show();
                }else {

                }
            }

        }.execute();
    }



    /**
     * 创建视频文件保存路径
     */
    public static String getSDPath(Context context) {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(context, "请查看您的SD卡是否存在！", Toast.LENGTH_SHORT).show();
            return null;
        }

        File sdDir = Environment.getExternalStorageDirectory();
        File eis = new File(sdDir.toString() + "/RecordVideo/");
        if (!eis.exists()) {
            eis.mkdir();
        }
        return sdDir.toString() + "/RecordVideo/";
    }

    private String getVideoName() {
        return "VID_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".mp4";
    }


    private AlertDialog progressDialog;

    public TextView showProgressDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        View view = View.inflate(mContext, R.layout.dialog_loading, null);
        builder.setView(view);
        ProgressBar pb_loading = view.findViewById(R.id.pb_loading);
        TextView tv_hint = view.findViewById(R.id.tv_hint);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pb_loading.setIndeterminateTintList(ContextCompat.getColorStateList(mContext, R.color.dialog_pro_color));
        }
        tv_hint.setText("视频编译中");
        progressDialog = builder.create();
        progressDialog.show();
        return tv_hint;
    }

    public void closeProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
