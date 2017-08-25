package com.hjq.parserhtml.http.retrofit;


import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface ApiStores {
    /**
     * 首页
     *
     * @return
     */
    @GET("{type}/{id}.html")
    Observable<String> getSize(@Path("type") String type,@Path("id") int id);
    @GET
    Observable<ResponseBody> downloadPicFromNet(@Url String fileUrl);

    /**
     * 原图
     * @param mod
     * @param tid
     * @param page
     * @return
     */
    @GET("forum.php")
    Observable<String> getSize2(@Query("mod") String mod,@Query("tid") int tid,@Query("page") int page);
    @GET("thread-{tid}-{page}-1.html")
    Observable<String> getSize2(@Path("tid") int tid,@Path("page") int page);
    @GET("portal.php")
    Observable<String> getPageData(@Query("page") int page);

}