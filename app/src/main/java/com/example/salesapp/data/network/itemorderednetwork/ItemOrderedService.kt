package com.example.salesapp.data.network.itemorderednetwork

import com.example.salesapp.data.dto.ItemOrderDTO
import com.example.salesapp.data.network.ApiService
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ItemOrderedService : ApiService<ItemOrderDTO> {

    @GET("api/item-order/getitem")
    override suspend fun get(@Query("orderId") identifier: String): ItemOrderDTO

    @POST("api/item-order/additem")
    override suspend fun add(@Body t: ItemOrderDTO): Boolean

    @POST("api/item-order/updateitem")
    override suspend fun update(@Body t: ItemOrderDTO): Boolean

    @GET("api/item-order/deleteitem")
    override suspend fun delete(@Query("id") identifier: String): Boolean
}