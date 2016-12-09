package com.chendong.meizshow;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.chendong.meizshow.adapter.ImgAdapter;
import com.chendong.meizshow.api.MeiziApi;
import com.chendong.meizshow.bean.MeiziBean;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ImgAdapter imgAdapter;
    private TextView textout;
    private int size = 10;//设置一次获取10张
    private int page = 1;   //设置获取第一页数据
    private Context context;
    private Retrofit retrofit;
    private MeiziApi meiziApi;
    private List<MeiziBean.MeizhibodyBean> meizhiList;
    private boolean httpFlag =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        Fresco.initialize(context);
        builderRetrofit();
        //设置通知栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.main_listview);
        textout = (TextView) findViewById(R.id.text_out);
        meizhiList = new ArrayList<>();
        addListener();
        getMeizi(size, page);

        textout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMeizi(size, page);
            }
        });


    }

    private void builderRetrofit() {
        retrofit = new Retrofit.Builder()                   //初始化retrofit
                .baseUrl("http://gank.io/api/data/")                 //API地址
                .addConverterFactory(GsonConverterFactory.create())   //使用GSON解析
                .build();

        meiziApi = retrofit.create(MeiziApi.class);    //构建访问接口
    }


    private void getMeizi(int size, int page) {
        if(!httpFlag) {
            httpFlag = true;
            textout.setVisibility(View.GONE);
            meiziApi.getMeizi(size, page).enqueue(new Callback<MeiziBean>() {     //enqueue-异步访问，execute-同步访问
                @Override
                public void onResponse(Call<MeiziBean> call, Response<MeiziBean> response) {
                    httpFlag = false;
                    Logger.i("访问成功"); //访问成功
                    List<MeiziBean.MeizhibodyBean> newList = response.body().getResults();   //获取返回的妹子对象
                    meizhiList.addAll(newList);
                    if (imgAdapter == null) {
                        imgAdapter = new ImgAdapter(context, meizhiList);  //初始化适配器
                        listView.setAdapter(imgAdapter);
                    } else {
                        imgAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<MeiziBean> call, Throwable t) {
                    Logger.i("访问失败" + t.toString());  //访问失败
                    textout.setVisibility(View.VISIBLE);
                    httpFlag = false;
                }
            });
        }
    }


    private void addListener() {
        // 监听listview滚到最底部
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (view.getLastVisiblePosition() == (view.getCount()-1)) {
                            page++;
                            getMeizi(size, page);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });
    }

}
