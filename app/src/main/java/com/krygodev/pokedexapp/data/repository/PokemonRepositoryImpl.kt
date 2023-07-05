package com.krygodev.pokedexapp.data.repository

import com.krygodev.pokedexapp.data.mappers.toPokemon
import com.krygodev.pokedexapp.data.mappers.toPokemonListEntry
import com.krygodev.pokedexapp.data.remote.PokemonApi
import com.krygodev.pokedexapp.domain.model.Pokemon
import com.krygodev.pokedexapp.domain.model.PokemonListEntry
import com.krygodev.pokedexapp.domain.repository.PokemonRepository
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val api: PokemonApi
): PokemonRepository {
    override suspend fun getPokemonList(limit: Int, offset: Int): Result<List<PokemonListEntry>> {
        return try {
            val response = api.getPokemonList(limit = limit, offset = offset)
            val pokemonList = response.results.map { it.toPokemonListEntry() }
            Result.success(pokemonList)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getPokemonDetails(name: String): Result<Pokemon> {
        return try {
            val response = api.getPokemonDetails(name = name)
            Result.success(response.toPokemon())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}