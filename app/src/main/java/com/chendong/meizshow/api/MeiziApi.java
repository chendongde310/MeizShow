package com.chendong.meizshow.api;

import com.chendong.meizshow.bean.MeiziBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 作者：陈东  —  www.renwey.com
 * 日期：2016/7/22 - 13:21
 * 注释：妹子图获取接口
 */
public interface MeiziApi {

    @GET("福利/{size}/{page}")
    Call<MeiziBean> getMeizi(@Path("size")int size,@Path("page")int page);




}
