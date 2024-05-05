package com.example.salesapp.view

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chargemap.compose.numberpicker.ListItemPicker
import com.example.salesapp.model.ItemOrder
import com.example.salesapp.model.QuantityUnit


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun salesForm(shopName : String, navController: NavController) {

    // States variables
    var orders by remember { mutableStateOf(mutableStateListOf<ItemOrder>()) }
    var productName by remember { mutableStateOf("") }
    var productQuantity = remember { mutableStateOf("") }
    var quantityUnit = remember { mutableStateOf<QuantityUnit>(QuantityUnit.PCS) }


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
            Text(
                text = "Penjualan $shopName",
                style = MaterialTheme.typography.headlineSmall
            )
            IconButton(onClick = {
                // TODO save the record
                Log.d("Saving records", "salesForm: saving records button tapped")
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
            OutlinedTextField(value = productName,
                onValueChange = { productName = it },
                label = { Text("Product Name") },
                modifier = Modifier
                    .padding(end = 10.dp)
                    .fillMaxWidth(0.55f)
            )

            // Spinner (quantity)
            quantityField(
                quantity = productQuantity,
                quantityUnit = quantityUnit
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
                        productName = productName,
                        productQuantity = productQuantity.value.toIntOrNull() ?: 0,
                        quantityUnit = quantityUnit.value
                    )

                    val success = orders.add(order)

                    Log.d("Adding Order", "${ if (success) "order added successfully" else "failed to add the order"}")
                    Log.d("Adding Order", "orders : ${orders.toList()}")
                },
            ) {
                Text("Add")
            }
        }

        Divider(
            color = Color.LightGray
        )

        LazyColumn{
            itemsIndexed(orders) {ind, item ->
                var editing by remember { mutableStateOf(false) }

                if (editing) {
                    EditableListItem(item = item, onEditComplete = {
                        orders.set(ind, it)
                        Log.d("Order has been Change?", "${orders.toList()}")
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun quantityField(quantity : MutableState<String>, quantityUnit : MutableState<QuantityUnit>, modifier : Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {

        TextField(
            value = quantity.value.toString(),
            onValueChange = { quantity.value = it},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent
            ),

            modifier = Modifier
                .fillMaxWidth(0.3f)
        )

        Spacer(
            Modifier
            .width(20.dp)
        )

        ListItemPicker<QuantityUnit>(
            label = { it.quantUnit },
            value = quantityUnit.value,
            onValueChange = { quantityUnit.value = it },
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

