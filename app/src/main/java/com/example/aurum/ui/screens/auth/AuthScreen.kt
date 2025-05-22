@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.aurum.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AuthScreen(
    onAuthenticated: () -> Unit,
    onBack: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var isLogin by remember { mutableStateOf(true) }
    val loginState by viewModel.loginState.collectAsState()
    val signUpState by viewModel.signUpState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = if (isLogin) "Welcome Back" else "Create Account",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 28.sp,
                    color = Color.Black
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(onClick = { isLogin = true }) {
                Text(
                    text = "Log In",
                    fontSize = 16.sp,
                    color = if (isLogin) Color.Black else Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(24.dp))
            TextButton(onClick = { isLogin = false }) {
                Text(
                    text = "Sign Up",
                    fontSize = 16.sp,
                    color = if (!isLogin) Color.Black else Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isLogin) {
            LoginContent(
                loginState = loginState,
                onLoginClick = { loginOrEmail, pass ->
                    viewModel.login(loginOrEmail, pass)
                },
                onLoginSuccess = onAuthenticated
            )
        } else {
            SignUpContent(
                signUpState = signUpState,
                onSignUpClick = { email, username, phone, dateOfBirth, password ->
                    viewModel.register(email, username, phone, password, dateOfBirth)
                }
            )
        }
    }
}

@Composable
fun LoginContent(
    loginState: LoginUiState,
    onLoginClick: (String, String) -> Unit,
    onLoginSuccess: () -> Unit
) {
    var emailOrUsername by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Hello there,\nLogin to your account",
            fontSize = 20.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TextField(
            value = emailOrUsername,
            onValueChange = { emailOrUsername = it },
            label = { Text("Username or E-mail") },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFF8F8F8),
                textColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFF8F8F8),
                textColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { onLoginClick(emailOrUsername, password) },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Text("Login", color = Color.White, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        when (loginState) {
            is LoginUiState.Loading -> Text("Logging in...", color = Color.Gray)
            is LoginUiState.Error -> Text("Error: ${loginState.error}", color = Color.Red)
            is LoginUiState.Success -> onLoginSuccess()
            else -> {}
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Don't have an account? Sign up",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun SignUpContent(
    signUpState: SignUpUiState,
    onSignUpClick: (String, String, String, String, String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Sign up\nCreate your new account",
            fontSize = 20.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TextField(
            value = username,
            onValueChange = {
                val filtered = it.filter { c -> c.isLetterOrDigit() || c == '_' }
                username = filtered
            },
            label = { Text("Username") },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFF8F8F8),
                textColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFF8F8F8),
                textColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = phone,
            onValueChange = {
                val digits = it.filter { d -> d.isDigit() }
                phone = if (digits.length <= 9) digits else digits.take(9)
            },
            label = { Text("Phone (9 digits)") },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFF8F8F8),
                textColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = dateOfBirth,
            onValueChange = {
                var filtered = it.filter { c -> c.isDigit() || c == '-' }
                if (filtered.length == 4 && !filtered.endsWith("-")) filtered += "-"
                if (filtered.length == 7 && !filtered.endsWith("-")) filtered += "-"
                if (filtered.length > 10) filtered = filtered.take(10)
                dateOfBirth = filtered
            },
            label = { Text("Date of Birth (YYYY-MM-DD)") },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFF8F8F8),
                textColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password (>= 8 chars)") },
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFF8F8F8),
                textColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Re-type Password") },
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFF8F8F8),
                textColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                errorMessage = ""
                if (username.isBlank()) {
                    errorMessage = "Username is required."
                } else if (!email.contains("@") || !email.contains(".")) {
                    errorMessage = "Invalid e-mail address."
                } else if (phone.length != 9) {
                    errorMessage = "Phone must have exactly 9 digits."
                } else if (dateOfBirth.length != 10 || dateOfBirth[4] != '-' || dateOfBirth[7] != '-') {
                    errorMessage = "Birth date must be in YYYY-MM-DD format."
                } else if (password.length < 8) {
                    errorMessage = "Password must be at least 8 characters."
                } else if (password != confirmPassword) {
                    errorMessage = "Passwords do not match."
                }
                if (errorMessage.isEmpty()) {
                    onSignUpClick(
                        email.trim(),
                        username.trim(),
                        phone.trim(),
                        dateOfBirth.trim(),
                        password
                    )
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Text("Sign up", color = Color.White, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = Color.Red, modifier = Modifier.padding(8.dp))
        }
        when (signUpState) {
            is SignUpUiState.Loading -> Text("Signing up...", color = Color.Gray)
            is SignUpUiState.Error -> Text("Error: ${(signUpState as SignUpUiState.Error).error}", color = Color.Red)
            is SignUpUiState.Success -> Text("Registration successful!", color = Color.Green)
            else -> {}
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Already have an account? Login",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}
