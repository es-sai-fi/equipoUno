package com.example.picobotella.webservice

import com.example.picobotella.model.PokemonModelResponse
import com.example.picobotella.utils.Constants.END_POINT
import retrofit2.http.GET

interface ApiService {
    @GET(END_POINT)
    suspend fun getPokemons(): MutableList<PokemonModelResponse>
}