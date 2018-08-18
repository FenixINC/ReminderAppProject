package com.example.taras.reminerapp.db.service

import com.example.taras.reminerapp.db.model.Remind
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by Taras Koloshmatin on 26.07.2018
 */
interface RemindService {

    @GET("/reminder/get")
    fun getList(): Call<List<Remind>>

    @GET("/reminder/get-by-id")
    fun getById(@Query("id") id: Int): Call<Remind>

    @GET("/reminder/get-by-type")
    fun getListByType(@Query("type") typeRemind: String): Call<List<Remind>>

    @POST("/reminder/create")
    fun createRemind(@Body remind: Remind): Call<ResponseBody>

    @DELETE("/reminder/delete")
    fun delete(@Query("id") id: Int): Call<ResponseBody>

    //--- Spark Server:
//    @POST("/reminder/create")
//    fun createRemind(@Body remind: Remind): Call<ResponseBody>

    @GET("/reminder/get-all")
    fun getSparkList(): Call<List<Remind>>
}