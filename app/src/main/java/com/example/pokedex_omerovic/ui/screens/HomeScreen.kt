package com.example.pokedex_omerovic.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.pokedex_omerovic.R
import com.example.pokedex_omerovic.model.PokemonModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import com.example.pokedex_omerovic.ui.theme.typeColors
@Composable
fun ResultScreen(pokemons: List<PokemonModel>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        content = {
            items(pokemons) { pokemon ->
                PokemonCard(pokemon = pokemon)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    )
}
@Composable
fun PokemonCard(pokemon: PokemonModel) {
    val typeColor = typeColors[pokemon.type.firstOrNull()] ?: Color.Gray
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(120.dp)
            .clickable {
                val intent = Intent(context, DetailPokemon::class.java)
                intent.putExtra("pokemonId", pokemon.id)

                if (pokemon.evolutions.before.isNotEmpty()) {
                    intent.putIntegerArrayListExtra("EvolutionsBeforeIds", ArrayList(pokemon.evolutions.before))
                }

                if (pokemon.evolutions.after.isNotEmpty()) {
                    intent.putIntegerArrayListExtra("EvolutionsAfterIds", ArrayList(pokemon.evolutions.after))
                }

                context.startActivity(intent)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(color = typeColor)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(text = "No. ${pokemon.id}")

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

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                AsyncImage(
                    model = pokemon.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Text(
                    text = pokemon.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun getDrawableForType(context: Context, type: String): Int? {
    val resourceId = when (type) {
        context.getString(R.string.Pokemon_Type_Eau) -> R.drawable.eau
        context.getString(R.string.Pokemon_Type_Feu) -> R.drawable.feu
        context.getString(R.string.Pokemon_Type_Plante) -> R.drawable.plante
        context.getString(R.string.Pokemon_Type_Poison) -> R.drawable.poison
        context.getString(R.string.Pokemon_Type_Insecte) -> R.drawable.insecte
        context.getString(R.string.Pokemon_Type_Vol) -> R.drawable.vol
        context.getString(R.string.Pokemon_Type_Normal) -> R.drawable.normal
        else -> null
    }
    return resourceId
}


@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}
@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}