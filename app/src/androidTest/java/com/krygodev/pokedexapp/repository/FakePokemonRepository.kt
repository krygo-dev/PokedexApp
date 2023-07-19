package com.krygodev.pokedexapp.repository

import com.krygodev.pokedexapp.domain.model.Pokemon
import com.krygodev.pokedexapp.domain.model.PokemonResult
import com.krygodev.pokedexapp.domain.repository.PokemonRepository
import com.krygodev.pokedexapp.repository.test_api_response.validPokemonDetailsResponse
import com.krygodev.pokedexapp.repository.test_api_response.validPokemonListResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

class FakePokemonRepository: PokemonRepository {

    private val moshi = Moshi.Builder().build()
    private val pokemonListAdapter: JsonAdapter<PokemonResult> = moshi.adapter(PokemonResult::class.java)
    private val pokemonDetailsAdapter: JsonAdapter<Pokemon> = moshi.adapter(Pokemon::class.java)

    private val pokemonListResult = pokemonListAdapter.fromJson(validPokemonListResponse)
    private val pokemonDetailsResult = pokemonDetailsAdapter.fromJson(validPokemonDetailsResponse)

    override suspend fun getPokemonList(limit: Int, offset: Int): Result<PokemonResult> {
        val result = pokemonListResult?.pokemonList?.subList(offset, offset + limit)

        return Result.success(
            PokemonResult(
                count = pokemonListResult?.count ?: 0,
                pokemonList = result ?: emptyList()
            )
        )
    }

    override suspend fun getPokemonDetails(name: String): Result<Pokemon> {
        return Result.success(pokemonDetailsResult!!)
    }
}