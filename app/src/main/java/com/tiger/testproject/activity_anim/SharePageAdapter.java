package com.tiger.testproject.activity_anim;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiger.testproject.R;

/**
 * Created by zhanghe on 2018/9/4.
 */

public class SharePageAdapter extends PagerAdapter {
    private Activity activity;

    public SharePageAdapter(Activity activity) {

        this.activity = activity;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_share_page, null);
        //View viewById = view.findViewById(R.id.iv_share);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
