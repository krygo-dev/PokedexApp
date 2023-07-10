package com.krygodev.pokedexapp.domain.model

data class PokemonResult(
    val count: Int,
    val pokemonList: List<PokemonListEntry>
)
