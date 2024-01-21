package com.example.pokedex_omerovic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokedex_omerovic.ui.screens.DetailPokemonScreen
import com.example.pokedex_omerovic.ui.screens.PokemonDetailViewModel
import com.example.pokedex_omerovic.ui.theme.Pokedex_OmerovicTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Pokedex_OmerovicTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "listePokemons") {
                    composable("listePokemons") { PokedexApp(navController) }
                    composable(
                        "detailPokemon/{pokemonId}",
                        arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val viewModel: PokemonDetailViewModel = viewModel()
                        DetailPokemonScreen(
                            pokemonId = backStackEntry.arguments?.getInt("pokemonId") ?: 0,
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}



