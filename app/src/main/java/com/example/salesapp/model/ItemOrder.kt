package com.example.salesapp.model

enum class QuantityUnit(val quantUnit : String) {
    BUNGKUS("Bungkus"),
    CARTON("Carton"),
    PCS("pieces");
    companion object { fun allCases(): Array<QuantityUnit> = values() }
}

data class ItemOrder(
    var productName : String,
    var productQuantity : Int? = 0,
    var price : Int? = null,
    var quantityUnit : QuantityUnit
)