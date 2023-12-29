package com.example.pokedex_omerovic.network

import com.example.pokedex_omerovic.model.PokemonModel
import com.example.pokedex_omerovic.ui.screens.PokedexModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

private const val BASE_URL =
    "https://raw.githubusercontent.com/MegaRaG/CreationJsonCompletPokedex/main/"


private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()
/**
 * Retrofit service object for creating api calls
 */
interface PokedexApiService {
    @GET("pokemon_transformed.json")
    suspend fun getPokemon(): List<PokemonModel>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object PokedexApi {
    val retrofitService: PokedexApiService by lazy {
        retrofit.create(PokedexApiService::class.java)
    }
}
