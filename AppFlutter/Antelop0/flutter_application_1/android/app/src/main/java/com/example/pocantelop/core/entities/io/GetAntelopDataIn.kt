package com.example.pocantelop.core.entities.io

import com.example.pocantelop.core.entities.Recipient

data class GetAntelopDataIn(
    var recipient: Recipient,
    var issuerCardId: String,
    var binSize: Int = 8
)
