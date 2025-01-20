package com.arb.soulscribe.ui_Layer.States

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arb.soulscribe.Data.Table.Journal


data class JournalUiState(
    var loading :Boolean=false,
    val journalList: List<Journal> = emptyList(),
    val id: MutableState<Int?> = mutableStateOf(0),
    val title: MutableState<String> = mutableStateOf(""),
    val date: MutableState<String> = mutableStateOf(""),
    val content: MutableState<String> = mutableStateOf(""),
)