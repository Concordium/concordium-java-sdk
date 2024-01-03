package com.example.android_sdk_example.wallet_proxy

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
class ProxyBackendConfig(val context: Context, baseUrl: String) {
    val retrofit: Retrofit
    val backend: ProxyBackend

    init {
        retrofit = initializeRetrofit(baseUrl)
        backend = retrofit.create(ProxyBackend::class.java)
    }

    private fun initializeGson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }

    private fun initializeRetrofit(baseUrl: String): Retrofit {
        val gson: Gson = initializeGson()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(initializeOkkHttp())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private fun initializeOkkHttp(): OkHttpClient {
        var okHttpClientBuilder = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .cache(null)
        return okHttpClientBuilder.build()
    }
}