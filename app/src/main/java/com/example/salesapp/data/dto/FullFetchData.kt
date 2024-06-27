package com.example.salesapp.data.dto



data class FullFetchData(
    val name : String,
    val address : String,
    val phoneNumber : String,
    val orders : List<OrderWithItems>
)

data class OrderWithItems(
    val id : String,
    val totalAmount : Double,
    val status : String,
    val dateOrdered : String,
    val items : List<ItemOrderFetchDTO>,
)

data class ItemOrderFetchDTO(
    val id : String,
    val name : String,
    val quantity : Int,
    val price : Double
)

