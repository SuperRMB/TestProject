package com.tiger.testproject.particle;

import android.graphics.Color;
import android.graphics.Path;

/**
 * Created by zhanghe on 2018/8/30.
 * 长方形粒子
 */
public class RectangleParticle extends Particle {

    private Path path;

    public RectangleParticle(float[] startPosition){
        setColor(Color.RED);
        setSpeed(50);
        setFactor(-0.9f);
        setParticleWidth(20);
        setParticleHeight(5);
        setStart_position(startPosition);
    }


    /**
     * 设置粒子的形状
     *  速度公式：vt=v0+at
     *  平均速度公式：V=(v0+vt)/2
     *  位移公式：s=v0t + 1/2at^2
     *  速度-位移公式：vt^2 - v0^2=2as
     *  公式的特点及应用注意事项：公式中涉及vt,v0,a,s,t五个物理量...
     * @return
     */
    @Override
    public Path getParticleShape() {
        path = new Path();
        float[] start_position = getStart_position();
        path.moveTo(start_position[0],start_position[1]);
        path.lineTo(start_position[0]+getParticleWidth(),start_position[1]);
        path.lineTo(start_position[0]+getParticleWidth(),start_position[1]+getParticleHeight());
        path.lineTo(start_position[0],start_position[1]+getParticleHeight());
        path.close();
        float s = getSpeed() + 0.5f * getFactor();
        float v = getSpeed() + getFactor();

        /*if (v <= 0) {
            path = null;
        }*/
        setSpeed(v);
        start_position[1] += s;
        return path;
    }
}
