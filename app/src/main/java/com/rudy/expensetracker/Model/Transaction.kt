package com.rudy.expensetracker.Model

data class Transaction(
    val id: String = "",
    val title: String,
    val category: String,
    val date: Long,
    val amount: Double
)
