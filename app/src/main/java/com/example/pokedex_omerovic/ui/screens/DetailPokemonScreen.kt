package com.example.pokedex_omerovic.ui.screens

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.pokedex_omerovic.R
import com.example.pokedex_omerovic.model.PokemonModel
import com.example.pokedex_omerovic.ui.theme.Pokedex_OmerovicTheme
import com.example.pokedex_omerovic.ui.theme.typeColors
import java.util.Locale

@Composable
fun DetailPokemonScreen(
    pokemonId: Int,
    viewModel: PokemonDetailViewModel,
    navController: NavController
) {
    val pokemonDetails = viewModel.pokemonDetailState.collectAsState().value
    val evolutions = viewModel.evolutionsState.collectAsState().value

    LaunchedEffect(pokemonId) {
        viewModel.fetchPokemonDetails(pokemonId)
    }

    if (pokemonDetails != null) {
        PokemonDetailsContent(
            pokemon = pokemonDetails,
            evolutions = evolutions,
            navController = navController
        )
    } else {
        Text("Aucun Pokémon trouvé avec l'ID: $pokemonId", color = Color.Black)
    }
}


@Composable
fun PokemonDetailsContent(
    pokemon: PokemonModel,
    evolutions: List<PokemonModel>,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondpokedexdetail),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                HeaderSection(pokemon.name, pokemon.id, pokemon.type)
                PokemonDetailsRow(pokemon)
                Text(
                    text = "Description: ${pokemon.description}",
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color.Black
                )
                DisplayFamilyPokemon(
                    pokemons = evolutions,
                    currentPokemon = pokemon,
                    navController = navController
                )
                CloseButton(
                    navController = navController, modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterEnd)
                )
            }
        }
    }
}

@Composable
fun CloseButton(navController: NavController, modifier: Modifier = Modifier) {
    IconButton(
        onClick = {
            navController.navigate("listePokemons") {
                popUpTo("listePokemons") { inclusive = true }
            }
        },
        modifier = modifier
    ) {
        val closeIcon = painterResource(id = R.drawable.btnclose)
        Image(
            painter = closeIcon,
            contentDescription = "Fermer",
            modifier = Modifier.size(24.dp)
        )
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
                text = "No. $pokemonId",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                color = Color.Black
            )
            Text(
                text = pokemonName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                color = Color.Black
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
                Text(text = typeName, color = Color.Black)
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
                    .background(
                        backgroundColor1,
                        shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
                    .background(
                        backgroundColor2,
                        shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                    )
                    .align(Alignment.CenterEnd)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor1, shape = RoundedCornerShape(16.dp))
            )
        }
        AsyncImage(
            model = pokemon.imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun DisplayFamilyPokemon(
    pokemons: List<PokemonModel>,
    currentPokemon: PokemonModel,
    navController: NavController
) {
    val sortedEvolutions = (listOf(currentPokemon) + pokemons).sortedBy { it.id }

    Column(modifier = Modifier.fillMaxWidth()) {
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(sortedEvolutions) { pokemon ->
                PokemonItem(
                    pokemon = pokemon,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    onClick = { navController.navigate("detailPokemon/${pokemon.id}") }
                )
                if (sortedEvolutions.indexOf(pokemon) < sortedEvolutions.size - 1) {
                    ArrowPokemon(color = typeColors[pokemon.type.getOrNull(0)] ?: Color.Black)
                }
            }
        }
    }
}

@Composable
fun PokemonItem(pokemon: PokemonModel, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(data = pokemon.imageUrl)
            .apply(block = fun ImageRequest.Builder.() {
            }).build()
    )
    Box(
        modifier = modifier
            .size(70.dp)
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ArrowPokemon(color: Color, size: Dp = 80.dp) {
    Canvas(
        modifier = Modifier
            .size(size)
            .padding(top = 8.dp)
    ) {
        val arrowLength = size.toPx()
        val arrowWidth = size.toPx() / 1f

        // Draw the arrow body
        drawLine(
            color = color,
            start = Offset(0f, arrowWidth / 2),
            end = Offset(arrowLength - arrowWidth / 2, arrowWidth / 2),
            strokeWidth = 8f  // Réduire l'épaisseur de la flèche
        )

        // Draw the arrow tip
        val tipPath = androidx.compose.ui.graphics.Path().apply {
            moveTo(arrowLength - arrowWidth / 2, 0f)
            lineTo(arrowLength, arrowWidth / 2)
            lineTo(arrowLength - arrowWidth / 2, arrowWidth)
            close()
        }
        drawPath(tipPath, color = color)
    }
}