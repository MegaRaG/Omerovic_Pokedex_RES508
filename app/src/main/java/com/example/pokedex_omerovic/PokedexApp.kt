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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.pokedex_omerovic.ui.screens.ErrorScreen
import com.example.pokedex_omerovic.ui.screens.LoadingScreen
import com.example.pokedex_omerovic.ui.screens.PokedexModel
import com.example.pokedex_omerovic.ui.screens.PokedexUiState
import com.example.pokedex_omerovic.ui.screens.PokemonCard

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PokedexApp(navController: NavController) {
    Scaffold {
        Box(modifier = Modifier.fillMaxSize()) {

            Image(
                painter = painterResource(id = R.drawable.fondpokedex),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .scale(1.2f)
            )

            val pokedexViewModel: PokedexModel = viewModel()
            when (val currentState = pokedexViewModel.pokedexUiState) {
                is PokedexUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
                is PokedexUiState.Success -> {
                    val topPadding = 150.dp
                    val bottomPadding = 50.dp

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = topPadding, bottom = bottomPadding)
                            .heightIn(min = 0.dp, max = 450.dp)
                    ) {
                        items(currentState.pokemonList) { pokemon ->
                            PokemonCard(pokemon = pokemon, navController = navController)
                        }
                    }
                }

                is PokedexUiState.Error -> ErrorScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

