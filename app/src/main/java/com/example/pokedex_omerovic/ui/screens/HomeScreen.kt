package com.example.pokedex_omerovic.ui.screens

import android.content.Context
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
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.pokedex_omerovic.R
import com.example.pokedex_omerovic.model.PokemonModel
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pokedex_omerovic.ui.theme.typeColors

@Composable
fun PokemonCard(pokemon: PokemonModel, navController: NavController) {
    val typeColor = typeColors[pokemon.type.firstOrNull()] ?: Color.Gray
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(120.dp)
            .clickable {
                navController.navigate("detailPokemon/${pokemon.id}")
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
                Text(text = "No. ${pokemon.id}", color = Color.White)
                pokemon.type.take(2).forEach { typeName ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        getDrawableForType(context, typeName)?.let { painterResource(id = it) }
                            ?.let {
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
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun getDrawableForType(context: Context, type: String): Int? {
    val resourceId = when (type) {
        context.getString(R.string.Pokemon_Type_Feu) -> R.drawable.feu
        context.getString(R.string.Pokemon_Type_Eau) -> R.drawable.eau
        context.getString(R.string.Pokemon_Type_Plante) -> R.drawable.plante
        context.getString(R.string.Pokemon_Type_Electrik) -> R.drawable.electrik
        context.getString(R.string.Pokemon_Type_Glace) -> R.drawable.glace
        context.getString(R.string.Pokemon_Type_Combat) -> R.drawable.combat
        context.getString(R.string.Pokemon_Type_Poison) -> R.drawable.poison
        context.getString(R.string.Pokemon_Type_Sol) -> R.drawable.sol
        context.getString(R.string.Pokemon_Type_Vol) -> R.drawable.vol
        context.getString(R.string.Pokemon_Type_Psy) -> R.drawable.psy
        context.getString(R.string.Pokemon_Type_Insecte) -> R.drawable.insecte
        context.getString(R.string.Pokemon_Type_Roche) -> R.drawable.roche
        context.getString(R.string.Pokemon_Type_Spectre) -> R.drawable.spectre
        context.getString(R.string.Pokemon_Type_Dragon) -> R.drawable.dragon
        context.getString(R.string.Pokemon_Type_Tenebres) -> R.drawable.tenebres
        context.getString(R.string.Pokemon_Type_Acier) -> R.drawable.acier
        context.getString(R.string.Pokemon_Type_Fee) -> R.drawable.fee
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