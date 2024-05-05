package com.example.salesapp

// External Library

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.salesapp.ui.theme.SalesAppTheme
import com.example.salesapp.view.salesForm


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SalesAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // TODO: Composable UI
                    var navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "salesForm") {
                        composable("salesForm") {
                            salesForm("Tomi Mart", navController)
                        }
                    }

                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

//    var quant = remember { mutableStateOf(0) }
//    var quantityUnit = remember { mutableStateOf(QuantityUnit.PCS) }

    SalesAppTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
//            salesForm(shopName = "Tomi Mart")

//            // Testing Custom Item List View
//            ListItemContent(order = ItemOrder("Product 1", 10, null, QuantityUnit.PCS)) {
//                Log.d("list Item edit button Clicked", "GreetingPreview: edit button works!")
//            }

//            // Testing Custom Editable List Item
//            EditableListItem(item = ItemOrder("Product 1", 10, null, QuantityUnit.PCS), onEditComplete = {
//                Log.d("list Item Done Editing", "GreetingPreview: Done Editing function works!")
//            }) {
//                Log.d("list Item Done Cancel", "GreetingPreview: Done Cancel function works!")
//            }
            // Testing Custom Number Picker
//            quantityField(quantity = quant, quantityUnit = quantityUnit)
        }
    }

}