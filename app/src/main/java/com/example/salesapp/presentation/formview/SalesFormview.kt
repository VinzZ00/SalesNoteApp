package com.example.salesapp.presentation.formview

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.chargemap.compose.numberpicker.ListItemPicker
import com.example.salesapp.data.dto.ItemOrderDTO
import com.example.salesapp.data.dto.OrderDTO
import com.example.salesapp.data.dto.ShopDTO
import com.example.salesapp.data.dto.WebResponse
import com.example.salesapp.model.ItemOrder
import com.example.salesapp.model.QuantityUnit
import com.example.salesapp.presentation.formview.SalesFormViewModel.ordersResponsibility.appendOrder
import com.example.salesapp.presentation.formview.SalesFormViewModel.ordersResponsibility.setShop
import com.example.salesapp.presentation.formview.SalesFormViewModel.ordersResponsibility.updateOrder
import com.example.salesapp.presentation.formview.SalesFormViewModel.productItemOrderResponsibility.changeProductName
import com.example.salesapp.presentation.formview.SalesFormViewModel.productItemOrderResponsibility.changeProductQuantity
import com.example.salesapp.presentation.formview.SalesFormViewModel.productItemOrderResponsibility.changeProductQuantityUnit
import com.example.salesapp.repository.RestRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun salesForm(
    navController: NavController,
    restRepository: RestRepository
) {
    // ViewModel
    var viewModel : SalesFormViewModel = viewModel(factory = SalesFormViewModel.provideFactory(restRepository))

    // States Flow Variables
    var orders = viewModel.order.collectAsState();
    var productName = viewModel.productName.collectAsState();
    var productQuantity = viewModel.productQuantity.collectAsState()
    var quantityUnit = viewModel.quantityUnit.collectAsState()
    var shop : State<ShopDTO?> = viewModel.selectedShop.collectAsState()

    var coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)

        ) {
            // MARK: Content
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .wrapContentWidth()
                ) {
                    Text(
                        text = "Penjualan ",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(end = 5.dp)
                    )

                    DropDownList(viewModel = viewModel, selectedShopDTO = shop) {
                        viewModel.setShop(it)
                    }
                }
                IconButton(
                    enabled = shop.value != null && shop.value!!.name != "" && orders.value.isNotEmpty(),
                    onClick = {

                        val dtf = SimpleDateFormat("yyyy-MM-dd")
                        // TODO save the record
                        var orderDTO = OrderDTO(
                            id = null,
                            totalAmount = (orders.value.map { it.price ?: 0.0 } as List<Int>).sum().toDouble(),
                            status = "On Progress",
                            dateOrdered = dtf.format(Date()),
                            shopId = UUID.fromString(shop.value!!.id)
                        )
                        try {
                            coroutineScope.launch {
                                isLoading = true
                                var res = async {
                                    viewModel.repository.addOrder(orderDTO)
                                }

                                Log.d("Inside of the coroutine scope", "salesForm: res : ${res.await().statusCode}, ${res.await().data}, ${res.await().errMessage}")

                                var idString = res.await().data

                                orders.value.forEach {
                                    var orderItem = ItemOrderDTO(
                                        id = null,
                                        productName = it.productName,
                                        quantity = it.productQuantity ?: 1,
                                        price = (it.price ?: 0.0).toDouble(),
                                        order = OrderDTO(
                                            id = idString,
                                            totalAmount = (orders.value.map { it.price ?: 0.0 } as List<Int>).sum().toDouble(),
                                            status = "On Progress",
                                            dateOrdered = dtf.format(Date()),
                                            shopId = UUID.fromString(shop.value!!.id)
                                        )
                                    )

                                    try {
                                        val resp = viewModel.repository.addItemOrdered(orderItem)
                                        Log.d("Sales Didalam try", "salesForm: ${resp}")
                                    } catch (err : Exception) {
                                        Log.e("SalesForm", "Error saving order", err)
                                    } finally {
                                        isLoading = false
                                        navController.popBackStack()
                                        viewModel.clearForm()

                                    }
                                }
                            }
                        } catch (err : Exception) {
                            Log.e("SalesForm", "Error saving order", err)
                        }


                    }) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = "Save record")
                }
            }


            Spacer(modifier = Modifier
                .height(16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Text Field (product Name)
                OutlinedTextField(value = productName.value,
                    onValueChange = { viewModel.changeProductName(it) },
                    label = { Text("Product Name") },
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .fillMaxWidth(0.45f)
                )

                // Spinner (quantity)
                quantityField(
                    quantity = productQuantity.value,
                    quantityUnit = quantityUnit.value,
                    onQuantityChange = {
                        viewModel.changeProductQuantity(it);
                    },
                    onQuantityUnitChange = {
                        viewModel.changeProductQuantityUnit(it);
                    }
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                FilledTonalButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val order = ItemOrder(
                            productName = productName.value,
                            productQuantity = productQuantity.value.toIntOrNull() ?: 0,
                            quantityUnit = quantityUnit.value
                        )

                        viewModel.appendOrder(order);

//                    Log.d("Adding Order", "${ if (success) "order added successfully" else "failed to add the order"}")
                        Log.d("Adding Order", "orders : ${orders.value}")
                    },
                ) {
                    Text("Add")
                }
            }

            Divider(
                color = Color.LightGray
            )

            LazyColumn{
                itemsIndexed(orders.value) {ind, item ->
                    var editing by remember { mutableStateOf(false) }

                    if (editing) {
                        EditableListItem(item = item, onEditComplete = {
                            viewModel.updateOrder(it, orders.value.get(ind))
                            Log.d("Order has been Change?", "${orders.value}")
                            editing = false
                        }) {
                            editing = false
                        }
                    } else {
                        ListItemContent(order = item) {
                            editing = true
                        }
                    }
                }
            }
        }

        if (isLoading) {
            WaitDialog()
        }
    }
}

