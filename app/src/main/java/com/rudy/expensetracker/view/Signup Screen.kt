package com.rudy.expensetracker.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rudy.expensetracker.ViewModel.AuthViewModel
import com.rudy.expensetracker.ui.theme.ButtonBlue
import com.rudy.expensetracker.ui.theme.InputFieldName
import com.rudy.expensetracker.ui.theme.TextHint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Signup(navController: NavController) {
    val viewModel: AuthViewModel = AuthViewModel()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Scaffold(
        topBar = { TopBar(navController) }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(
                    top = 28.dp + padding.calculateTopPadding(),
                    start = 20.dp,
                    end = 10.dp
                )
        ) {
            Text(
                text = "Name",
                fontWeight = FontWeight.W500,
                fontSize = 16.sp,
                fontFamily = FontFamily.SansSerif
            )
            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                modifier = Modifier.fillMaxWidth()
                    .height(60.dp)
                    .background(Color.Transparent)
                    .border(BorderStroke(1.dp, TextHint),
                        shape = RoundedCornerShape(8.dp) ),
                value = name,
                onValueChange = { name = it },
                shape = RoundedCornerShape(8.dp),
                placeholder = { Text(text="Your Name", fontFamily = FontFamily.SansSerif, color = TextHint) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = InputFieldName,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,

                    )
            )
            Spacer(modifier = Modifier.height(20.dp))

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
                    .border(BorderStroke(1.dp, TextHint),
                        shape = RoundedCornerShape(8.dp) ),
                value = email,
                onValueChange = { email = it },
                shape = RoundedCornerShape(8.dp),
                placeholder = { Text(text="user@email.com", fontFamily = FontFamily.SansSerif, color = TextHint) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = InputFieldName,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,

                    )
            )
            Spacer(modifier = Modifier.height(20.dp))

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
                    .border(BorderStroke(1.dp, TextHint),
                        shape = RoundedCornerShape(8.dp) ),
                value = password,
                onValueChange = { password = it },
                shape = RoundedCornerShape(8.dp),
                placeholder = {  Text(text="Your password", fontFamily = FontFamily.SansSerif, color = TextHint) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = InputFieldName,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,

                    )
            )

            Spacer(modifier = Modifier.height(128.dp))

            Button(modifier = Modifier.fillMaxWidth()
                .height(60.dp)
                .background(ButtonBlue, shape = RoundedCornerShape(8.dp)),
                onClick = { viewModel.signUp(email,password) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(ButtonBlue)
            ) {
                Text("Sign up", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif)
            }
            Spacer(modifier = Modifier.height(16.dp))

            SignInText(navController)


        }
    }
}


@Composable
fun SignInText(navController: NavController) {
    val annotatedString = buildAnnotatedString {
        // Normal text
        append("Don't have an account? ")

        // Styled and clickable "Sign Up"
        pushStringAnnotation(tag = "SIGN_IN", annotation = "signup_action")
        withStyle(style = SpanStyle(color = ButtonBlue, fontWeight = FontWeight.Bold)) {
            append("Signin")
        }
        pop()
    }

    ClickableText(
        text = annotatedString,
        style = TextStyle(fontSize = 15.sp, color = TextHint), // Base style for normal text
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "SIGN_UP", start = offset, end = offset)
                .firstOrNull()?.let {
                    navController.navigate("/signin")
                }
        }
    )
}


@Composable
fun TopBar(navController: NavController){
    Row(modifier = Modifier.fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically)
    {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", modifier = Modifier.clickable { navController.popBackStack() })

        Spacer(modifier = Modifier.weight(1f))

        Text("Sign up", fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif)

        Spacer(modifier = Modifier.weight(1f))
    }
}
