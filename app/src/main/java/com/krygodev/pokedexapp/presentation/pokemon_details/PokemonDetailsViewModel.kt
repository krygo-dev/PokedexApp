package com.krygodev.pokedexapp.presentation.pokemon_details

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krygodev.pokedexapp.domain.repository.PokemonRepository
import com.krygodev.pokedexapp.util.Constants.POKEMON_COLOR
import com.krygodev.pokedexapp.util.Constants.POKEMON_NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
        loadPokemonDetails()
    }

    fun loadPokemonDetails() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, loadError = null) }

            val result = repository.getPokemonDetails(state.value.pokemonName)

            result
                .onSuccess { pokemon ->
                    _state.update { it.copy(
                        pokemon = pokemon,
                        isLoading = false,
                        loadError = null,
                    ) }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            pokemon = null,
                            isLoading = false,
                            loadError = error.message
                        )
                    }
                }
        }
    }

    private fun getNavigationArguments(savedStateHandle: SavedStateHandle) {
        viewModelScope.launch {
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
}