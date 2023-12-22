package com.example.pokedex_omerovic.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.pokedex_omerovic.PokedexApp
import com.example.pokedex_omerovic.ui.theme.Pokedex_OmerovicTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pokedex_omerovic.model.PokemonModel
import com.example.pokedex_omerovic.network.PokedexApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailPokemon : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pokemonId = intent.getIntExtra("pokemonId", 0)

        CoroutineScope(Dispatchers.IO).launch {
            val allPokemons = PokedexApi.retrofitService.getPokemon()
            val pokemonDetails = allPokemons.find { it.id == pokemonId }
//Changer le get pour récupérer les données nécessaires des évolutions aussi, donc dans le .find mettre les id présent dans évolution
            withContext(Dispatchers.Main) {
                setContent {
                    Pokedex_OmerovicTheme {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            if (pokemonDetails != null) {
                                PokemonDetailsContent(
                                    pokemon = pokemonDetails
                                ) {
                                    finish() // Fermer l'activité
                                }
                            } else {
                                Text("Aucun Pokémon trouvé avec l'ID: $pokemonId")
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun PokemonDetailsContent(pokemon: PokemonModel, onDismiss: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Détails du Pokémon", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // Utilisation d'une Row pour afficher l'image à droite et les détails à gauche
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Section gauche : ID
            Column(
                modifier = Modifier.weight(2f).padding(16.dp)
            ) {
                Text(text = "No. ${pokemon.id}", fontSize = 18.sp)
            }

            // Section droite : Image et nom
            Column(
                modifier = Modifier.weight(1f).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Ici, vous pouvez afficher une image du Pokémon si vous en avez une.
                // AsyncImage(
                //    model = pokemon.imageUrl,
                //    contentDescription = null,
                //    modifier = Modifier.size(150.dp)
                // )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = pokemon.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onDismiss) {
            Text("Fermer")
        }
    }
}