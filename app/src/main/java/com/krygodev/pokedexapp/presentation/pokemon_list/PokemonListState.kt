package com.krygodev.pokedexapp.presentation.pokemon_list

import com.krygodev.pokedexapp.domain.model.PokemonListEntry

data class PokemonListState(
    val pokemonList: List<PokemonListEntry> = emptyList(),
    val loadError: String? = null,
    val isLoading: Boolean = false,
    val endReached: Boolean = false,
    val isSearching: Boolean = false,
    val searchQuery: String = "",
    val currentPage: Int = 0,
    val cachedPokemonList: List<PokemonListEntry> = emptyList()
)
