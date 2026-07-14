package com.example.picobotella.repository

import com.example.picobotella.webservice.ApiService
import com.example.picobotella.model.PokemonModelResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PokemonRepository(
    private val apiService: ApiService
) {

    suspend fun getPokemons(): MutableList<PokemonModelResponse> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getPokemons().pokemon
            } catch (e: Exception) {
                e.printStackTrace()
                mutableListOf()
            }
        }
    }
}