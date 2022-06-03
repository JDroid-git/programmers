package com.jdroid.flo.server

import retrofit2.Call
import retrofit2.Response

interface ServerCallback {
    fun <Any>onSuccess(call: Call<Any>, response: Response<Any>)
    fun <Any> onFailed(call: Call<Any>, response: Response<Any>?, t: Throwable?)
}