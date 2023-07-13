package com.krygodev.pokedexapp.domain.model

import com.krygodev.pokedexapp.data.model.pokemon_details.Sprites
import com.krygodev.pokedexapp.data.model.pokemon_details.Stat
import com.krygodev.pokedexapp.data.model.pokemon_details.Type

data class Pokemon(
    val id: Int,
    val name: String,
    val types: List<Type>,
    val height: Int,
    val weight: Int,
    val stats: List<Stat>,
    val frontSprite: String
)
