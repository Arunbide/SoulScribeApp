package com.arb.soulscribe.Data.Repo

import com.arb.soulscribe.Data.Database.JournalAppDatabase
import com.arb.soulscribe.Data.Table.Journal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

class JournalRepository(val database: JournalAppDatabase) {
suspend fun updateJournal(journal: Journal) {
database.GetJournalDao().updateJournal(journal)
}
    suspend fun deleteJournal(journal: Journal){
        database.GetJournalDao().deleteJournal(journal)
    }
     fun getallJournal():Flow<List<Journal>> {
        return database.GetJournalDao().getAllJournal().onEach { journals: List<Journal> ->  }
     }

    fun getJournalByDate(date: String): Flow<List<Journal>> {
        return database.GetJournalDao().getJournalEntriesByDate(date)

    }

    suspend fun getAllJournalList(): List<Journal> {
        return database.GetJournalDao().getAllJournalList()
    }



}
