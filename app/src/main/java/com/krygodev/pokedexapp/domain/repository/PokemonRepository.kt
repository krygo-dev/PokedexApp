package com.krygodev.pokedexapp.domain.repository

import com.krygodev.pokedexapp.domain.model.Pokemon
import com.krygodev.pokedexapp.domain.model.PokemonResult

interface PokemonRepository {

    suspend fun getPokemonList(limit: Int, offset: Int): Result<PokemonResult>
    suspend fun getPokemonDetails(name: String): Result<Pokemon>

}