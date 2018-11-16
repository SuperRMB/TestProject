package com.tiger.testproject.activity_anim;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiger.testproject.R;

/**
 * Created by zhanghe on 2018/9/4.
 */

public class ListShareAdapter extends RecyclerView.Adapter<ListShareAdapter.ListShareHolder> {

    private Activity context;

    public ListShareAdapter(Activity context){

        this.context = context;
    }

    @NonNull
    @Override
    public ListShareHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_share, parent, false);
        return new ListShareHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListShareHolder holder, int position) {
        holder.tv_title.setText("这是标题"+position);
        holder.tv_desc.setText("这是描述"+position);
    }

    @Override
    public int getItemCount() {
        return 50;
    }

    public class ListShareHolder extends RecyclerView.ViewHolder{

        private final TextView tv_title;
        private final TextView tv_desc;
        private final View iv_share;

        public ListShareHolder(View itemView) {
            super(itemView);
            iv_share = itemView.findViewById(R.id.iv_share);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Intent intent = new Intent(context, ShareAct2.class);
                        context.startActivity(intent,
                                ActivityOptions.makeSceneTransitionAnimation(context,
                                        Pair.create(iv_share,"share"),
                                        Pair.create(iv_share,"share_anim")
                                ).toBundle());
                    }

                    /*Intent intent = new Intent(context, ShareAct2.class);
                    context.startActivity(intent);*/
                }
            });
        }
    }
}
