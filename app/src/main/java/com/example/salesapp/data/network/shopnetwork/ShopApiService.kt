package com.example.salesapp.data.network.shopnetwork

import com.example.salesapp.data.dto.DeserializedJSONWebResponse
import com.example.salesapp.data.dto.FullFetchData
import com.example.salesapp.data.dto.ShopDTO
import com.example.salesapp.data.dto.WebResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ShopApiService
//    : ApiService<ShopDTO>
{

    @GET("api/shop/getshop")
     suspend fun getAll(): WebResponse<List<ShopDTO>>

     @GET("api/shop/getshop")
     suspend fun fetchAll() : WebResponse<List<FullFetchData>>

    @GET("api/shop/getshop")
     suspend fun get(@Query("id") identifier: String): WebResponse<ShopDTO>

    @POST("api/shop/deleteshop")
     suspend fun delete(@Query("id") identifier: String): DeserializedJSONWebResponse

    @POST("api/shop/updateshop")
     suspend fun update(@Body t: ShopDTO): DeserializedJSONWebResponse

    @POST("api/shop/addshop")
     suspend fun add(@Body t: ShopDTO): Boolean

}