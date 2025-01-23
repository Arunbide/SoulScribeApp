package com.arb.soulscribe.ui_Layer.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.arb.soulscribe.ui_Layer.ViewModel.JournalViewModel
import com.arb.soulscribe.ui_Layer.States.JournalUiState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddJournalScreen(
    navController: NavController,
    viewModel: JournalViewModel = hiltViewModel(),
    onEvent: () -> Unit,
    state: JournalUiState? = null,
) {
    val isEditing = state != null

    val calendar = Calendar.getInstance()
    val formattedDate =
        SimpleDateFormat("EEEE, MMM dd", Locale.getDefault()).format(calendar.time)
    Scaffold(
        containerColor = Color(0xFF231B33),
        topBar = {
            TopAppBar(
                title = { Text(text = formattedDate, style = MaterialTheme.typography.titleMedium,color=MaterialTheme.colorScheme.primary) },
                navigationIcon = {

                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (isEditing) {
                            onEvent()
                        } else {
                            viewModel.upsertJournal()
                        }
                        navController.navigateUp()

                    }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF231B33)
                ))

        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp) .imePadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    state?.title?.let {
                        TextField(
                            value = it.value,
                            onValueChange = { state.title.value = it },
                            placeholder = { Text(" title", fontSize = 25.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            textStyle = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 6
                        )
                    }
                }

                item {
                    if (state != null) {
                        TextField(
                            value = state.content.value,
                            onValueChange = { state.content.value = it },
                            placeholder = { Text("Start Writing") },
                            modifier = Modifier.fillMaxSize(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent
                            ),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }

        }
    )
}
