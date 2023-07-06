package com.krygodev.pokedexapp.presentation.pokemon_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krygodev.pokedexapp.domain.repository.PokemonRepository
import com.krygodev.pokedexapp.util.Constants.PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(PokemonListState())
    val state = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadPokemonPaginated()
    }

    fun onEvent(event: PokemonListEvent) {
        when (event) {
            is PokemonListEvent.SetSearchQuery -> {
                _state.update {
                    it.copy(
                        searchQuery = event.query
                    )
                }

                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500)
                    searchPokemonList(state.value.searchQuery)
                }
            }

            is PokemonListEvent.LoadPokemonPaginated -> loadPokemonPaginated()
        }
    }

    private fun loadPokemonPaginated() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, loadError = null) }

            val result = repository.getPokemonList(
                limit = PAGE_SIZE,
                offset = PAGE_SIZE * state.value.currentPage
            )

            result
                .onSuccess { pokemonList ->
                    _state.update {
                        it.copy(
                            pokemonList = state.value.pokemonList + pokemonList,
                            isLoading = false,
                            loadError = null,
                            currentPage = state.value.currentPage + 1
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            loadError = error.message,
                            isLoading = false
                        )
                    }
                }
        }
    }

    private fun searchPokemonList(query: String) {
//        viewModelScope.launch {
//            if (query.isEmpty()) {
//
//            }
//        }
    }
}