package com.krygodev.pokedexapp.data.model.pokemon_details

import com.squareup.moshi.Json

data class IconsX(
    @Json(name = "front_default")
    val frontDefault: String,
    @Json(name = "front_female")
    val frontFemale: Any
)
