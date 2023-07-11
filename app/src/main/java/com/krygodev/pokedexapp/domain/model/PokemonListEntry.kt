package com.krygodev.pokedexapp.domain.model

import androidx.compose.ui.graphics.Color

data class PokemonListEntry(
    val name: String,
    val imageUrl: String,
    val number: Int,
    val dominantColor: Color
)
