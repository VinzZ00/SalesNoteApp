package com.example.salesapp.presentation.orderlistview

import androidx.lifecycle.ViewModel
import com.example.salesapp.data.dto.FullFetchData
import com.example.salesapp.data.dto.WebResponse
import com.example.salesapp.model.ItemOrder
import com.example.salesapp.model.Order
import com.example.salesapp.model.QuantityUnit
import com.example.salesapp.model.Shop
import com.example.salesapp.repository.RestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SalesOrderListViewModel(
   val repository: RestRepository
) : ViewModel() {

    var _OrderDatas = MutableStateFlow<Map<Shop, List<Order>>>(emptyMap())
    var orders = _OrderDatas.asStateFlow()

    object orderResponsibility {
        suspend fun SalesOrderListViewModel.getAllOrder() {
            var unGroupedOrderData : WebResponse<List<FullFetchData>> = repository.fetchAllData()
            var orderData = unGroupedOrderData.data.filter {
                it.orders.isNotEmpty()
            }

            val orderMap = mutableMapOf<Shop, List<Order>>()

            orderData.forEach{
                orderMap[
                    Shop(
                        it.name,
                        it.address,
                        it.phoneNumber
                    )
                ] = it.orders.map {
                    Order(
                        it.id,
                        it.totalAmount,
                        it.dateOrdered,
                        it.status,
                        it.items.map {
                            ItemOrder(
                                it.name,
                                it.quantity,
                                it.price,
                                QuantityUnit.PCS
                            )
                        }
                    )
                }
            }
            _OrderDatas.value = orderMap
        }
    }

}