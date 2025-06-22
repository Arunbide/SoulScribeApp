package com.arb.soulscribe.navigation

import PassScreenUI
import WalkThroughScreenUI
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arb.soulscribe.ui_Layer.ViewModel.JournalViewModel

import com.arb.soulscribe.ui_Layer.Screens.AddJournalScreen

import com.arb.soulscribe.ui_Layer.Screens.HomeScreenUI
import com.arb.soulscribe.ui_Layer.Screens.LockScreenUI

import androidx.compose.ui.platform.LocalContext
import com.arb.soulscribe.ui_Layer.Screens.BackupScreenUI
import com.arb.soulscribe.ui_Layer.Screens.JournalEntriesScreen
import com.arb.soulscribe.ui_Layer.Screens.SetupPasswordScreen

@Composable
fun App( viewModel: JournalViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current



    NavHost(navController = navController, startDestination = AppStartup) {
        composable<HomeScreen> {
            HomeScreenUI(state = state.value, viewModel = viewModel, navController = navController)
        }
        composable<AddEditScreen> {
            AddJournalScreen(
                state = state.value,
                onEvent = { viewModel.upsertJournal() },
                navController = navController
            )
        }
        composable<WalkThroughScreen> {
            WalkThroughScreenUI(navController = navController)
        }
        composable<PasswordScreen> {
            PassScreenUI(navController = navController)
        }
        composable<LockScreen> {
            LockScreenUI(navController = navController)
        }
        composable<AppStartup> {
            AppStart(
                navController = navController,
                context = context
            )
        }
        composable("journal_entries/{date}") { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: ""
            JournalEntriesScreen(viewModel, date,navController=navController)
        }

        composable<Backupscreen> {
            BackupScreenUI(navController)
        }
        composable<PasswordSetting> {
            SetupPasswordScreen(navController = navController)

        }
    }
}
