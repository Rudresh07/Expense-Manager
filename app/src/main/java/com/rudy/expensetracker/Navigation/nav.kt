package com.rudy.expensetracker.Navigation

import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.rudy.expensetracker.view.AddExpenseScreen
import com.rudy.expensetracker.view.DashboardScreen
import com.rudy.expensetracker.view.ExpenseDetailScreen
import com.rudy.expensetracker.view.Signin
import com.rudy.expensetracker.view.Signup

@Composable
fun NavHostScreen() {
    val navController = rememberNavController()
    val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser

    LaunchedEffect(currentUser) {
        Log.d("NavHostScreen", "Current user: $currentUser")
    }

    // Determine the start destination based on user's login state
    val startDestination = if (currentUser != null) "/dashboard" else "/signin"

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0) // Keeps consistent padding for content
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = "/signin") {
                Signin(navController)
            }
            composable(route = "/signup") {
                Signup(navController)
            }
            composable(route = "/dashboard") {
                DashboardScreen(navController)
            }
            composable(route = "/add") {
                AddExpenseScreen(navController, onBackClick = { navController.navigateUp() })
            }
            composable(
                route = "/transaction/{title}/{amount}/{category}/{date}/{id}",
            ) { backStackEntry ->
                val title = backStackEntry.arguments?.getString("title")
                val amount = backStackEntry.arguments?.getString("amount")
                val category = backStackEntry.arguments?.getString("category")
                val date = backStackEntry.arguments?.getString("date")
                val id = backStackEntry.arguments?.getString("id")

                ExpenseDetailScreen(
                    title = title.orEmpty(),
                    amount = amount.orEmpty(),
                    category = category.orEmpty(),
                    date = date?.toLong(),
                    id = id.orEmpty(),
                    navController
                )
            }
        }
    }
}

