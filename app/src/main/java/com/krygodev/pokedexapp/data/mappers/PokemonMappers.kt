package com.krygodev.pokedexapp.data.mappers

import androidx.compose.ui.graphics.Color
import com.krygodev.pokedexapp.data.model.pokemon_details.PokemonDetailsModel
import com.krygodev.pokedexapp.data.model.pokemon_list.Result
import com.krygodev.pokedexapp.domain.model.Pokemon
import com.krygodev.pokedexapp.domain.model.PokemonListEntry
import java.util.Locale

fun Result.toPokemonListEntry(): PokemonListEntry {
    val capitalizedName = name.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.ROOT)
        else it.toString()
    }

    val number = if (url.endsWith("/")) {
        url.dropLast(1).takeLastWhile { it.isDigit() }
    } else url.takeLastWhile { it.isDigit() }

    val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"

    return PokemonListEntry(
        name = capitalizedName,
        imageUrl = imageUrl,
        number = number.toInt(),
        dominantColor = Color.Black
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