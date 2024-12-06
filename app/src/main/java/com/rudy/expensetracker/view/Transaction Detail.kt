package com.rudy.expensetracker.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rudy.expensetracker.ViewModel.MainViewModel
import com.rudy.expensetracker.ui.theme.background
import com.rudy.expensetracker.utils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDetailScreen(title: String, amount: String, category: String, date: Long?,id:String,navController: NavController) {

    val viewModel: MainViewModel = viewModel()
    val onEditClick: () -> Unit = {}
    val onBackClick: () -> Unit = { navController.popBackStack() }

    // Updated delete click handler
    val onDeleteClick: () -> Unit = {
        // Call deleteExpense from the viewModel
        viewModel.deleteExpense(id)

        // Navigate back to the Dashboard after deleting the expense
        navController.navigate("/dashboard") {
            // Ensure that the user can't navigate back to the current screen after deletion
            popUpTo("/dashboard") { inclusive = true }
        }
    }
    Scaffold(modifier = Modifier.fillMaxSize(1f),
        topBar = {
            TopAppBar(
                title = { Text("Expense", fontWeight = FontWeight.Medium) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onEditClick) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color(0xFF6B4EFF)
                        )
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFFFF4E4E)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = background) // Apply custom background color here
                .padding(paddingValues)
                .padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Expense Number and Date
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, MaterialTheme.shapes.medium)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    "Expense #12",
                    color = Color(0xFF6B4EFF),
                    fontSize = 15.sp
                )
                Text(
                    utils.formatDate(date!!),
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }

            // Description Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, MaterialTheme.shapes.medium)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Description",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    title,
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
            }

            // Total Amount Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, MaterialTheme.shapes.medium)
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Total Amount",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    amount,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


