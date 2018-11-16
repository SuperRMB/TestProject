package com.tiger.testproject.particle;

import android.graphics.Path;

/**
 * Created by zhanghe on 2018/8/30.
 * 抽象的粒子
 */
public abstract class Particle {

    //粒子速度
    private float speed;

    //粒子宽
    private int particleWidth;
    //粒子高
    private int particleHeight;
    //粒子颜色
    private int color;

    //因子
    private float factor;

    //垂直移动
    private float vertical_move;
    //水平移动
    private float horizontal_move;

    //开始位置

    public float[] getStart_position() {
        return start_position;
    }

    public void setStart_position(float[] start_position) {
        this.start_position = start_position;
    }

    private float[] start_position;

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getParticleWidth() {
        return particleWidth;
    }

    public void setParticleWidth(int particleWidth) {
        this.particleWidth = particleWidth;
    }

    public int getParticleHeight() {
        return particleHeight;
    }

    public void setParticleHeight(int particleHeight) {
        this.particleHeight = particleHeight;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getFactor() {
        return factor;
    }

    public void setFactor(float factor) {
        this.factor = factor;
    }

    public float getVertical_move() {
        return vertical_move;
    }

    public void setVertical_move(float vertical_move) {
        this.vertical_move = vertical_move;
    }

    public float getHorizontal_move() {
        return horizontal_move;
    }

    public void setHorizontal_move(float horizontal_move) {
        this.horizontal_move = horizontal_move;
    }

    /**
     * 设置粒子的形状
     * @return
     */
    public abstract Path getParticleShape();
}
