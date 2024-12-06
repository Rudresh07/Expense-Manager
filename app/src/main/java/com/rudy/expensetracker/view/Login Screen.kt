package com.rudy.expensetracker.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rudy.expensetracker.ViewModel.AuthState
import com.rudy.expensetracker.ViewModel.AuthViewModel
import com.rudy.expensetracker.ui.theme.ButtonBlue
import com.rudy.expensetracker.ui.theme.InputFieldName
import com.rudy.expensetracker.ui.theme.TextHint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Signin(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Inject ViewModel using Koin or any dependency injection framework
    val viewModel: AuthViewModel = viewModel()

    val authState by viewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Error -> {
                val message = (authState as AuthState.Error).message
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
            is AuthState.Authenticated -> onLoginSuccess(navController)
            else -> Unit
        }
    }

    Scaffold(
        topBar = { TopBarsignin() }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Email input
            Text(
                text = "Email",
                fontWeight = FontWeight.W500,
                fontSize = 16.sp,
                fontFamily = FontFamily.SansSerif
            )
            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                modifier = Modifier.fillMaxWidth()
                    .height(60.dp)
                    .background(Color.Transparent)
                    .border(BorderStroke(1.dp, TextHint), shape = RoundedCornerShape(8.dp)),
                value = email,
                onValueChange = { email = it },
                shape = RoundedCornerShape(8.dp),
                placeholder = { Text(text = "User@email.com", fontFamily = FontFamily.SansSerif, color = TextHint) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = InputFieldName,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Password input
            Text(
                text = "Password",
                fontWeight = FontWeight.W500,
                fontSize = 16.sp,
                fontFamily = FontFamily.SansSerif
            )
            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                modifier = Modifier.fillMaxWidth()
                    .height(60.dp)
                    .background(Color.Transparent)
                    .border(BorderStroke(1.dp, TextHint), shape = RoundedCornerShape(8.dp)),
                value = password,
                onValueChange = { password = it },
                shape = RoundedCornerShape(8.dp),
                placeholder = { Text(text = "Your password", fontFamily = FontFamily.SansSerif, color = TextHint) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = InputFieldName,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
            ForgetPassword()

            Spacer(modifier = Modifier.height(56.dp))

            // Authentication state handling
            when (authState) {
                is AuthState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is AuthState.Error -> {
                    Text(
                        text = (authState as AuthState.Error).message,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                else -> {
                    Button(
                        modifier = Modifier.fillMaxWidth()
                            .height(60.dp)
                            .background(ButtonBlue, shape = RoundedCornerShape(8.dp)),
                        onClick = {
                            viewModel.login(email, password)
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(ButtonBlue)
                    ) {
                        Text("Log in", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            SignUpText(navController)
        }
    }
}

@Composable
fun ForgetPassword() {
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = ButtonBlue, fontSize = 15.sp)) {
            append("Forget Password?")
        }
    }

    ClickableText(
        text = annotatedString,
        style = TextStyle.Default,
        onClick = { offset ->
            // Handle click event
            println("Forget Password clicked at offset: $offset")
            // Add your navigation or action logic here
        }
    )
}

@Composable
fun SignUpText(navController: NavController) {
    val annotatedString = buildAnnotatedString {
        append("Don't have an account? ")

        pushStringAnnotation(tag = "SIGN_UP", annotation = "signup_action")
        withStyle(style = SpanStyle(color = ButtonBlue, fontWeight = FontWeight.Bold)) {
            append("Signup")
        }
        pop()
    }

    ClickableText(
        text = annotatedString,
        style = TextStyle(fontSize = 15.sp, color = TextHint),
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "SIGN_UP", start = offset, end = offset)
                .firstOrNull()?.let {
                    navController.navigate("signup")
                }
        }
    )
}

@Composable
fun TopBarsignin() {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text("Sign in", fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif)
        Spacer(modifier = Modifier.weight(1f))
    }
}

fun onLoginSuccess(navController: NavController) {
    // Navigate to the next screen after successful login
    navController.navigate("/dashboard"){
        popUpTo("signin") { inclusive = true }
    }


}
