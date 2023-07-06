package com.krygodev.pokedexapp.presentation.pokemon_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krygodev.pokedexapp.domain.repository.PokemonRepository
import com.krygodev.pokedexapp.util.Constants.PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository,
): ViewModel() {

    private val _state = MutableStateFlow(PokemonListState())
    val state = _state.asStateFlow()

    init {
        loadPokemonPaginated()
    }

    fun loadPokemonPaginated() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, loadError = null) }

            val result = repository.getPokemonList(
                limit = PAGE_SIZE,
                offset = PAGE_SIZE * state.value.currentPage
            )

            result
                .onSuccess { pokemonList ->
                    _state.update { it.copy(
                        pokemonList = state.value.pokemonList + pokemonList,
                        isLoading = false,
                        loadError = null,
                        currentPage = state.value.currentPage + 1
                    ) }
                }
                .onFailure { error ->
                    _state.update { it.copy(
                        loadError = error.message,
                        isLoading = false
                    ) }
                }
        }
    }
}