@Composable
fun WaitDialog() {
    AlertDialog(
        onDismissRequest = { /* Do nothing to prevent dismissing */ },
        title = {
            Text(text = "Please Wait")
        },
        text = {
            Text("Your request is being processed. This might take a while.")
        },
        confirmButton = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Please Wait")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun quantityField(
    quantity : String,
    quantityUnit : QuantityUnit,
    modifier : Modifier = Modifier,
    onQuantityChange : (newQuant : String) -> Unit,
    onQuantityUnitChange: (newQUantUnit : QuantityUnit) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {

        TextField(
            value = quantity,
            onValueChange = { onQuantityChange(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent
            ),

            modifier = Modifier
                .fillMaxWidth(0.4f)
        )

        Spacer(
            Modifier
            .width(20.dp)
        )

        ListItemPicker<QuantityUnit>(
            label = { it.quantUnit },
            value = quantityUnit,
            onValueChange = { onQuantityUnitChange(it) },
            list = enumValues<QuantityUnit>().toList(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableListItem(item : ItemOrder, onEditComplete : (newItem : ItemOrder) -> Unit, onCancel : () -> Unit) {
    var productName by remember { mutableStateOf(item.productName) }
    var productQuantity by remember { mutableStateOf<String>(item.productQuantity.toString()) }
    var productPrice by remember { mutableStateOf<String>((item.price ?: 0).toString()) }
    var quantityUnit by remember { mutableStateOf<QuantityUnit>(item.quantityUnit) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.3f),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            TextField(
                value = productName,
                onValueChange = { productName = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent
                )
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
//                    .fillMaxHeight()
//                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextField(
                    value = productQuantity,
                    onValueChange = { productQuantity = it},
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f)
                        .fillMaxHeight(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),

                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent
                    )

                )


                TextField(
                    value = productPrice,
                    onValueChange = { productPrice = it},
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f)
                        .fillMaxHeight(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        }

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .weight(0.01f)
        )

        ListItemPicker<QuantityUnit>(
            label = { it.quantUnit },
            value = quantityUnit,
            onValueChange = { quantityUnit = it },
            list = enumValues<QuantityUnit>().toList(),
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)

        )

        IconButton(onClick = {
            var newItem = item
            newItem.quantityUnit = quantityUnit
            newItem.productQuantity = productQuantity.toIntOrNull() ?: item.productQuantity
            newItem.productName = productName
            newItem.price = productPrice.toIntOrNull() ?: item.price
            onEditComplete(newItem)
        }) {
            Icon(imageVector = Icons.Default.Done,
                contentDescription = "Done"
            )
        }

        IconButton(onClick = { onCancel() }) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Cancel"
            )
        }
    }
}


