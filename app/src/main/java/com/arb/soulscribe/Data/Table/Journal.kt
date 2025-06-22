package com.arb.soulscribe.Data.Table

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "journal_entries")
data class Journal(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val title: String,
    val date: String,
    val content: String,

    )
