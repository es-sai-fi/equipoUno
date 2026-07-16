package com.example.picobotella.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.picobotella.data.ChallengeDB
import com.example.picobotella.repository.ChallengeRepository
import com.example.picobotella.repository.PokemonRepository
import com.example.picobotella.model.Challenge import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import com.example.picobotella.model.PokemonModelResponse
import androidx.lifecycle.LiveData
import com.example.picobotella.webservice.ApiUtils
class ChallengeViewModel(application: Application) : AndroidViewModel(application) {
  private val repository =
      ChallengeRepository(
          ChallengeDB.getDatabase(application).challengeDao(),
      )
  val challenges = repository.challenges.asLiveData()

  private val pokemonRepository =
    PokemonRepository(
      ApiUtils.getApiService()
    )

  private val _pokemonList = MutableLiveData<MutableList<PokemonModelResponse>>()
  val pokemonList: LiveData<MutableList<PokemonModelResponse>> get() = _pokemonList

  private val _progressState = MutableLiveData(false)
  val progressState: LiveData<Boolean> get() = _progressState

  private val _randomChallenge = MutableLiveData<Challenge?>()
  val randomChallenge: LiveData<Challenge?> = _randomChallenge

  fun clearRandomChallenge() {
    _randomChallenge.value = null
  }
  fun getPokemons() {
    viewModelScope.launch {
      _progressState.value = true

      try {
        _pokemonList.value = pokemonRepository.getPokemons()
      } catch (e: Exception) {
        _pokemonList.value = mutableListOf()
      }

      _progressState.value = false
    }
  }
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
  fun getRandomChallenge() {
    viewModelScope.launch {

      _progressState.value = true

      try {
        _randomChallenge.value = repository.getRandomChallenge()
      } catch (e: Exception) {
        _randomChallenge.value = null
      }

      _progressState.value = false
    }
  }
}
