package com.example.salesapp.data.dto


public data class ShopDTO(
    var id : String,
    var name : String,
    var address : String,
    var phoneNumber : String,
    var orders : List<OrderDTO> = emptyList()
)