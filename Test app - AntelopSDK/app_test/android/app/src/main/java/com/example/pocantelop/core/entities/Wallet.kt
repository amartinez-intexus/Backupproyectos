package com.example.pocantelop.core.entities

import com.google.gson.annotations.SerializedName

data class Wallet (
    @SerializedName(value = "IdWallet")
    var idWallet: String,
    @SerializedName(value = "Documento")
    var documento: String = ""
)
