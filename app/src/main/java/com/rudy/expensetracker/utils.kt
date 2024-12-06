package com.rudy.expensetracker


import android.util.Log
import androidx.compose.material3.SnackbarDuration
import java.text.SimpleDateFormat
import java.util.Locale

object utils {

    fun formatDate(dateInMillis:Long): String {

        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }

    sealed class SnackbarEvent{
        data class ShowSnackbar(
            val message:String,
            val duration: SnackbarDuration = SnackbarDuration.Short
        ):SnackbarEvent()

        data object NavigateUp:SnackbarEvent()

        fun Int.TimeToString():String{
            return this.toString().padStart(length = 2, padChar = '0')
        }
    }

    fun handleException(tag: String, message: String, exception: Exception?) {
        // Log the error
        Log.e(tag, message, exception)

        // Optionally, you can show a snackbar or toast
        SnackbarEvent.ShowSnackbar("Error: ${exception?.localizedMessage ?: "Unknown error"}")
    }


}