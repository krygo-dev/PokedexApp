package com.krygodev.pokedexapp.presentation.pokemon_list

sealed interface PokemonListEvent {
    data class SetSearchQuery(val query: String): PokemonListEvent
    object LoadPokemonPaginated: PokemonListEvent
}
