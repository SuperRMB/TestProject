package com.tiger.testproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zhanghe on 2018/8/22.
 */

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestHolder> {


    private Context mContext;
    private List<String> mLists;
    private OnItemClickListener mListener;

    public TestAdapter(Context context, List<String> lists){

        mContext = context;
        mLists = lists;
    }

    @NonNull
    @Override
    public TestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new TestHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull TestHolder holder, int position) {
        holder.text1.setText(mLists.get(position));
    }


    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public class TestHolder extends RecyclerView.ViewHolder{

        private final TextView text1;

        public TestHolder(View itemView) {
            super(itemView);

            text1 = itemView.findViewById(android.R.id.text1);
            text1.setGravity(Gravity.CENTER);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        mListener.onItemClick(view,getAdapterPosition());
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
