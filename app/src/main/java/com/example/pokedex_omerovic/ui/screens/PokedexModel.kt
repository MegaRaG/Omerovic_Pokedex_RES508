package com.example.pokedex_omerovic.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex_omerovic.model.PokemonModel
import com.example.pokedex_omerovic.network.PokedexApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface PokedexUiState {
    data class Success(val pokemonList: List<PokemonModel>) : PokedexUiState
    object Error : PokedexUiState
    object Loading : PokedexUiState
}

class PokedexModel  : ViewModel() {
    var pokemons: List<PokemonModel> by mutableStateOf(emptyList())
    var pokedexUiState: PokedexUiState by mutableStateOf(PokedexUiState.Loading)
        private set

    init {
        getPokemon()
    }

    fun getPokemon() {
        viewModelScope.launch {
            pokedexUiState = PokedexUiState.Loading
            try {
                val pokemonList = PokedexApi.retrofitService.getPokemon()
                pokedexUiState = PokedexUiState.Success(pokemonList)
                pokemons = pokemonList
            } catch (e: IOException) {
                pokedexUiState = PokedexUiState.Error
            } catch (e: HttpException) {
                pokedexUiState = PokedexUiState.Error
            }
        }
    }
}