package com.example.salesapp.data.dto

import java.util.Date

data class OrderDTO(
    var totalAmount : Double,
    var status : String,
    var dateOrder : Date,
    var items : List<ItemOrderDTO>
)

