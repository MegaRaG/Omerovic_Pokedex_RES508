@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.pokedex_omerovic

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pokedex_omerovic.ui.screens.ErrorScreen
import com.example.pokedex_omerovic.ui.screens.LoadingScreen
import com.example.pokedex_omerovic.ui.screens.PokedexModel
import com.example.pokedex_omerovic.ui.screens.PokedexUiState
import com.example.pokedex_omerovic.ui.screens.ResultScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexApp() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { PokedexTopAppBar(scrollBehavior = scrollBehavior) }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val pokedexViewModel: PokedexModel = viewModel()
            when (val currentState = pokedexViewModel.pokedexUiState) {
                is PokedexUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
                is PokedexUiState.Success -> ResultScreen(pokemons = pokedexViewModel.pokemons, modifier = Modifier.fillMaxWidth())
                is PokedexUiState.Error -> ErrorScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun PokedexTopAppBar(scrollBehavior: TopAppBarScrollBehavior, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        modifier = modifier
    )
}