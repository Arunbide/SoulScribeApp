package com.arb.soulscribe.ui_Layer.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.arb.soulscribe.R
import com.arb.soulscribe.utils.PasswordManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupPasswordScreen(navController: NavHostController) {
    val context = LocalContext.current
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var currentStep by remember { mutableStateOf(1) }
    var message by remember { mutableStateOf("Set a 4-digit PIN") }
    var errorMessage by remember { mutableStateOf("") }
    var isPasswordSet by remember { mutableStateOf(PasswordManager.isPasswordSet(context)) }

    if (isPasswordSet) {
        // Password already set - show management options
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121025))
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121025)
                )
            )

            Spacer(modifier = Modifier.height(60.dp))

            Image(
                painter = painterResource(R.drawable.lock),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Password Protection",
                fontSize = 24.sp,
                fontWeight = FontWeight.W700,
                fontFamily = FontFamily.SansSerif,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your app is already secured with a password",
                fontSize = 14.sp,
                fontWeight = FontWeight.W400,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(60.dp))

            Button(
                onClick = {
                    isPasswordSet = false
                    currentStep = 1
                    message = "Enter new 4-digit PIN"
                    password = ""
                    confirmPassword = ""
                    errorMessage = ""
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5854D4)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Change PIN",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    PasswordManager.removePassword(context)
                    navController.navigateUp()
                    message = "Password protection removed"
                    errorMessage = ""
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(44.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Red
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Remove Protection",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500
                )
            }
        }
    } else {
        // Password not set - show setup flow
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121025))
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentStep == 2) {
                            currentStep = 1
                            confirmPassword = ""
                            message = "Set a 4-digit PIN"
                            errorMessage = ""
                        } else {
                            navController.popBackStack()
                        }
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121025)
                )
            )

            Spacer(modifier = Modifier.height(40.dp))

            Image(
                painter = painterResource(R.drawable.lock),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = message,
                fontSize = 24.sp,
                fontWeight = FontWeight.W700,
                fontFamily = FontFamily.SansSerif,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = when (currentStep) {
                    1 -> "Choose a 4-digit PIN to secure your app"
                    2 -> "Re-enter your PIN to confirm"
                    else -> ""
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.W400,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W400,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            BasicTextField(
                value = if (currentStep == 1) password else confirmPassword,
                onValueChange = { newValue ->
                    if (newValue.length <= 4 && newValue.all { char -> char.isDigit() }) {
                        if (currentStep == 1) {
                            password = newValue
                        } else {
                            confirmPassword = newValue
                        }
                        errorMessage = ""
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    repeat(4) { index ->
                        val currentInput = if (currentStep == 1) password else confirmPassword
                        val number = when {
                            index >= currentInput.length -> ""
                            else -> currentInput[index]
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = number.toString(),
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.White
                            )
                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(2.dp)
                                    .background(
                                        color = if (index < currentInput.length)
                                            Color(0xFF5854D4)
                                        else
                                            Color(0xFF2A2A2A)
                                    )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                modifier = Modifier
                    .width(180.dp)
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5854D4)
                ),
                shape = RoundedCornerShape(12.dp),
                onClick = {
                    when (currentStep) {
                        1 -> {
                            if (password.length == 4) {
                                currentStep = 2
                                message = "Confirm your PIN"
                                errorMessage = ""
                            } else {
                                errorMessage = "Please enter a 4-digit PIN"
                            }
                        }
                        2 -> {
                            if (confirmPassword.length == 4) {
                                if (password == confirmPassword) {
                                    PasswordManager.setPassword(context, password)
                                    message = "Password protection enabled!"
                                    errorMessage = ""
                                    navController.popBackStack()
                                } else {
                                    errorMessage = "PINs don't match. Try again."
                                    confirmPassword = ""
                                }
                            } else {
                                errorMessage = "Please enter a 4-digit PIN"
                            }
                        }
                    }
                },
                enabled = (currentStep == 1 && password.length == 4) ||
                        (currentStep == 2 && confirmPassword.length == 4)
            ) {
                Text(
                    text = when (currentStep) {
                        1 -> "Continue"
                        2 -> "Enable Protection"
                        else -> "Continue"
                    },
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (currentStep == 1) {
                TextButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = "Skip for now",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1F1D35)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Why use password protection?",
                        fontWeight = FontWeight.W600,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "• Keep your personal thoughts private\n• Prevent unauthorized access\n• Secure your data even if device is shared",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}