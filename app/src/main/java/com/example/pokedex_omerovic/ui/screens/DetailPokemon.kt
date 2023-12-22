package com.example.pokedex_omerovic.ui.screens

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.rememberImagePainter
import com.example.pokedex_omerovic.R
import com.example.pokedex_omerovic.ui.theme.typeColors

class DetailPokemon : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pokemonId = intent.getIntExtra("pokemonId", 0)
        val evolutionsBeforeIds: ArrayList<Int>? = intent.getIntegerArrayListExtra("EvolutionsBeforeIds")
        val evolutionsAfterIds: ArrayList<Int>? = intent.getIntegerArrayListExtra("EvolutionsAfterIds")
        Log.d("PokemonDetails", "Pokemon ID: $pokemonId")
        Log.d("PokemonDetails", "Evolutions Before IDs: $evolutionsBeforeIds")
        Log.d("PokemonDetails", "Evolutions After IDs: $evolutionsAfterIds")

        CoroutineScope(Dispatchers.IO).launch {
            val allPokemons = PokedexApi.retrofitService.getPokemon()

            val pokemonDetails = allPokemons.find { it.id == pokemonId }
            val pokemonFamille1 = evolutionsBeforeIds?.let { ids ->
                allPokemons.find { it.id == ids.firstOrNull() }
            }
            val pokemonFamille2 = evolutionsBeforeIds?.let { ids ->
                if (ids.size > 1) allPokemons.find { it.id == ids[1] } else null
            }

            withContext(Dispatchers.Main) {
                setContent {
                    Pokedex_OmerovicTheme {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            if (pokemonDetails != null) {
                                PokemonDetailsContent(
                                    pokemon = pokemonDetails,
                                    famille1 = pokemonFamille1,
                                    famille2 = pokemonFamille2
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
fun PokemonDetailsContent(pokemon: PokemonModel, famille1: PokemonModel?, famille2: PokemonModel?, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White) // Couleur de fond par défaut
    ) {
        // Fond bleu transparent
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0x600000FF), shape = RectangleShape) // Bleu transparent
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderSection(pokemon.name, pokemon.id) // Cette section contiendra maintenant l'image
            PokemonDetailsRow(pokemon)

            // Afficher les familles du Pokémon actuel
            DisplayFamilyPokemon(pokemon = pokemon, evolutionsBefore = listOfNotNull(famille1), evolutionsAfter = listOfNotNull(famille2))

            Spacer(modifier = Modifier.height(16.dp))
            CloseButton(onDismiss)
        }
    }
}

@Composable
fun HeaderSection(pokemonName: String, pokemonId: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Hauteur maximale de l'image
    ) {
        // Image de fond en mode paysage
        Image(
            painter = painterResource(id = R.drawable.bgfeu),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Aligner les textes à la même hauteur en utilisant Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp), // Ajout de padding pour espacement
            horizontalArrangement = Arrangement.SpaceBetween, // Espacement entre les deux textes
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pokemon ID à gauche
            Text(
                text = "No. ${pokemonId}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )

            // Pokemon Name à droite
            Text(
                text = pokemonName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun PokemonDetailsRow(pokemon: PokemonModel) {
    Row(modifier = Modifier.fillMaxWidth()) {
        PokemonTypeSection(pokemon)
        Spacer(modifier = Modifier.width(40.dp))
        PokemonImageSection(pokemon)
    }
}
@Composable
fun PokemonTypeSection(pokemon: PokemonModel) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        pokemon.type.take(2).forEach { typeName ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                getDrawableForType(typeName)?.let { painterResource(id = it) }?.let {
                    Image(
                        painter = it,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = typeName, color = Color.White)
            }
        }
    }
}

@Composable
fun PokemonImageSection(pokemon: PokemonModel) {
    val backgroundColor1 = typeColors[pokemon.type.getOrNull(0)] ?: Color.Gray
    val backgroundColor2 = typeColors[pokemon.type.getOrNull(1)] ?: Color.Gray

    Box(
        modifier = Modifier
            .padding(16.dp)
            .size(150.dp) // Taille de la boîte d'image
    ) {
        // Si le Pokémon a deux types, divisez la boîte en deux couleurs
        if (pokemon.type.size == 2) {
            // La première couleur occupe la moitié gauche
            Box(
                modifier = Modifier
                    .fillMaxHeight() // Remplir la hauteur de la boîte
                    .fillMaxWidth(0.5f) // Remplir seulement la moitié de la largeur
                    .background(backgroundColor1, shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
            )

            // La deuxième couleur occupe la moitié droite
            Box(
                modifier = Modifier
                    .fillMaxHeight() // Remplir la hauteur de la boîte
                    .fillMaxWidth(0.5f) // Remplir seulement la moitié de la largeur
                    .background(backgroundColor2, shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                    .align(Alignment.CenterEnd) // Alignement à droite
            )
        } else {
            // Si le Pokémon a un seul type, remplissez la boîte avec cette couleur
            Box(
                modifier = Modifier
                    .fillMaxSize() // Remplissez toute la boîte
                    .background(backgroundColor1, shape = RoundedCornerShape(16.dp)) // Utilisez la couleur du type avec des coins arrondis
            )
        }

        // Image du Pokémon au-dessus
        AsyncImage(
            model = pokemon.imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize() // Remplissez l'espace de la boîte
        )
    }
}


@Composable
fun DisplayFamilyPokemon(pokemon: PokemonModel, evolutionsBefore: List<PokemonModel>, evolutionsAfter: List<PokemonModel>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display evolutions before if they exist
        if (evolutionsBefore.isNotEmpty()) {
            Text("Evolutions Before:")
            evolutionsBefore.forEach { pokemonBefore ->
                Spacer(modifier = Modifier.height(8.dp))
                AsyncImage(
                    model = pokemonBefore.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
                Text(text = pokemonBefore.name)
            }
        }

        // Display evolutions after if they exist
        if (evolutionsAfter.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Evolutions After:")
            evolutionsAfter.forEach { pokemonAfter ->
                Spacer(modifier = Modifier.height(8.dp))
                AsyncImage(
                    model = pokemonAfter.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
                Text(text = pokemonAfter.name)
            }
        }
    }
}

@Composable
fun CloseButton(onDismiss: () -> Unit) {
    TextButton(onClick = onDismiss) {
        Text("Fermer")
    }
}
