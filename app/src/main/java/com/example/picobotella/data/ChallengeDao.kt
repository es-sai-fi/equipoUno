package com.example.picobotella.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.picobotella.model.Challenge
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeDao {
    @Insert
    suspend fun insertChallenge(challenge: Challenge): Long

    @Query("SELECT * FROM Challenge ORDER BY id DESC")
    fun observeChallenges(): Flow<List<Challenge>>

    @Delete
    suspend fun deleteChallenge(challenge: Challenge)

    @Update
    suspend fun updateChallenge(challenge: Challenge)
}
