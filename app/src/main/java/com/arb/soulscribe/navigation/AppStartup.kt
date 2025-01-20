package com.arb.soulscribe.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.arb.soulscribe.ui_Layer.utils.WalkthroughManager
import com.arb.soulscribe.utils.PasswordManager

@Composable
fun AppStart(navController: NavHostController, context: Context) {
    var isPasswordSet by remember { mutableStateOf(false) }
    var isWalkthroughShown by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isPasswordSet = PasswordManager.isPasswordSet(context)
        isWalkthroughShown = WalkthroughManager.isWalkthroughShown(context)

        if (isWalkthroughShown) {
            if (isPasswordSet) {
                navController.navigate(PasswordScreen)
            } else {
                navController.navigate(LockScreen)
            }
        } else {
            navController.navigate(WalkThroughScreen)
        }
    }
}
