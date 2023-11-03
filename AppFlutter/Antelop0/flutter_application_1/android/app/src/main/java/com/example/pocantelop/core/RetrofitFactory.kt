package com.example.pocantelop.core

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class RetrofitFactory(
    private val logLevel: HttpLoggingInterceptor.Level,
    private val url: String
) {
    fun <T> create(serviceType: Class<T>): T {
        return create(serviceType, getHttpClient(), url)
    }

    private fun <T> create(
        serviceType: Class<T>,
        client: OkHttpClient,
        baseUrl: String
    ): T {
        val retrofit = Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl).build()
        return retrofit.create(serviceType)
    }

    private fun getHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(HttpLoggingInterceptor().apply { level = logLevel })
            .addInterceptor(BearerInterceptor())
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
        return builder.build()
    }
}