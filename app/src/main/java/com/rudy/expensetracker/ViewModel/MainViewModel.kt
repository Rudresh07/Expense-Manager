package com.rudy.expensetracker.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.rudy.expensetracker.Model.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await



class MainViewModel : ViewModel() {
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    // StateFlow to hold expense data
    private val _expenses = MutableStateFlow<List<Transaction>>(emptyList())
    val expenses: StateFlow<List<Transaction>> = _expenses


    fun fetchExpenses() {
        viewModelScope.launch {
            try {
                val snapshot = firebaseFirestore.collection("Expenses").get().await()

                val fetchedExpenses = snapshot.documents.mapNotNull { doc ->
                    try {
                        Transaction(
                            id = doc.id,
                            title = doc.getString("title") ?: "Untitled",
                            category = doc.getString("category") ?: "Unknown",
                            date = doc.getLong("date") ?: 0L,
                            amount = doc.getDouble("amount") ?: 0.0
                        )
                    } catch (e: Exception) {
                        Log.e("MainViewModel", "Error processing document ${doc.id}: ${e.message}", e)
                        null // Exclude this document if an error occurs
                    }
                }

                _expenses.value = fetchedExpenses

            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching expenses: ${e.message}", e) // Log the error
            }
        }
    }



    // Function to add a new expense
    fun addExpense(expense: Transaction) {
        viewModelScope.launch {
            try {
                firebaseFirestore.collection("Expenses")
                    .add(expense)
                    .await()
                fetchExpenses() // Refresh the list after adding
            } catch (e: Exception) {
                // Handle exception
                e.printStackTrace()
            }
        }
    }

    // Function to delete an expense by ID
    fun deleteExpense(expenseId: String) {
        viewModelScope.launch {
            try {
                firebaseFirestore.collection("Expenses").document(expenseId)
                    .delete()
                    .await()
                fetchExpenses() // Refresh the list after deleting
            } catch (e: Exception) {
                // Handle exception
                e.printStackTrace()
            }
        }
    }
}
