package com.example.pocantelop.core.entities

import com.example.pocantelop.core.entities.io.RespuestaGeneral
import com.google.gson.annotations.SerializedName

data class ObtenerTarjetaResult (
    @SerializedName(value = "TarjetaDigital")
    var tarjetaDigital: List<Tarjeta>
): RespuestaGeneral()
