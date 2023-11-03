package com.example.pocantelop.core.services

import com.example.pocantelop.core.entities.*
import com.example.pocantelop.core.entities.io.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TarjetaDigitalService {

    @POST("ObtenerTarjetaAGenerar")
    suspend fun obtenerTarjetasAGenerar(@Body wallet: ObtenerTarjetaAGenerarIn): Response<ObtenerTarjetaAGenerarOut>

    @POST("RegistrarWallet")
    suspend fun registrarWallet(@Body wallet: RegistrarWalletIn): Response<RegistrarWalletOut>

    @POST("NotificarTarjeta")
    suspend fun notificarTarjeta(@Body notificarTarjetaIn: NotificarTarjetaIn): Response<NotificarTarjetaOut>
}