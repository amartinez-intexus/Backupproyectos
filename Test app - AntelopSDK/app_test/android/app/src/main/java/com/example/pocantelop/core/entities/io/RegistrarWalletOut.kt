package com.example.pocantelop.core.entities.io

import com.google.gson.annotations.SerializedName

data class RegistrarWalletOut(
    @SerializedName(value = "RegistrarWalletResult")
    var registrarWalletResult: RespuestaGeneralOut
)
