package com.krygodev.pokedexapp.data.mappers

import com.google.common.truth.Truth.assertThat
import com.krygodev.pokedexapp.data.model.pokemon_list.Result
import com.krygodev.pokedexapp.domain.model.PokemonListEntry
import com.krygodev.pokedexapp.ui.theme.lightGrey
import org.junit.Test


class PokemonMappersTest {
    @Test
    fun `Result  toPokemonListEntry, return valid PokemonListEntry`() {
        val testResult = Result(name = "pikachu", url = "https://pokeapi.co/api/v2/pokemon/25/")
        val testPokemonListEntry = PokemonListEntry(
            name = "Pikachu",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png",
            number = 25,
            dominantColor = lightGrey
        )

        val mapped = testResult.toPokemonListEntry()

        assertThat(mapped).isInstanceOf(PokemonListEntry::class.java)
        assertThat(mapped).isEqualTo(testPokemonListEntry)
    }
}