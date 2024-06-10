package com.example.salesapp.data.network.shopnetwork

import com.example.salesapp.data.dto.ShopDTO
import com.example.salesapp.data.network.ApiService
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ShopApiService : ApiService<ShopDTO> {

    @GET("api/shop/getshops")
    override suspend fun getAll(): List<ShopDTO>

    @GET("api/shop/getshop")
    override suspend fun get(@Query("id") identifier: String): ShopDTO

    @POST("api/shop/deleteshop")
    override suspend fun delete(@Query("id") identifier: String): Boolean

    @POST("api/shop/updateshop")
    override suspend fun update(@Body t: ShopDTO): Boolean

    @POST("api/shop/addshop")
    override suspend fun add(@Body t: ShopDTO): Boolean

}