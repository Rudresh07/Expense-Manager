package com.rudy.expensetracker.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rudy.expensetracker.Model.Transaction
import com.rudy.expensetracker.ViewModel.MainViewModel
import com.rudy.expensetracker.utils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(navController: NavController,onBackClick: () -> Unit = {}) {
    var selectedTab by remember { mutableStateOf(0) }
    val date = remember { mutableStateOf(0L) }
    var description = remember { mutableStateOf("Electricity Bill") }
    var amount = remember { mutableStateOf("3500.0") }
    val dateDialogVisibility = remember { mutableStateOf(false) }

    val viewModel:MainViewModel = MainViewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Record Expense", fontWeight = FontWeight.Medium) },
                navigationIcon = {
                    IconButton(onClick ={navController.popBackStack()}) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F9FE))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Tab Selection
            ExpenseIncomeToggle(selectedTab,{selectedTab = it})

            // Form Fields
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Date Field
                Column {
                    Text(
                        "DATE*",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    (if (date.value == 0L) "1/1/1971" else utils.formatDate(date.value))?.let {
                        OutlinedTextField(
                            value = it,
                            onValueChange = {},
                            modifier = Modifier
                                .clickable { dateDialogVisibility.value = true }
                                .fillMaxWidth(),
                            trailingIcon = {
                                Icon(
                                    Icons.Default.DateRange,
                                    contentDescription = "Select Date",
                                    tint = Color.Gray,
                                )
                            },
                            enabled = false,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.LightGray,
                                unfocusedContainerColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }

                // Description Field
                Column {
                    Text(
                        "DESCRIPTION*",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = description.value,
                        onValueChange = { description.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.LightGray,
                            unfocusedContainerColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                // Amount Field
                Column {
                    Text(
                        "Total Amount",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "₹",
                            modifier = Modifier.align(Alignment.CenterVertically),
                            fontSize = 18.sp
                        )

                        OutlinedTextField(
                            value = amount.value,
                            onValueChange = { amount.value = it },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.LightGray,
                                unfocusedContainerColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Save Button
            Button(

                onClick = {
                    val expenseType = if(selectedTab==0)"Expense" else "Income"
                    val expense = Transaction(
                        title = description.value,
                        amount = amount.value.toDouble(),
                        date = date.value,
                        category = expenseType
                    )
                    viewModel.addExpense(expense)
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6B4EFF)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Save", fontSize = 16.sp)
            }

            if (dateDialogVisibility.value) {
                DatePicker(
                    onDateSelected = {
                        date.value = it
                        dateDialogVisibility.value = false
                    },
                    onDismiss = { dateDialogVisibility.value = false }
                )
            }



        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(onDateSelected: (date:Long) -> Unit,
               onDismiss : ()->Unit)
{

    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis ?:0L

    DatePickerDialog(
        onDismissRequest = {onDismiss() },
        confirmButton = { TextButton(onClick = { onDateSelected(selectedDate) }) {
            Text("Confirm")
        }
        },
        dismissButton = {
            TextButton(onClick = { onDateSelected(selectedDate)}) {
                Text(text = "Dismiss")
            }
        }
    )
    {
        androidx.compose.material3.DatePicker(state = datePickerState)
    }
}

@Composable
fun ExpenseIncomeToggle(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        listOf("Expense", "Income").forEachIndexed { index, title ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .background(
                        color = if (selectedTab == index) Color(0xFF6B4EFF).copy(alpha = 0.1f) else Color.White,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = if (selectedTab == index) Color(0xFF6B4EFF) else Color.Gray,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .clickable { onTabSelected(index) },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Circle indicator
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .border(
                                width = 2.dp,
                                color = if (selectedTab == index) Color(0xFF6B4EFF) else Color.Gray,
                                shape = CircleShape
                            )
                            .background(
                                color = if (selectedTab == index) Color(0xFF6B4EFF) else Color.Transparent,
                                shape = CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // Text
                    Text(
                        text = title,
                        color = if (selectedTab == index) Color(0xFF6B4EFF) else Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
