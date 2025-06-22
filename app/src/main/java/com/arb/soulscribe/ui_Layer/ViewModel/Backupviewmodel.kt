package com.arb.soulscribe.ui_Layer.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arb.soulscribe.Data.Repo.JournalRepository
import com.arb.soulscribe.Data.Table.Journal
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val repository: JournalRepository
) : ViewModel() {

    suspend fun generateBackupJson(): String {
        val journals = repository.getAllJournalList()
        return Gson().toJson(journals)
    }

    suspend fun importBackupJson(json: String) {
        val type = object : TypeToken<List<Journal>>() {}.type
        val importedEntries: List<Journal> = Gson().fromJson(json, type)

        // Fetch existing entries from database
        val existingEntries = repository.getAllJournalList()

        // Combine: Keep existing, add only new ones
        val newEntries = importedEntries.filterNot { imported ->
            existingEntries.any { existing -> existing.id == imported.id }
        }

        // Insert only the new unique entries
        newEntries.forEach { repository.updateJournal(it) }
    }

}
