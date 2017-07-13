package com.hjq.parserhtml.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hjq.parserhtml.R;
import com.hjq.parserhtml.model.LSM;
import com.hjq.parserhtml.model.MM131;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/6.
 */

public class MM131Adapter extends RecyclerView.Adapter<MM131Adapter.ViewHolder> {
    Context context;
    ArrayList<MM131> dataArr = new ArrayList<>();

    public MM131Adapter(Context context, ArrayList<MM131> dataArr) {
        this.context = context;
        this.dataArr = dataArr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_lsm,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MM131 model = dataArr.get(position);
        String url = "http://img1.mm131.com/pic/" + dataArr.get(position).getArrarId() + "/1.jpg";
        Glide.with(context).load(url).placeholder(R.mipmap.ic_launcher_round).error(R.mipmap.ic_launcher_round).into(holder.ivAvatar);
        holder.tvTitle.setText(model.getTitle());
        holder.tvArrayid.setText(String.valueOf(model.getArrarId()));
        holder.tvCount.setText(String.valueOf(model.getCount()));
        holder.tvDownnum.setText(String.valueOf(model.getDownNum()));
        holder.tvTime.setText(model.getTime());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return dataArr.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_arrayid)
        TextView tvArrayid;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.tv_downnum)
        TextView tvDownnum;
        @BindView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
