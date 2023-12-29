package com.example.pokedex_omerovic.ui.screens

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.pokedex_omerovic.R
import com.example.pokedex_omerovic.ui.theme.typeColors
import java.util.Locale

class DetailPokemon : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pokemonId = intent.getIntExtra("pokemonId", 0)
        val evolutionsBeforeIds: ArrayList<Int>? = intent.getIntegerArrayListExtra("EvolutionsBeforeIds")
        val evolutionsAfterIds: ArrayList<Int>? = intent.getIntegerArrayListExtra("EvolutionsAfterIds")

        CoroutineScope(Dispatchers.IO).launch {
            val allPokemons = PokedexApi.retrofitService.getPokemon()

            val pokemonDetails = allPokemons.find { it.id == pokemonId }
            val pokemonFamille1 = evolutionsBeforeIds?.let { ids ->
                allPokemons.find { it.id == ids.firstOrNull() }
            }
            val pokemonFamille2 = evolutionsBeforeIds?.let { ids ->
                if (ids.size > 1) allPokemons.find { it.id == ids[1] } else null
            }
            val pokemonFamilleAfter1 = evolutionsAfterIds?.let { ids ->
                allPokemons.find { it.id == ids.firstOrNull() }
            }

            val pokemonFamilleAfter2 = evolutionsAfterIds?.let { ids ->
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
                                    famille2 = pokemonFamille2,
                                    familleAfter1 = pokemonFamilleAfter1,
                                    familleAfter2 = pokemonFamilleAfter2
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
fun PokemonDetailsContent(
    pokemon: PokemonModel,
    famille1: PokemonModel?,
    famille2: PokemonModel?,
    familleAfter1: PokemonModel?,
    familleAfter2: PokemonModel?,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0x600000FF), shape = RectangleShape)
        )

        // Utilisez un LazyColumn pour rendre le contenu scrollable
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                HeaderSection(pokemon.name, pokemon.id, pokemon.type)
                PokemonDetailsRow(pokemon)

                if (famille1 != null) {
                    DisplayFamilyPokemonBefore(evolutionsBefore = listOf(famille1))
                }

                if (famille2 != null) {
                    DisplayFamilyPokemonBefore(evolutionsBefore = listOf(famille2))
                }

                if (familleAfter1 != null) {
                    DisplayFamilyPokemonAfter(evolutionsAfter = listOf(familleAfter1))
                }

                if (familleAfter2 != null) {
                    DisplayFamilyPokemonAfter(evolutionsAfter = listOf(familleAfter2))
                }
                CloseButton(onDismiss = onDismiss, modifier = Modifier
                    .fillMaxSize() // Prend toute la taille disponible
                    .align(Alignment.CenterEnd) // Centrer à l'extrémité droite (end)
                )

            }
        }
    }
}
@Composable
fun HeaderSection(pokemonName: String, pokemonId: Int, pokemonType: List<String>) {
    val backgroundResourceId = getBackgroundResourceForType(pokemonType.firstOrNull())

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = backgroundResourceId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
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

fun getBackgroundResourceForType(type: String?): Int {
    return when (type?.lowercase(Locale.getDefault())) {
        "feu" -> R.drawable.bgfeu
        "eau" -> R.drawable.bgeau
        "plante" -> R.drawable.bgplante
        "vol" -> R.drawable.bgvol
        "insecte" -> R.drawable.bginsecte
        else -> R.drawable.bgdefault
    }
}


@Composable
fun PokemonDetailsRow(pokemon: PokemonModel) {
    Row(modifier = Modifier.fillMaxWidth()) {
        PokemonTypeSection(pokemon, context = LocalContext.current)
        Spacer(modifier = Modifier.width(40.dp))
        PokemonImageSection(pokemon)
    }
}
@Composable
fun PokemonTypeSection(pokemon: PokemonModel, context: Context) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        pokemon.type.take(2).forEach { typeName ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                getDrawableForType(context, typeName)?.let { painterResource(id = it) }?.let {
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
            .size(150.dp)
    ) {
        if (pokemon.type.size == 2) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
                    .background(backgroundColor1, shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
                    .background(backgroundColor2, shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                    .align(Alignment.CenterEnd)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor1, shape = RoundedCornerShape(16.dp))
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
fun DisplayFamilyPokemonAfter(evolutionsAfter: List<PokemonModel>) {
    if (evolutionsAfter.isNotEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Evolutions After:")
            evolutionsAfter.forEach { pokemonAfter ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        model = pokemonAfter.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = pokemonAfter.name,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun DisplayFamilyPokemonBefore(evolutionsBefore: List<PokemonModel>) {
    if (evolutionsBefore.isNotEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Evolutions Before:")
            evolutionsBefore.forEach { pokemonBefore ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        model = pokemonBefore.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = pokemonBefore.name,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
@Composable
fun CloseButton(onDismiss: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onDismiss, modifier = modifier) {
        val closeIcon = painterResource(id = R.drawable.btnclose)
        Image(
            painter = closeIcon,
            contentDescription = "Fermer",
            modifier = Modifier.size(24.dp)
        )
    }
}

