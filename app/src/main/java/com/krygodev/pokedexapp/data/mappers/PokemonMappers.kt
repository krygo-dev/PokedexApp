package com.krygodev.pokedexapp.data.mappers

import com.krygodev.pokedexapp.data.model.pokemon_details.PokemonDetailsModel
import com.krygodev.pokedexapp.data.model.pokemon_list.Result
import com.krygodev.pokedexapp.domain.model.Pokemon
import com.krygodev.pokedexapp.domain.model.PokemonListEntry

fun Result.toPokemonListEntry(): PokemonListEntry {
    return PokemonListEntry(
        name = name,
        url = url
    )
}

fun PokemonDetailsModel.toPokemon(): Pokemon {
    return Pokemon(
        id = id,
        name = name,
        types = types,
        height = height,
        weight = weight,
        stats = stats
    )
}