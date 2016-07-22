package com.chendong.meizshow.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chendong.meizshow.R;
import com.chendong.meizshow.bean.MeiziBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 作者：陈东  —  www.renwey.com
 * 日期：2016/7/22 - 13:15
 * 注释： 妹子适配器
 */
public class ImgAdapter extends BaseAdapter {

    private Context context;
    private List<MeiziBean.MeizhibodyBean> meizhibodyBeenlist;

    public ImgAdapter(Context context, List<MeiziBean.MeizhibodyBean> meizhibodyBeenlist) {
        this.context = context;
        this.meizhibodyBeenlist = meizhibodyBeenlist;
    }


    @Override
    public int getCount() {
        return meizhibodyBeenlist.size() > 0 ? meizhibodyBeenlist.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(context, R.layout.activity_main_list, null);
        }

        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.main_list_img);
        simpleDraweeView.setImageURI(meizhibodyBeenlist.get(i).getUrl());


        return view;
    }
}
