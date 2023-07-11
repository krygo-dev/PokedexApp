package com.krygodev.pokedexapp.presentation.pokemon_list

import android.graphics.drawable.Drawable
import com.krygodev.pokedexapp.domain.model.PokemonListEntry

sealed interface PokemonListEvent {
    data class SetSearchQuery(val query: String) : PokemonListEvent
    data class CalculateDominantColor(
        val pokemonListEntry: PokemonListEntry,
        val drawable: Drawable,
    ) : PokemonListEvent

    object LoadPokemonPaginated : PokemonListEvent
}
