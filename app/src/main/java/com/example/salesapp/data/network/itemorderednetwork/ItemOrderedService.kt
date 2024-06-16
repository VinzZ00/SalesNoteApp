package com.example.salesapp.data.network.itemorderednetwork

import com.example.salesapp.data.dto.DeserializedJSONWebResponse
import com.example.salesapp.data.dto.ItemOrderDTO
import com.example.salesapp.data.dto.WebResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ItemOrderedService
//    : ApiService<ItemOrderDTO>
{

    @GET("api/item-order/getitem")
     suspend fun get(@Query("orderId") identifier: String): WebResponse<ItemOrderDTO>

    @POST("api/item-order/additem")
     suspend fun add(@Body t: ItemOrderDTO): WebResponse<ItemOrderDTO>

    @POST("api/item-order/updateitem")
     suspend fun update(@Body t: ItemOrderDTO): DeserializedJSONWebResponse

    @GET("api/item-order/deleteitem")
     suspend fun delete(@Query("id") identifier: String): DeserializedJSONWebResponse
}