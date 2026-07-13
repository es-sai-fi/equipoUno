package com.example.picobotella.data

import com.example.picobotella.model.Challenge
import kotlinx.coroutines.flow.Flow

class ChallengeRepository(
    private val challengeDao: ChallengeDao,
) {
  val challenges: Flow<List<Challenge>> = challengeDao.observeChallenges()

  suspend fun insert(description: String) {
    challengeDao.insertChallenge(Challenge(description = description))
  }

  suspend fun update(challenge: Challenge, description: String) {
    challengeDao.updateChallenge(challenge.copy(description = description))
  }

  suspend fun delete(challenge: Challenge) {
    challengeDao.deleteChallenge(challenge)
  }
}
