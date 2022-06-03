package com.jdroid.flo.server

import com.jdroid.flo.server.data.MusicInfo
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface ServerConfig {
    @GET("song.json")
    suspend fun getMusicInfo(): Response<MusicInfo>
}
