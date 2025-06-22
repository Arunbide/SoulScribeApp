package com.arb.soulscribe.ui_Layer.Screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
    val availableYears = remember { mutableStateOf((currentYear.value - 10)..(currentYear.value + 5)) }
    val availableMonths = remember { Month.values().toList() }

    val today = LocalDate.now()
    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    // Function to navigate to previous month
    fun navigateToPreviousMonth() {
        if (currentMonth.value == Month.JANUARY) {
            currentMonth.value = Month.DECEMBER
            currentYear.value -= 1
        } else {
            currentMonth.value = currentMonth.value.minus(1)
        }
    }

    // Function to navigate to next month
    fun navigateToNextMonth() {
        if (currentMonth.value == Month.DECEMBER) {
            currentMonth.value = Month.JANUARY
            currentYear.value += 1
        } else {
            currentMonth.value = currentMonth.value.plus(1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF121025), Color(0xFF5E3B67))
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header with month/year navigation
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.1f)
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Previous month button
                IconButton(
                    onClick = { navigateToPreviousMonth() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Previous Month",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Month and Year dropdowns
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Month dropdown
                    Box {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                                .clickable { monthDropdownExpanded.value = true }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = currentMonth.value.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Icon(
                                Icons.Default.KeyboardArrowDown,
                                contentDescription = "Select Month",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        DropdownMenu(
                            modifier = Modifier
                                .background(
                                    Color(0xFF2A1F3D),
                                    RoundedCornerShape(16.dp)
                                )
                                .border(
                                    1.dp,
                                    Color.White.copy(alpha = 0.2f),
                                    RoundedCornerShape(16.dp)
                                ),
                            expanded = monthDropdownExpanded.value,
                            onDismissRequest = { monthDropdownExpanded.value = false }
                        ) {
                            availableMonths.forEach { month ->
                                DropdownMenuItem(
                                    onClick = {
                                        currentMonth.value = month
                                        monthDropdownExpanded.value = false
                                    },
                                    text = {
                                        Text(
                                            text = month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                                            color = if (month == currentMonth.value) Color(0xFF007AFF) else Color.White,
                                            fontWeight = if (month == currentMonth.value) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    modifier = Modifier.background(
                                        if (month == currentMonth.value) Color.White.copy(alpha = 0.1f) else Color.Transparent
                                    )
                                )
                            }
                        }
                    }

                    // Year dropdown
                    Box {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                                .clickable { yearDropdownExpanded.value = true }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = currentYear.value.toString(),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Icon(
                                Icons.Default.KeyboardArrowDown,
                                contentDescription = "Select Year",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        DropdownMenu(
                            modifier = Modifier
                                .background(
                                    Color(0xFF2A1F3D),
                                    RoundedCornerShape(16.dp)
                                )
                                .border(
                                    1.dp,
                                    Color.White.copy(alpha = 0.2f),
                                    RoundedCornerShape(16.dp)
                                )
                                .heightIn(max = 200.dp),
                            expanded = yearDropdownExpanded.value,
                            onDismissRequest = { yearDropdownExpanded.value = false }
                        ) {
                            availableYears.value.forEach { year ->
                                DropdownMenuItem(
                                    onClick = {
                                        currentYear.value = year
                                        yearDropdownExpanded.value = false
                                    },
                                    text = {
                                        Text(
                                            text = year.toString(),
                                            color = if (year == currentYear.value) Color(0xFF007AFF) else Color.White,
                                            fontWeight = if (year == currentYear.value) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    modifier = Modifier.background(
                                        if (year == currentYear.value) Color.White.copy(alpha = 0.1f) else Color.Transparent
                                    )
                                )
                            }
                        }
                    }
                }

                // Next month button
                IconButton(
                    onClick = { navigateToNextMonth() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowRight,
                        contentDescription = "Next Month",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Days of week header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.1f)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                daysOfWeek.forEach { day ->
                    Text(
                        text = day,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Calendar grid
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.05f)
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val startOfMonth = LocalDate.of(currentYear.value, currentMonth.value, 1)
                val emptyCells = (startOfMonth.dayOfWeek.value % 7)

                // Empty cells for days before month starts
                items(emptyCells) {
                    Spacer(modifier = Modifier.size(44.dp))
                }

                val daysInMonth = currentMonth.value.length(
                    LocalDate.of(currentYear.value, currentMonth.value, 1).isLeapYear
                )

                items((1..daysInMonth).toList()) { day ->
                    val date = startOfMonth.plusDays(day - 1L)
                    val isSelected = date == selectedDate.value
                    val isToday = date == today
                    val isPastDate = date.isBefore(today)

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isSelected -> Color(0xFF007AFF)
                                    isToday -> Color(0xFF34C759).copy(alpha = 0.3f)
                                    else -> Color.Transparent
                                }
                            )
                            .border(
                                width = if (isToday && !isSelected) 2.dp else 0.dp,
                                color = Color.White,
                                shape = CircleShape
                            )
                            .clickable {
                                selectedDate.value = date
                                val formattedDate = SimpleDateFormat(
                                    "EEEE, MMM dd",
                                    Locale.getDefault()
                                ).format(
                                    Date.from(
                                        date.atStartOfDay(ZoneId.systemDefault()).toInstant()
                                    )
                                )
                                navController.navigate("journal_entries/$formattedDate")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day.toString(),
                            fontSize = 16.sp,
                            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                            color = when {
                                isSelected -> Color.White
                                isToday -> Color(0xFF34C759)
                                isPastDate -> Color.White.copy(alpha = 0.6f)
                                else -> Color.White
                            },
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

        }
    }
}