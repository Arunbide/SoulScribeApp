package com.arb.soulscribe.ui_Layer.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arb.soulscribe.Data.Repo.JournalRepository
import com.arb.soulscribe.Data.Table.Journal
import com.arb.soulscribe.ui_Layer.States.JournalUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val repository: JournalRepository
) : ViewModel() {

    val calendar = Calendar.getInstance()
    val shortdate = SimpleDateFormat("EEEE, MMM dd", Locale.getDefault()).format(calendar.time)
    private val _journalEntries = MutableStateFlow<List<Journal>>(emptyList())
    val journalEntries = _journalEntries.asStateFlow()

    private val _state = MutableStateFlow<JournalUiState>(JournalUiState())
    private val JournalList = repository.getallJournal().stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(),
        emptyList()
    )

    val state = combine(_state, JournalList) { _state, journal ->
        _state.copy(journalList = journal)
    }.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), JournalUiState()
    )

    fun upsertJournal() {
        val journal = Journal(
            title = state.value.title.value,
            content = state.value.content.value,
            date = shortdate
        )
        viewModelScope.launch {
            resetStateforNewEntry()
            repository.updateJournal(journal)
        }

        state.value.title.value = ""
        state.value.date.value = ""
        state.value.content.value = ""
        state.value.id.value = null
    }

    fun setCurrentJournal(journal: Journal) {
        state.value.title.value = journal.title
        state.value.content.value = journal.content
        state.value.date.value = journal.date
        state.value.id.value = journal.id
    }

    fun resetStateforNewEntry() {
        state.value.title.value = ""
        state.value.date.value = ""
        state.value.content.value = ""
        state.value.id.value = null
    }

    fun deleteEntries(journal: Journal) {
        viewModelScope.launch {
            repository.deleteJournal(journal)
        }
    }
    fun getEntriesForSelectedDate(date: String) {
        viewModelScope.launch {
            repository.getJournalByDate(date).collect { entries ->
                _journalEntries.value = entries
                Log.d("JournalViewModel", "Fetched entries for $date: $entries")
            }
        }
    }
}
