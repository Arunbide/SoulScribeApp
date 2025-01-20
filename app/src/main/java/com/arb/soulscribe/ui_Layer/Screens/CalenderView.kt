package com.arb.soulscribe.ui_Layer.Screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.arb.soulscribe.ui_Layer.ViewModel.JournalViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.*

@SuppressLint("NewApi")
@Composable
fun CalendarView(viewModel: JournalViewModel, navController: NavController) {
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val currentMonth = remember { mutableStateOf(LocalDate.now().month) }
    val currentYear = remember { mutableStateOf(LocalDate.now().year) }
    val yearDropdownExpanded = remember { mutableStateOf(false) }
    val monthDropdownExpanded = remember { mutableStateOf(false) }
    val availableYears = remember { mutableStateOf((currentYear.value - 5)..(currentYear.value + 5)) }
    val availableMonths = remember { Month.values().toList() }

    val today = LocalDate.now()
    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = currentMonth.value.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.clickable { monthDropdownExpanded.value = true }
            )
            DropdownMenu(modifier = Modifier
                .background(
                    color = Color(0xf0231B33),
                    shape = MaterialTheme.shapes.extraLarge
                )
                ,
                expanded = monthDropdownExpanded.value,
                onDismissRequest = { monthDropdownExpanded.value = false },
                content = {
                    availableMonths.forEach { month ->
                        DropdownMenuItem(onClick = {
                            currentMonth.value = month
                            monthDropdownExpanded.value = false
                        },
                            text = { Text(text = month.getDisplayName(TextStyle.FULL, Locale.getDefault()), style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )) })
                    }
                }
            )

            Text(
                text = currentYear.value.toString(),
                fontSize = 18.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.clickable { yearDropdownExpanded.value = true }
            )
            DropdownMenu( modifier = Modifier
                .background(
                    color = Color(0xf0231B33),
                    shape = MaterialTheme.shapes.extraLarge
                )
                ,
                expanded = yearDropdownExpanded.value,
                onDismissRequest = { yearDropdownExpanded.value = false },
                content = {
                    availableYears.value.forEach { year ->
                        DropdownMenuItem(onClick = {
                            currentYear.value = year
                            yearDropdownExpanded.value = false
                        },
                            text = { Text(text = year.toString(), style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )) })
                    }
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(48.dp)
                        .padding(vertical = 4.dp)
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            val startOfMonth = LocalDate.of(currentYear.value, currentMonth.value, 1)
            val emptyCells = (startOfMonth.dayOfWeek.value % 7)
            items(emptyCells) {
                Spacer(modifier = Modifier.size(48.dp))
            }

            val daysInMonth = currentMonth.value.length(false)
            items((1..daysInMonth).toList()) { day ->
                val date = startOfMonth.plusDays(day - 1L)
                val isSelected = date == selectedDate.value

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(4.dp)
                        .background(
                            if (isSelected) Color.Blue else Color.Transparent
                        )
                        .clickable {
                            selectedDate.value = date
                            val formattedDate = SimpleDateFormat("EEEE, MMM dd", Locale.getDefault()).format(Date.from(date.atStartOfDay(
                                ZoneId.systemDefault()).toInstant()))
                            navController.navigate("journal_entries/$formattedDate")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day.toString(),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
