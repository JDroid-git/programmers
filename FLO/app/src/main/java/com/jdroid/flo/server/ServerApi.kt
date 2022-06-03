package com.jdroid.flo.server

import com.jdroid.flo.server.data.MusicInfo
import com.jdroid.flo.util.Constants
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServerApi {
    private fun getRetrofit(): Retrofit =
        Retrofit.Builder().client(OkHttpClient())
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    suspend fun getMusicInfo(callback: ServerCallback) {
        val api = getRetrofit().create(ServerConfig::class.java)
        val call: Response<MusicInfo> = api.getMusicInfo()
//        call.enqueue(object: Callback<MusicInfo> {
//            override fun onResponse(call: Call<MusicInfo>, response: Response<MusicInfo>) {
//                if (response.isSuccessful) {
//                    callback.onSuccess(call, response)
//                } else {
//                    callback.onFailed(call, response, null)
//                }
//
//            }
//
//            override fun onFailure(call: Call<MusicInfo>, t: Throwable) {
//                callback.onFailed(call, null, t)
//            }
//        })
    }

    suspend fun getMusicInfo() = getRetrofit().create(ServerConfig::class.java).getMusicInfo()
}