package com.example.salesapp.data.network.ordernetwork

import com.example.salesapp.data.dto.OrderDTO
import com.example.salesapp.data.network.ApiService
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface OrderApiService : ApiService<OrderDTO> {
    @GET("api/order/getorders")
    override suspend fun get(@Query("shopId") identifier: String): OrderDTO
    @POST("api/order/addorder")
    override suspend fun add(@Body t: OrderDTO): Boolean

    @POST("api/order/updateorder")
    override suspend fun update(@Body t: OrderDTO) : Boolean

    @POST("api/order/deleteorder")
    override suspend fun delete(@Query("orderId") identifier: String): Boolean
}