package com.example.pocantelop.core.entities.io

import com.example.pocantelop.core.entities.ObtenerTarjetaResult
import com.example.pocantelop.core.entities.Tarjeta
import com.example.pocantelop.core.entities.Wallet
import com.google.gson.annotations.SerializedName

data class ObtenerTarjetaAGenerarOut (
    @SerializedName(value = "ObtenerTarjetaAGenerarResult")
    var result: ObtenerTarjetaResult
)
