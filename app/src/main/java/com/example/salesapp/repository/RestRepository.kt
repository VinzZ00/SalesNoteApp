package com.example.salesapp.repository

import com.example.salesapp.data.dto.ItemOrderDTO
import com.example.salesapp.data.dto.OrderDTO
import com.example.salesapp.data.dto.ShopDTO
import com.example.salesapp.data.network.NetworkServiceImpl

class RestRepository {
    private val orderApiService = NetworkServiceImpl.getInstance().getOrderService()
    private val shopApiService = NetworkServiceImpl.getInstance().getShopService()
    private val itemOrderedService = NetworkServiceImpl.getInstance().getItemOrderedService()

    // MARK: SHOP API
    suspend fun getAllShop() = shopApiService.getAll()
    suspend fun getShop(shopId: String) = shopApiService.get(shopId)
    suspend fun deleteShop(shopId: String) = shopApiService.delete(shopId)
    suspend fun updateShop(shop: ShopDTO) = shopApiService.update(shop)
    suspend fun addShop(shop: ShopDTO) = shopApiService.add(shop)

    // MARK: ORDER API
    suspend fun getOrder(shopId: String) = orderApiService.get(shopId)
    suspend fun addOrder(order: OrderDTO) = orderApiService.add(order)
    suspend fun updateOrder(order: OrderDTO) = orderApiService.update(order)
    suspend fun deleteOrder(orderId: String) = orderApiService.delete(orderId)

    // MARK : ITEM ORDERED API
    suspend fun getItemOrdered(orderId: String) = itemOrderedService.get(orderId)
    suspend fun addItemOrdered(itemOrder: ItemOrderDTO) = itemOrderedService.add(itemOrder)
    suspend fun updateOrderedItem(itemOrder : ItemOrderDTO) = itemOrderedService.update(itemOrder)
    suspend fun deleteOrderedItem(id : String ) = itemOrderedService.delete(id)
}