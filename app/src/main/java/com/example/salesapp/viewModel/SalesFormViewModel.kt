import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.salesapp.model.ItemOrder
import com.example.salesapp.model.QuantityUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class SalesFormViewModel : ViewModel() {

    private var _order  = MutableStateFlow<List<ItemOrder>>(emptyList());
    private var _productName = MutableStateFlow<String>("");
    private var _productQuantity = MutableStateFlow<String>("");
    private var _quantityUnit = MutableStateFlow<QuantityUnit>(QuantityUnit.PCS);

    val order  = _order.asStateFlow()
    val productName = _productName.asStateFlow()
    val productQuantity = _productQuantity.asStateFlow()
    val quantityUnit = _quantityUnit.asStateFlow()

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
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SalesFormViewModel()
            }
        }
    }
}
