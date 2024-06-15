package com.example.salesapp.data.network

import com.example.salesapp.data.network.itemorderednetwork.ItemOrderedService
import com.example.salesapp.data.network.ordernetwork.OrderApiService
import com.example.salesapp.data.network.shopnetwork.ShopApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface NetworkService {
    fun getOrderService() : OrderApiService
    fun getShopService() : ShopApiService
    fun getItemOrderedService() : ItemOrderedService
}

class NetworkServiceImpl private constructor() : NetworkService {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://140.238.207.125:8081")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun getOrderService(): OrderApiService {
        return retrofit.create(OrderApiService::class.java)
    }

    override fun getShopService(): ShopApiService {
        return retrofit.create(ShopApiService::class.java)
    }

    override fun getItemOrderedService(): ItemOrderedService {
        return retrofit.create(ItemOrderedService::class.java)
    }


    companion object {
        @Volatile
        private var shared : NetworkService? = null

        fun getInstance() : NetworkService {
            return shared ?: synchronized(this) {
                shared ?: NetworkServiceImpl().also { shared = it }
            }
        }
    }
}