package com.example.pocantelop.core.entities

import com.google.gson.annotations.SerializedName

data class Tarjeta(
    var idTarjeta: String,
    @SerializedName(value = "IdNotificacion")
    var idNotificacion: String,
    @SerializedName(value = "IdWallet")
    var walletId: String,
    @SerializedName(value = "Bin")
    var bin: String
)
