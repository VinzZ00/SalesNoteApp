package com.example.salesapp.presentation.formview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.salesapp.data.dto.ShopDTO
import com.example.salesapp.model.ItemOrder
import com.example.salesapp.model.QuantityUnit
import com.example.salesapp.repository.RestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class SalesFormViewModel(repository : RestRepository) : ViewModel() {


    private var _order  = MutableStateFlow<List<ItemOrder>>(emptyList());
    private var _productName = MutableStateFlow<String>("");
    private var _productQuantity = MutableStateFlow<String>("");
    private var _quantityUnit = MutableStateFlow<QuantityUnit>(QuantityUnit.PCS);
    private var _selectedShop = MutableStateFlow<ShopDTO?>(null);

    val order  = _order.asStateFlow()
    val productName = _productName.asStateFlow()
    val productQuantity = _productQuantity.asStateFlow()
    val quantityUnit = _quantityUnit.asStateFlow()
    val selectedShop : StateFlow<ShopDTO?> = _selectedShop.asStateFlow()
    val repository = repository

    fun clearForm() {
        _order.value = emptyList()
        _productName.value = ""
        _productQuantity.value = ""
        _quantityUnit.value = QuantityUnit.PCS
        _selectedShop.value = null
    }

    object ordersResponsibility {
        fun SalesFormViewModel.appendOrder(newOrder: ItemOrder) {
            _order.value = _order.value + newOrder;
        }

        fun SalesFormViewModel.deleteOrder(order : ItemOrder) {
            _order.value = _order.value - order
        }

        fun SalesFormViewModel.updateOrder(newOrder: ItemOrder, oldOrder: ItemOrder) {
            _order.value = _order.value.map {
                if (it == oldOrder) newOrder else it
            }
        }

        fun SalesFormViewModel.setShop(shop : ShopDTO) {
            _selectedShop.value = shop
        }

    }

    object productItemOrderResponsibility {
        fun SalesFormViewModel.changeProductName(newProductName : String) {
            _productName.value = newProductName
        }

        fun SalesFormViewModel.changeProductQuantity(newQuantity : String) {
            _productQuantity.value = newQuantity
        }

        fun SalesFormViewModel.changeProductQuantityUnit(newQuantityUnit: QuantityUnit) {
            _quantityUnit.value = newQuantityUnit
        }
    }


    companion object {
        fun provideFactory(repository: RestRepository) : ViewModelProvider.Factory {
            return viewModelFactory {
                initializer { SalesFormViewModel(repository) }
            }
        }
    }
}
