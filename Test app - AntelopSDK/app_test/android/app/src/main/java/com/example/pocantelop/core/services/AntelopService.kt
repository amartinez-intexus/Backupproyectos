package com.example.pocantelop.core.services

import com.example.pocantelop.core.entities.io.GetAntelopDataIn
import com.example.pocantelop.core.entities.io.GetAntelopDataOut
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.PUT

interface AntelopService {

    @Headers(
        "X-Issuer-Id:demo",
        "X-User-Id:demo-bearer"
    )
    @PUT("prepareDigitization")
    suspend fun getAntelopData(@Body antelopDataIn: GetAntelopDataIn): Response<GetAntelopDataOut>

}