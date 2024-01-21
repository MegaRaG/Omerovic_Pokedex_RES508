package com.example.pokedex_omerovic.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex_omerovic.model.PokemonModel
import com.example.pokedex_omerovic.network.PokedexApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonDetailViewModel : ViewModel() {
    private val _pokemonDetailState = MutableStateFlow<PokemonModel?>(null)
    val pokemonDetailState: StateFlow<PokemonModel?> = _pokemonDetailState
    private val _evolutionsState = MutableStateFlow<List<PokemonModel>>(emptyList())
    val evolutionsState: StateFlow<List<PokemonModel>> = _evolutionsState

    fun fetchPokemonDetails(pokemonId: Int) {
        viewModelScope.launch {
            try {
                val allPokemons = PokedexApi.retrofitService.getPokemon()
                val pokemonDetails = allPokemons.find { it.id == pokemonId }
                _pokemonDetailState.value = pokemonDetails

                val evolutions = mutableListOf<PokemonModel>()
                pokemonDetails?.evolutions?.let { evolutionsData ->
                    evolutionsData.before.plus(evolutionsData.after).forEach { id ->
                        allPokemons.find { it.id == id }?.let { evolutions.add(it) }
                    }
                }
                _evolutionsState.value = evolutions
            } catch (e: Exception) {
                _pokemonDetailState.value = null
                _evolutionsState.value = emptyList()
            }
        }
    }
}
