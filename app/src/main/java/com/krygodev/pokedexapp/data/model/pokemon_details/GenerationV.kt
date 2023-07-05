package com.krygodev.pokedexapp.data.model.pokemon_details

import com.squareup.moshi.Json

data class GenerationV(
    @Json(name = "black-white")
    val blackWhite: BlackWhite
)