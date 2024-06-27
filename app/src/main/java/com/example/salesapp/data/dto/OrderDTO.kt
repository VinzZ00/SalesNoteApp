package com.example.salesapp.data.dto

import java.util.UUID

data class OrderDTO(
    var id : String?,
    var totalAmount : Double,
    var status : String,
    var dateOrdered : String,
    var shopId : UUID
)

