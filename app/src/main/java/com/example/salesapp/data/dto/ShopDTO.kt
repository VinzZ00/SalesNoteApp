package com.example.salesapp.data.dto


public data class ShopDTO(
    var name : String,
    var address : String,
    var phoneNumber : String,
    var orders : List<OrderDTO> = emptyList()
)