package com.example.mamun.imageuploadusingretrofitexample1;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("imageupload.php")
    Call<ImageClass> uploadImage(@Field("title") String title,@Field("image") String image);


}
