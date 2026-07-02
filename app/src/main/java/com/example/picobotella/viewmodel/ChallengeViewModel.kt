package com.example.picobotella.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.picobotella.data.ChallengeDB
import com.example.picobotella.data.ChallengeRepository
import com.example.picobotella.model.Challenge
import kotlinx.coroutines.launch

class ChallengeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ChallengeRepository(
        ChallengeDB.getDatabase(application).challengeDao(),
    )

    val challenges = repository.challenges.asLiveData()

    fun addChallenge(description: String) {
        val normalizedDescription = description.trim()
        if (normalizedDescription.isEmpty()) return

        viewModelScope.launch {
            repository.insert(normalizedDescription)
        }
    }

    fun updateChallenge(challenge: Challenge, description: String) {
        val normalizedDescription = description.trim()
        if (normalizedDescription.isEmpty()) return

        viewModelScope.launch {
            repository.update(challenge, normalizedDescription)
        }
    }

    fun deleteChallenge(challenge: Challenge) {
        viewModelScope.launch {
            repository.delete(challenge)
        }
    }
}
