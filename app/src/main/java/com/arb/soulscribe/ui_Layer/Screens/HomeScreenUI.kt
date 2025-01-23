package com.arb.soulscribe.ui_Layer.Screens

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.NavController
import com.arb.soulscribe.navigation.AddEditScreen
import com.arb.soulscribe.Data.Table.Journal
import com.arb.soulscribe.ui_Layer.Screen.CalendarView
import com.arb.soulscribe.ui_Layer.ViewModel.JournalViewModel
import com.arb.soulscribe.ui_Layer.States.JournalUiState
import com.arb.soulscribe.ui_Layer.utils.ShimmerListItem
import kotlinx.coroutines.delay

@SuppressLint("UseOfNonLambdaOffsetOverload")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenUI(
    navController: NavController,
    state: JournalUiState,
    viewModel: JournalViewModel = hiltViewModel(),
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val activity = LocalContext.current as? Activity
    var showDialog by remember { mutableStateOf(false) }

    BackHandler {
        showDialog = true
    }

    if (showDialog) {
        AlertDialog(containerColor = Color(0xFF121025),
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Exit App") },
            text = { Text(text = "Are you sure you want to exit the app?") },
            confirmButton = {
                Button(onClick = { activity?.finish() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    ) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    ) {
                    Text(text = "No",)
                }
            }
        )
    }

    Scaffold( containerColor =Color(0xFF231B33),
        contentColor = contentColorFor(containerColor),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "SoulScribe",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = Color.White)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF231B33),

                )

            )

        },
        floatingActionButton = {
            if (selectedTab == 0) {

                FloatingActionButton(onClick = {
                    viewModel.resetStateforNewEntry()
                    navController.navigate(
                        AddEditScreen

                    ) }, modifier = Modifier.size(70.dp), shape = RoundedCornerShape(40),
                    containerColor = Color(0xFF3E3B67)) {
                    Icon(imageVector = Icons.Rounded.Add,
                        contentDescription = "Add Journal",
                        modifier = Modifier.size(30.dp))
                }
            }
        },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(35.dp)
                        .padding(horizontal = 44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.DarkGray)
                ) {
                    Row(
                        Modifier.fillMaxSize()
                    ) {

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (selectedTab == 0) Color.Gray else Color.Transparent)
                                .clickable { selectedTab = 0 },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Journals",
                                color = if (selectedTab == 0) Color.White else Color.LightGray,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }


                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (selectedTab == 1) Color.Gray else Color.Transparent)
                                .clickable { selectedTab = 1 },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Calendar",
                                color = if (selectedTab == 1) Color.White else Color.LightGray,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                when (selectedTab) {
                    0 -> JournalListScreen(state, navController, viewModel)
                    1 -> CalendarView(
                        viewModel,
                        navController = navController,
                    )
                }
            }

        }
    )
} @Composable
fun JournalListScreen(
    state: JournalUiState,
    navController: NavController,
    viewModel: JournalViewModel
) {
    var isLoadingTimedOut by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000)
        isLoadingTimedOut = true
    }



    if (state.journalList.isEmpty() && !isLoadingTimedOut) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(5) {
                ShimmerListItem()
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 0.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF121025), Color(0xFF5E3B67)) // Gradient background colors
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (state.journalList.isEmpty() && isLoadingTimedOut) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    ) {
                        Text(
                            text = "No entries available",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }


            } else {
                items(state.journalList) { journal ->
                    JournalCard(
                        journal = journal,
                        viewModel = viewModel,
                        onClick = {
                            viewModel.setCurrentJournal(journal)
                            navController.navigate(AddEditScreen)
                        }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JournalCard(journal: Journal, viewModel: JournalViewModel, onClick: () -> Unit) {
    var MenuVisible by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, start = 15.dp, end = 15.dp)

            .combinedClickable(onClick = { onClick() }, onLongClick = { MenuVisible = true }),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)), // Whitish transparent background
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.03f)),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text(
                    text = journal.title,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = journal.content,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.height(20.dp)) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.White.copy(alpha = 0.1f)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = journal.date,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall
                    )


                    IconButton(
                        onClick = { MenuVisible = true },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            modifier = Modifier.graphicsLayer(
                                rotationZ = 90f
                            )
                        )
                    }
                }
            }
        }


        DropdownMenu(
            expanded = MenuVisible,
            onDismissRequest = { MenuVisible = false },
            modifier = Modifier
                .background(
                    color = Color(0xf0231B33),
                    shape = MaterialTheme.shapes.extraLarge
                )

                .width(200.dp),
            offset = DpOffset(x = (-16).dp, y = 0.dp)
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        "Delete",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                onClick = {
                    MenuVisible = false
                    viewModel.deleteEntries(journal)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            DropdownMenuItem(
                text = {
                    Text(
                        "Cancel",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF007AFF),
                            fontWeight = FontWeight.Medium
                        )
                    )
                },
                onClick = {
                    MenuVisible = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical =12.dp)
            )
        }

    }
    }



