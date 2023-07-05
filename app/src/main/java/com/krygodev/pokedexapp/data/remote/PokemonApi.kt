package com.krygodev.pokedexapp.data.remote

import com.krygodev.pokedexapp.data.model.pokemon_details.PokemonDetailsModel
import com.krygodev.pokedexapp.data.model.pokemon_list.PokemonListModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonListModel

    @GET("pokemon/{name}")
    suspend fun getPokemonDetails(
        @Path("name") name: String
    ): PokemonDetailsModel

}