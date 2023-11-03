package com.example.pocantelop

import com.example.pocantelop.core.repositories.AntelopRepository
import com.example.pocantelop.core.repositories.TarjetaDigitalRepository
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module

val walletModule = module {

    factory { HttpLoggingInterceptor.Level.BODY }

    factory {
        val tarjetaDigitalRepository = TarjetaDigitalRepository(get())
        tarjetaDigitalRepository
    }

    factory {
        val antelopRepository = AntelopRepository(get())
        antelopRepository
    }

    single { WalletViewModel(get(), get()) }

}