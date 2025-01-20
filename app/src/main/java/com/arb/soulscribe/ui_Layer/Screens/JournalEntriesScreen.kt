package com.arb.soulscribe.ui_Layer.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arb.soulscribe.Data.Table.Journal
import com.arb.soulscribe.ui_Layer.ViewModel.JournalViewModel
import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*

import androidx.navigation.NavController
import com.arb.soulscribe.navigation.AddEditScreen












@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalTopAppBar(date: String) {
    TopAppBar( colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF121025)),
        title = { Text("Entries for $date") }
    )
}

@Composable
fun JournalEntriesScreen(viewModel: JournalViewModel, date: String, navController: NavController) {
    LaunchedEffect(date) {
        viewModel.getEntriesForSelectedDate(date)
    }

    val entries by viewModel.journalEntries.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF121025))) {

        JournalTopAppBar(date = date)

        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {


            if (entries.isNotEmpty()) {
                items(entries) { journal ->
                    JournalCard(
                        journal = journal,
                        viewModel = viewModel,
                        onClick = {
                            viewModel.setCurrentJournal(journal)
                            navController.navigate(AddEditScreen)
                        }
                    )
                }
            } else {
                item {
                    Text(
                        text = "No entries for this date.",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}


