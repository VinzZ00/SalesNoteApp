package com.example.salesapp.data.dto

import java.util.UUID

data class ItemOrderDTO(
    val id : UUID,
    var name : String,
    var quantity : Int,
    var price : Double
//    var orderId : UUID
)