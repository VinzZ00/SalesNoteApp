package com.example.salesapp.data.network.ordernetwork

import com.example.salesapp.data.dto.DeserializedJSONWebResponse
import com.example.salesapp.data.dto.OrderDTO
import com.example.salesapp.data.dto.WebResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface OrderApiService
//    : ApiService<OrderDTO>
{

    @GET("api/order/getorders")
    suspend fun get(@Query("shopId") identifier: String): WebResponse<OrderDTO>

    @GET("api/order/getorders")
    suspend fun getOrderBy(@Query("shopId") shopIdentifier : String, @Query("date")  dateOrder : String)

    @POST("api/order/addorder")
    suspend fun add(@Body t: OrderDTO): WebResponse<String>

    @POST("api/order/updateorder")
    suspend fun update(@Body t: OrderDTO) : DeserializedJSONWebResponse

    @POST("api/order/deleteorder")
    suspend fun delete(@Query("orderId") identifier: String): DeserializedJSONWebResponse
}