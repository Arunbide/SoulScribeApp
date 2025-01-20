package com.arb.soulscribe.Data.DI

import android.app.Application
import androidx.room.Room
import com.arb.soulscribe.Data.Database.JournalAppDatabase
import com.arb.soulscribe.Data.Repo.JournalRepository
import com.arb.soulscribe.db
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideJournalDatabase(application: Application): JournalAppDatabase {
        return Room.databaseBuilder(application, JournalAppDatabase::class.java,
             db) .fallbackToDestructiveMigration().build()
    }
     @Provides
     @Singleton
     fun provideRepository(database: JournalAppDatabase):JournalRepository{
      return JournalRepository(database)
     }

}