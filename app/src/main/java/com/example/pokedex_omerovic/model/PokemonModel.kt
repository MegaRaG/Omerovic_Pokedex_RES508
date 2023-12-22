package com.example.pokedex_omerovic.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonModel(
    val id: Int,
    val name: String,
    val type: List<String>,
    val description: String,
    @SerialName(value = "image_url")
    val imageUrl: String,
    val evolutions: Evolutions
)

@Serializable
data class Evolutions(
    val before: List<Int>,
    val after: List<Int>
)
