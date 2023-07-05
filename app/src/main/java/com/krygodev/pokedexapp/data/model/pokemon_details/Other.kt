package com.krygodev.pokedexapp.data.model.pokemon_details

import com.squareup.moshi.Json

data class Other(
    val dream_world: DreamWorld,
    val home: Home,
    @Json(name = "official-artwork")
    val officialArtwork: OfficialArtwork
)