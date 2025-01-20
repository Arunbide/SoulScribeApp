package com.arb.soulscribe.ui_Layer.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.arb.soulscribe.navigation.HomeScreen
import com.arb.soulscribe.navigation.PasswordScreen
import com.arb.soulscribe.navigation.WalkThroughScreen
import com.arb.soulscribe.utils.PasswordManager
@Composable
fun LockScreenUI(navController: NavHostController) {
   val context = LocalContext.current
   var isPasswordSet by remember { mutableStateOf(PasswordManager.isPasswordSet(context)) }

   Column(
      modifier = Modifier.fillMaxSize().background(Color(0xFF121025)),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
   ) {
      Column(
         horizontalAlignment = Alignment.CenterHorizontally,
         verticalArrangement = Arrangement.Center,
         modifier = Modifier
            .fillMaxWidth()
            .padding(top = 100.dp).background(Color(0xFF121025))
      ) {
         Box(
            modifier = Modifier
               .size(90.dp)
               .clip(RoundedCornerShape(50.dp))
               .background(Color(0xFF5854D4)),
            contentAlignment = Alignment.Center
         ) {
            Icon(
               imageVector = Icons.Default.Lock,
               contentDescription = "Lock Icon",
               modifier = Modifier.size(50.dp),
               tint = Color.Black
            )
         }

         Spacer(modifier = Modifier.height(20.dp))

         Text(
            text = "Lock Your Journal",
            modifier = Modifier.padding(horizontal = 20.dp),
            style = MaterialTheme.typography.headlineLarge.copy(
               fontWeight = FontWeight.ExtraBold
            ),
            textAlign = TextAlign.Center
         )

         Spacer(modifier = Modifier.height(15.dp))

         Text(
            text = "Protect your personal entries and lock your journal with your passcode, so only you can access it.",
            modifier = Modifier.padding(horizontal = 40.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
         )
      }

      // Bottom Section
      Column(
         modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF282143))
            .padding(horizontal = 24.dp, vertical = 20.dp),
         horizontalAlignment = Alignment.CenterHorizontally
      ) {
         Button(
            onClick = {navController.navigate(PasswordScreen)},
            modifier = Modifier
               .fillMaxWidth().padding(horizontal = 15.dp)
               .height(49.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5854D4)),
            shape = RoundedCornerShape(13.dp)
         ) {
            Text(
               text = "Turn On",
               color = Color.White,
               fontSize = 16.sp
            )
         }

         Spacer(modifier = Modifier.height(10.dp))

         Button(
            onClick = {navController.navigate(HomeScreen)},
            modifier = Modifier
               .fillMaxWidth()
               .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(13.dp)
         ) {
            Text(
               text = "Not Now",
               color = Color(0xFF5854D4),
               fontSize = 16.sp
            )
         }
      }
   }
}
