package com.example.salesapp.data.dto

data class WebResponse<T> (
    var data : T,
    val errMessage : String,
    val statusCode : String
)


