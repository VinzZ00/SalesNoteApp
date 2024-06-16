package com.example.salesapp.data.dto

data class ItemOrderDTO(
    val id : String?,
    var productName : String,
    var quantity : Int,
    var price : Double,
    var order : OrderDTO
)