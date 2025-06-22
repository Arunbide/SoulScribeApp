package com.arb.soulscribe.Data.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arb.soulscribe.Data.Table.Journal
import com.arb.soulscribe.Data.Table.JournalDao



@Database(entities = [Journal::class], version = 3, exportSchema = false)
abstract class JournalAppDatabase:RoomDatabase(){
    abstract fun GetJournalDao():JournalDao
}