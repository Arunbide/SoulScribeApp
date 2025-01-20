
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize


import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.arb.soulscribe.R
import com.arb.soulscribe.navigation.HomeScreen
import com.arb.soulscribe.utils.PasswordManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassScreenUI(navController: NavHostController) {
    val context = LocalContext.current
    var password by remember { mutableStateOf("") }
    var isPasswordSet by remember { mutableStateOf(PasswordManager.isPasswordSet(context)) }
    var message by remember { mutableStateOf(if (isPasswordSet) "Enter PassCode" else "Set PassCode") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121025)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text("") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF121025)
            )
        )

        Image(
            painter = painterResource(R.drawable.lock),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .padding(top = 0.dp)
        )

        Text(
            message,
            modifier = Modifier.padding(top = 10.dp),
            fontSize = 30.sp,
            fontWeight = FontWeight.W700,
            fontFamily = FontFamily.SansSerif
        )

        Text(
            "Please ${if (isPasswordSet) "enter" else "set"} a 4-digit password to continue",
            modifier = Modifier.padding(top = 10.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.W400,
        )

        Spacer(modifier = Modifier.height(30.dp))

        BasicTextField(
            value = password,
            onValueChange = {
                if (it.length <= 4 && it.all { char -> char.isDigit() }) {
                    password = it
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                repeat(4) { index ->
                    val number = when {
                        index >= password.length -> ""
                        else -> password[index]
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = number.toString(),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(2.dp)
                                .background(color = Color(0xFF5854D4))
                        )
                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(50.dp))


        Button(
            modifier = Modifier
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            onClick = {
                if (password.length == 4) {
                    if (isPasswordSet) {

                        if (PasswordManager.getPassword(context) == password) {
                            navController.navigate(HomeScreen)
                        } else {
                            message = "Incorrect Password"
                        }
                    } else {

                        PasswordManager.setPassword(context, password)
                        isPasswordSet = true
                        message = "Password Set Successfully"
                    }
                }
            }
        ) {
            Text(if (isPasswordSet) "Unlock" else "Set Password",color = Color(0xFF095DEC), fontSize = 18.sp, fontWeight = FontWeight.W500  )
        }
    }
}
