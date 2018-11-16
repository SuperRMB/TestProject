package com.tiger.testproject;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tiger.testproject.activity_anim.ShareAct1;
import com.tiger.testproject.album.PhotoAlbunAct;
import com.tiger.testproject.particle.ParticleAct;
import com.tiger.testproject.task.TaskAct;
import com.tiger.testproject.test.CardViewAct;
import com.tiger.testproject.test.TestMeasureAct;
import com.tiger.testproject.test_matrix.HandleBitmapAct;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    public static List<String> items = new ArrayList<>();
    public static List<Class> classList = new ArrayList<>();

    static {
        items.add("测量布局");
        classList.add(TestMeasureAct.class);

        items.add("自定义卡片");
        classList.add(CardViewAct.class);

        items.add("单元测试");
        classList.add(UnitTestAct.class);

        items.add("任务栈");
        classList.add(TaskAct.class);

        items.add("粒子动画");
        classList.add(ParticleAct.class);

        items.add("share动画");
        classList.add(ShareAct1.class);

        items.add("处理bitmap");
        classList.add(HandleBitmapAct.class);

        items.add("选择相册");
        classList.add(PhotoAlbunAct.class);
    }

    @Override
    protected int geteLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        RecyclerView recy_view = findViewById(R.id.recy_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);

        recy_view.setLayoutManager(manager);

        TestAdapter adapter = new TestAdapter(this,items);

        recy_view.setAdapter(adapter);
        adapter.setOnItemClickListener(new TestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, classList.get(position));
                startActivity(intent);
            }
        });
    }
}
