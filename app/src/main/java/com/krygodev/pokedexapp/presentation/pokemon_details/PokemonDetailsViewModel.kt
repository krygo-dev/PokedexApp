package com.krygodev.pokedexapp.presentation.pokemon_details

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.krygodev.pokedexapp.domain.repository.PokemonRepository
import com.krygodev.pokedexapp.util.Constants.POKEMON_COLOR
import com.krygodev.pokedexapp.util.Constants.POKEMON_NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val repository: PokemonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(PokemonDetailsState())
    val state = _state.asStateFlow()

    init {
        getNavigationArguments(savedStateHandle)
    }

    private fun loadPokemonDetails() {

    }

    private fun getNavigationArguments(savedStateHandle: SavedStateHandle) {
        val pokemonName = savedStateHandle.get<String>(POKEMON_NAME).orEmpty()
        val pokemonColorInt = savedStateHandle.get<Int>(POKEMON_COLOR)
        val pokemonColor = if (pokemonColorInt != null) Color(pokemonColorInt) else Color.Black

        _state.update {
            it.copy(
                pokemonName = pokemonName,
                pokemonDominantColor = pokemonColor
            )
        }
    }
}