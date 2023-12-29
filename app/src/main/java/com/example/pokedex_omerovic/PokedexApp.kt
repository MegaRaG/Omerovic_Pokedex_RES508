@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.pokedex_omerovic

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pokedex_omerovic.ui.screens.ErrorScreen
import com.example.pokedex_omerovic.ui.screens.LoadingScreen
import com.example.pokedex_omerovic.ui.screens.PokedexModel
import com.example.pokedex_omerovic.ui.screens.PokedexUiState
import com.example.pokedex_omerovic.ui.screens.PokemonCard
import com.example.pokedex_omerovic.ui.screens.ResultScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexApp() {
    Scaffold {
        // Utilisation de Box pour superposer l'image de fond et la liste des Pokémon
        Box(modifier = Modifier.fillMaxSize()) {

            // Image de fond en arrière-plan
            Image(
                painter = painterResource(id = R.drawable.fondpokedex),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .scale(1.2f) // Ajustez le zoom si nécessaire
            )

            // Contenu principal avec la liste des Pokémon
            val pokedexViewModel: PokedexModel = viewModel()
            when (val currentState = pokedexViewModel.pokedexUiState) {
                is PokedexUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
                is PokedexUiState.Success -> {
                    val topPadding = 150.dp // Hauteur de début
                    val bottomPadding = 50.dp // Hauteur de fin

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = topPadding, bottom = bottomPadding)
                            .heightIn(min = 0.dp, max = 450.dp) // Ajustez la hauteur maximale si nécessaire
                    ) {
                        items(currentState.pokemonList) { pokemon ->
                            PokemonCard(pokemon = pokemon)
                        }
                    }
                }
                is PokedexUiState.Error -> ErrorScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

