package com.example.android_sdk_example.services.wallet_proxy

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

internal class ProxyBackendConfig(baseUrl: String, private val timeoutSeconds: Long = 30) {
    private val retrofit: Retrofit
    val backend: ProxyBackend

    init {
        retrofit = initializeRetrofit(baseUrl)
        backend = retrofit.create(ProxyBackend::class.java)
    }

    private fun initializeRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(initializeOkkHttp())
            .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()))
            .build()
    }

    private fun initializeOkkHttp(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient().newBuilder()
            .connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .readTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .writeTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .cache(null)
        return okHttpClientBuilder.build()
    }
}