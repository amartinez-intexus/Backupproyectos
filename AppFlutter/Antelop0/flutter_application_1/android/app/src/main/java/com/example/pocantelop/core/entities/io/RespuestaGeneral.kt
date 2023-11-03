package com.example.pocantelop.core.entities.io

import com.google.gson.annotations.SerializedName

abstract class RespuestaGeneral (
    @SerializedName(value = "Codigo")
    val codigo: String = "",
    @SerializedName(value = "Mensaje")
    val mensaje: String = ""
)
