package com.example.pocantelop.core.entities.io

import com.google.gson.annotations.SerializedName

data class NotificarTarjetaOut(
    @SerializedName(value = "NotificarTarjetaResult")
    var notificarTarjetaResult: RespuestaGeneralOut
)
