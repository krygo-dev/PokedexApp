package com.krygodev.pokedexapp.repository

import com.krygodev.pokedexapp.data.mappers.toPokemon
import com.krygodev.pokedexapp.data.mappers.toPokemonListEntry
import com.krygodev.pokedexapp.data.model.pokemon_details.PokemonDetailsModel
import com.krygodev.pokedexapp.data.model.pokemon_list.PokemonListModel
import com.krygodev.pokedexapp.domain.model.Pokemon
import com.krygodev.pokedexapp.domain.model.PokemonResult
import com.krygodev.pokedexapp.domain.repository.PokemonRepository
import com.krygodev.pokedexapp.repository.test_api_response.validPokemonDetailsResponse
import com.krygodev.pokedexapp.repository.test_api_response.validPokemonListResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

class FakePokemonRepository : PokemonRepository {

    private val moshi = Moshi.Builder().build()
    private val pokemonListAdapter: JsonAdapter<PokemonListModel> =
        moshi.adapter(PokemonListModel::class.java)
    private val pokemonDetailsAdapter: JsonAdapter<PokemonDetailsModel> =
        moshi.adapter(PokemonDetailsModel::class.java)

    private val pokemonListModel = pokemonListAdapter.fromJson(validPokemonListResponse)
    private val pokemonDetailsModel = pokemonDetailsAdapter.fromJson(validPokemonDetailsResponse)

    override suspend fun getPokemonList(limit: Int, offset: Int): Result<PokemonResult> {
        val size = pokemonListModel?.results?.size!!
        val end = if (offset + limit <= size) offset + limit else size

        if (offset >= size) return Result.success(PokemonResult(
            count = pokemonListModel.count,
            pokemonList = emptyList()
        ))

        val result = pokemonListModel
            .results
            .subList(offset, end)
            .map { it.toPokemonListEntry() }

        return Result.success(
            PokemonResult(
                count = pokemonListModel.count,
                pokemonList = result
            )
        )
    }

    override suspend fun getPokemonDetails(name: String): Result<Pokemon> {
        return Result.success(pokemonDetailsModel?.toPokemon()!!)
    }
}