package com.example.pocantelop.core.repositories

import android.util.Log
import com.example.pocantelop.BuildConfig
import com.example.pocantelop.core.RetrofitFactory
import com.example.pocantelop.core.entities.*
import com.example.pocantelop.core.entities.io.*
import com.example.pocantelop.core.services.TarjetaDigitalService
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import java.net.UnknownHostException

class TarjetaDigitalRepository(logLevel: HttpLoggingInterceptor.Level): RetrofitFactory(logLevel, BuildConfig.BASE_URL) {

    suspend fun obtenerTarjetasAGenerar(obtenerTarjetasAGenerarIn: ObtenerTarjetaAGenerarIn): Result<ObtenerTarjetaAGenerarOut> {
        try {
            val response = create(TarjetaDigitalService::class.java).obtenerTarjetasAGenerar(obtenerTarjetasAGenerarIn)
            if (response.isSuccessful) {
                return if (response.body()!!.result.tarjetaDigital.isNotEmpty()) {
                    Result.Success(response.body()!!)
                } else {
                    Log.d("Repo obtener tarjetas", "${response.body()}")
                    Result.Error("No hay tarjetas")
                }
            }
            throw Exception("API retornó error")
        } catch (e: UnknownHostException) {
            Log.e("Rep - Host", e.message.toString())
            return Result.Error("Verifique su conexión a internet")
        } catch (e: Exception) {
            Log.e("Repo obtener tarjetas - Generic", e.message.toString())
            return Result.Error("Ocurrió un error al buscar las tarjetas");
        }
    }

    suspend fun registrarWallet(registrarWalletIn: RegistrarWalletIn): Result<RegistrarWalletOut> {
        try {
            val response = create(TarjetaDigitalService::class.java).registrarWallet(registrarWalletIn)
            if (response.isSuccessful) {
                return if (response.body()!!.registrarWalletResult.codigo == "00") {
                    Result.Success(response.body()!!)
                } else {
                    Log.d("Repo registrar wallet", "${response.body()}")
                    Result.Error(response.body()!!.registrarWalletResult.mensaje)
                }
            }
            throw Exception("API retornó error")
        } catch (e: UnknownHostException) {
            Log.e("Rep - Host", e.message.toString())
            return Result.Error("Verifique su conexión a internet")
        } catch (e: Exception) {
            Log.e("Repo registrar wallet - Generic", e.message.toString())
            return Result.Error("Ocurrió un error al registrar la wallet");
        }
    }

    suspend fun notificarTarjeta(notificarTarjetaIn: NotificarTarjetaIn): Result<NotificarTarjetaOut> {
        try {
            val response = create(TarjetaDigitalService::class.java).notificarTarjeta(notificarTarjetaIn)
            if (response.isSuccessful) {
                return if (response.body()!!.notificarTarjetaResult.codigo == "00") {
                    Result.Success(response.body()!!)
                } else {
                    Log.d("Repo notificar tarjeta", "${response.body()}")
                    Result.Error(response.body()!!.notificarTarjetaResult.mensaje)
                }
            }
            throw Exception("API retornó error")
        } catch (e: UnknownHostException) {
            Log.e("Rep - Host", e.message.toString())
            return Result.Error("Verifique su conexión a internet")
        } catch (e: Exception) {
            Log.e("Repo notificar tarjeta - Generic", e.message.toString())
            return Result.Error("Ocurrió un error al notificar la tarjeta");
        }
    }

}