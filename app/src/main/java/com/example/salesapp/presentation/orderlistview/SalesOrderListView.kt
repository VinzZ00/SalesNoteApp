package com.example.salesapp.presentation.orderlistview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.salesapp.repository.RestRepository

@Composable
fun SalesOrderListView(
    restRepository: RestRepository,
    navigationController: NavController
) {1
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top Navigation Bar
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "List Of Order",
                style = MaterialTheme.typography.headlineSmall
            )

            // Add Order Button
            IconButton(
                onClick = {
                    navigationController.navigate("salesForm")
                }
            ) {
                
            }

        }
    }
}