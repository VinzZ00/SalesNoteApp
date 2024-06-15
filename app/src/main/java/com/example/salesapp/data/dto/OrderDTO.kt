package com.example.salesapp.data.dto

import java.util.Date
import java.util.UUID

data class OrderDTO(
    var totalAmount : Double,
    var status : String,
    var dateOrder : Date,
    var shopId : UUID
)