@Composable
fun ListItemContent(order : ItemOrder, onEditClick : () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Text(
            order.productName,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
        )

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .weight(0.1f))

        Text(
            order.productQuantity.toString(),
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)
        )

        Spacer(modifier = Modifier.fillMaxWidth(0.1f))

        Text(
            (order.price ?: 0).toString(),
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)
        );

        Spacer(modifier = Modifier.fillMaxWidth(0.1f))

        Text(
            order.quantityUnit.quantUnit,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)
        )

        IconButton(onClick = onEditClick) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit"
            )
        }

    }
}

@Composable
fun DropDownList(viewModel: SalesFormViewModel, selectedShopDTO : State<ShopDTO?> ,onselectedShop : (ShopDTO) -> Unit) {

    var expanded by remember { mutableStateOf<Boolean>(false) }
    var shopList by remember {
        mutableStateOf<WebResponse<List<ShopDTO>>>(
            WebResponse(
                emptyList(),
                "",
                ""
            )
        )
    }
//    var shopList : WebResponse<List<ShopDTO>> = WebResponse(emptyList(), "", "");

    val coroutineScope = rememberCoroutineScope()
    var verticalScrollState = rememberScrollState()

    Box {

        if (expanded) {
            // TODO: Implement drop down list item
            Popup {
                Row(verticalAlignment = Alignment.Top) {
                    Column(
                        verticalArrangement = Arrangement.Top ,
                        modifier = Modifier
                            .height(200.dp)
                            .verticalScroll(verticalScrollState)
                    ) {
                        shopList.data.forEach {
                            Text(
                                it.name,
                                style = TextStyle(fontSize = 20.sp),
                                modifier = Modifier
                                    .clickable {
                                        onselectedShop(it)
                                        expanded = !expanded
                                    }
                                    .padding(bottom = 5.dp)
                            )
                        }
                    }
                    IconButton(
                        modifier = Modifier.height(20.dp),
                        onClick = {
                        expanded = !expanded

                        coroutineScope.launch {
                            try {
                                shopList = viewModel.repository.getAllShop()
                                Log.d(
                                    "ShopListData",
                                    "DropDownList: ${shopList.errMessage}, ${shopList.statusCode} "
                                )
                            } catch (error: Exception) {
                                Log.e("DropDownList", "DropDownList: Error fetching shops", error)
                            }
                        }
                    }) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "extend"
                        )
                    }
                }
            }
        } else {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    if (selectedShopDTO.value != null && selectedShopDTO.value!!.name != "") selectedShopDTO.value!!.name else "Select Shop First",
                    style = TextStyle(fontSize = 20.sp)
                )

                IconButton(
                    modifier = Modifier.height(20.dp),
                    onClick = {
                    expanded = !expanded

                    coroutineScope.launch {
                        try {
//                    var shop = restRepository.getAllShop()
                            shopList = viewModel.repository.getAllShop()
                            Log.d(
                                "ShopListData",
                                "DropDownList: ${shopList.errMessage}, ${shopList.statusCode} "
                            )
                        } catch (error: Exception) {
                            Log.e("DropDownList", "DropDownList: Error fetching shops", error)
                        }
                    }
                }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "extend"
                    )
                }
            }
        }
    }
}

