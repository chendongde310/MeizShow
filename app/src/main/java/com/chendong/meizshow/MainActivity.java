package com.chendong.meizshow;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.chendong.meizshow.adapter.ImgAdapter;
import com.chendong.meizshow.api.MeiziApi;
import com.chendong.meizshow.bean.MeiziBean;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.logger.Logger;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ImgAdapter imgAdapter;
    private TextView textout;
    private int size;
    private int page;
    private Context context;
    private Retrofit retrofit;
    private MeiziApi meiziApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        Fresco.initialize(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.main_listview);
        textout = (TextView) findViewById(R.id.text_out);

        size = 100;   //设置一次获取10张
        page = 1;   //设置获取第一页数据

        retrofit = new Retrofit.Builder()                   //初始化retrofit
                .baseUrl("http://gank.io/api/data/")                 //API地址
                .addConverterFactory(GsonConverterFactory.create())   //使用GSON解析
                .build();

        meiziApi = retrofit.create(MeiziApi.class);    //构建访问接口
        getMeizi();

        textout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMeizi();
                textout.setVisibility(View.GONE);
            }
        });


    }



    private void getMeizi(){
        meiziApi.getMeizi(size, page).enqueue(new Callback<MeiziBean>() {     //enqueue-异步访问，execute-同步访问
            @Override
            public void onResponse(Call<MeiziBean> call, Response<MeiziBean> response) {
                Logger.i("访问成功"); //访问成功
                List<MeiziBean.MeizhibodyBean> meizhibodyBeenlist = response.body().getResults();   //获取返回的妹子对象
                if (imgAdapter == null) {
                    imgAdapter = new ImgAdapter(context, meizhibodyBeenlist);  //初始化适配器
                    listView.setAdapter(imgAdapter);
                } else {
                    imgAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MeiziBean> call, Throwable t) {
                Logger.i("访问失败" + t.toString());  //访问失败
                textout.setVisibility(View.VISIBLE);
            }
        });
    }


}
