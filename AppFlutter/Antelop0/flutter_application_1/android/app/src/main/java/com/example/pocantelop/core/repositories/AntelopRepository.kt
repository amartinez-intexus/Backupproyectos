package com.example.pocantelop.core.repositories

import android.util.Log
import com.example.pocantelop.BuildConfig
import com.example.pocantelop.core.RetrofitFactory
import com.example.pocantelop.core.entities.*
import com.example.pocantelop.core.entities.io.GetAntelopDataIn
import com.example.pocantelop.core.entities.io.GetAntelopDataOut
import com.example.pocantelop.core.services.AntelopService
import okhttp3.logging.HttpLoggingInterceptor
import java.net.UnknownHostException

class AntelopRepository(logLevel: HttpLoggingInterceptor.Level): RetrofitFactory(logLevel, BuildConfig.ANTELOP_URL) {

    suspend fun getAntelopData(tarjeta: Tarjeta): Result<GetAntelopDataOut> {
        return try {
            val response = create(AntelopService::class.java).getAntelopData(GetAntelopDataIn(Recipient(tarjeta.walletId), "${tarjeta.bin.padEnd(16, '0')}ByIssCrdId"))
            if (response.isSuccessful)
                Result.Success(response.body()!!)
            else
                throw Exception("API retornó error")
        } catch (e: UnknownHostException) {
            Log.e("Rep - Host", e.message.toString())
            Result.Error("Verifique su conexión a internet")
        } catch (e: Exception) {
            Log.e("Repo registrar wallet - Generic", e.message.toString())
            Result.Error("Ocurrió un error al registrar la wallet");
        }
    }

}