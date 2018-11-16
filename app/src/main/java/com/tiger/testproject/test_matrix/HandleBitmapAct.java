package com.tiger.testproject.test_matrix;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.view.View;
import android.widget.ImageView;

import com.tiger.testproject.BaseActivity;
import com.tiger.testproject.R;
import com.tiger.testproject.TransformUtil;

/**
 * Created by zhanghe on 2018/9/21.
 */

public class HandleBitmapAct extends BaseActivity {
    @Override
    protected int geteLayoutId() {
        return R.layout.act_bitmap;
    }
    int degrees = 0;

    @Override
    protected void initData() {
        final ImageView iv_scale = findViewById(R.id.iv_scale);

        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.timg_1);
        final int deviceWidth = TransformUtil.getDeviceWidth(this);
        final float i = deviceWidth / 2 * bitmap.getHeight() * 1.0f / bitmap.getWidth();
        final Bitmap b_scale = scaleBitmap(bitmap, deviceWidth/2, (int) i);
        iv_scale.setImageBitmap(b_scale);



        iv_scale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap b_rotate = rotateBitmap(b_scale, degrees);
                iv_scale.setImageBitmap(b_rotate);
                degrees+=90;
            }
        });

        ImageView iv_rotate = findViewById(R.id.iv_rotate);

        Bitmap b_skew = skewBitmap(b_scale);
        iv_rotate.setImageBitmap(b_skew);


        ImageView iv_radius = findViewById(R.id.iv_radius);
        int radius = TransformUtil.dip2px(this, 10);
        Bitmap b_radius = radiusRect(b_scale, radius);
        iv_radius.setImageBitmap(b_radius);
    }

    private Bitmap scaleBitmap(Bitmap source,int newWidth,int newHeight){
        int width = source.getWidth();
        int height = source.getHeight();

        float sw = newWidth * 1.0f / width;
        float sh = newHeight * 1.0f / height;
        System.out.println(sw+"==sw=======sh="+sh);
        Matrix matrix = new Matrix();
        matrix.postScale(sw,sh);
//        matrix.postScale(sw,sh,width,height);

        Bitmap bitmap = Bitmap.createBitmap(source, 0, 0, width, height, matrix, false);
        return bitmap;
    }


    private Bitmap rotateBitmap(Bitmap source,float degrees){
        int width = source.getWidth();
        int height = source.getHeight();

        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);

        Bitmap bitmap = Bitmap.createBitmap(source, 0, 0, width, height, matrix, false);
        return bitmap;
    }

    private Bitmap skewBitmap(Bitmap source){
        int width = source.getWidth();
        int height = source.getHeight();

        Matrix matrix = new Matrix();
        matrix.postSkew(1,0);

        Bitmap bitmap = Bitmap.createBitmap(source, 0, 0, width, height, matrix, false);
        return bitmap;
    }



    private Bitmap radiusRect(Bitmap source,float radius){

        Bitmap radiusBP = Bitmap.createBitmap(source.getWidth(),source.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(radiusBP);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        /**
         * 圆角矩形
         */
//        RectF rectF = new RectF(0,0,source.getWidth(),source.getHeight());
//        canvas.drawRoundRect(rectF,radius,radius,paint);

        /**
         * 菱形
         */
//        Path path = new Path();
//        path.moveTo(source.getWidth()/2,0);
//        path.lineTo(0,source.getHeight()/2);
//        path.lineTo(source.getWidth()/2,source.getHeight());
//        path.lineTo(source.getWidth(),source.getHeight()/2);
//        path.close();
//        canvas.drawPath(path,paint);

        /**
         * 圆形
         */
        int r = source.getHeight()/2;
        Path path = new Path();


        path.close();
        canvas.drawPath(path,paint);

        return radiusBP;
    }
}

