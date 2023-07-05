package com.krygodev.pokedexapp.data.model.pokemon_details

import com.squareup.moshi.Json

data class GenerationI(
    @Json(name = "red-blue")
    val redBlue: RedBlue,
    val yellow: Yellow
)