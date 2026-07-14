package com.example.picobotella.model

import com.google.gson.annotations.SerializedName

data class PokemonModelResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("img") val img: String,
)