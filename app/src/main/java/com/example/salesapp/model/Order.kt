package com.example.salesapp.model

data class Order(
    var id : String,
    var totalAmount : Double,
    var dateOrder : String,
    var status : String,
    var orderItem : List<ItemOrder>
)
