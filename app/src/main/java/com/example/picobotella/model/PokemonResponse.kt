package com.example.picobotella.model

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    @SerializedName("pokemon")
    val pokemon: MutableList<PokemonModelResponse>
)
