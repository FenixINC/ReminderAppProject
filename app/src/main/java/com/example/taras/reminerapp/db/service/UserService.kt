package com.example.taras.reminerapp.db.service

import com.example.taras.reminerapp.db.model.Login
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Taras Koloshmatin on 15.08.2018
 */
interface UserService {

    @POST("/login/login")
    fun login(@Body login: Login): Call<ResponseBody>

    @POST("/login/create")
    fun create(@Body login: Login): Call<ResponseBody>

//    @FormUrlEncoded
//    @POST("/create/create-user")
//    fun create(@Field("username") username: String,
//              @Field("password") password: String
//    ): Call<Login>

}