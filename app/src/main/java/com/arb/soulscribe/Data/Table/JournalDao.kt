package com.arb.soulscribe.Data.Table

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface JournalDao {
    @Upsert
  suspend  fun updateJournal(journal: Journal)
    @Delete
  suspend fun deleteJournal(journal: Journal)
   @Query("SELECT*FROM JOURNAL_ENTRIES  ORDER BY id DESC")
  fun getAllJournal():Flow<List<Journal>>

    @Query("SELECT * FROM JOURNAL_ENTRIES WHERE date = :date")
    fun getJournalEntriesByDate(date: String): Flow<List<Journal>>

    @Query("SELECT * FROM JOURNAL_ENTRIES ORDER BY id DESC")
    suspend fun getAllJournalList(): List<Journal>
}