package com.example.picobotella.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.picobotella.model.Challenge
import com.example.picobotella.utils.Constants.NAME_BD

@Database(
    entities = [Challenge::class],
    version = 1,
    exportSchema = false,
)
abstract class ChallengeDB : RoomDatabase() {
    abstract fun challengeDao(): ChallengeDao

    companion object {
        @Volatile
        private var instance: ChallengeDB? = null

        fun getDatabase(context: Context): ChallengeDB {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ChallengeDB::class.java,
                    NAME_BD,
                ).build().also { instance = it }
            }
        }
    }
}
