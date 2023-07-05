package com.krygodev.pokedexapp.data.model.pokemon_list

data class PokemonListModel(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)