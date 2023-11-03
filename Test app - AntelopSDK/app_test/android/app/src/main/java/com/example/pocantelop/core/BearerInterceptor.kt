package com.example.pocantelop.core

import okhttp3.Interceptor
import okhttp3.Response

class BearerInterceptor(): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().newBuilder()
            .header("Authorization", "Bearer " + "b51532eb-7524-4fac-8880-1c1a3890c025")
            .build()
            )
    }

}