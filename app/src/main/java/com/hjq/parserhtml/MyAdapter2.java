package com.hjq.parserhtml;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/6.
 */

public class MyAdapter2 extends BaseAdapter {
    ArrayList<Model2> dataArr = new ArrayList<>();

    public MyAdapter2(ArrayList<Model2> dataArr) {
        this.dataArr = dataArr;
    }

    @Override
    public int getCount() {
        return dataArr.size();
    }

    @Override
    public Object getItem(int position) {
        return dataArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Model2 model=dataArr.get(position);
        ViewHolder holder;
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.tvArrayid.setText(String.valueOf(model.getArrarId()));
        holder.tvCount.setText(String.valueOf(model.getCount()));
        holder.tvDownnum.setText(String.valueOf(model.getDownNum()));
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_arrayid)
        TextView tvArrayid;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.tv_downnum)
        TextView tvDownnum;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
