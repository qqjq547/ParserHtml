package com.hjq.parserhtml.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.hjq.parserhtml.R;
import com.hjq.parserhtml.model.LSM;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/6.
 */

public class LSMAdapter extends RecyclerView.Adapter<LSMAdapter.ViewHolder> {
    Context context;
    ArrayList<LSM> dataArr = new ArrayList<>();

    public LSMAdapter(Context context, ArrayList<LSM> dataArr) {
        this.context = context;
        this.dataArr = dataArr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_lsm,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LSM model = dataArr.get(position);
        Log.i("hjq","getThumb="+model.getThumb());
//        GlideUrl glideUrl = new GlideUrl(model.getThumb(), new LazyHeaders.Builder()
//                .addHeader("Referer", "http://www.lesmao.com/")
//                .build());
        Glide.with(context).load(model.getThumb()).placeholder(R.mipmap.ic_launcher_round).error(R.mipmap.ic_launcher_round).into(holder.ivAvatar);
//        Glide.with(context).load(model.getThumb()).placeholder(R.mipmap.ic_launcher_round).error(R.mipmap.ic_launcher_round).into(holder.ivAvatar);
        holder.tvTitle.setText("["+(position+1)+"]  "+model.getTitle());
        holder.tvArrayid.setText(String.valueOf(model.getArrarId()));
        holder.tvCount.setText(String.valueOf(model.getUrlArr().size()));
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
