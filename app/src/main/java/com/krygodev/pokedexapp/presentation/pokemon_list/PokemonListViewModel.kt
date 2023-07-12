package com.krygodev.pokedexapp.presentation.pokemon_list

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
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
            is PokemonListEvent.CalculateDominantColor -> {
                val color = calculateDominantColor(event.drawable)
                val index = state.value.pokemonList.indexOf(event.pokemonListEntry)
                val updatedPokemon = state.value.pokemonList[index].copy(dominantColor = color ?: Color.Black)
                val mutablePokemonList = state.value.pokemonList.toMutableList()
                mutablePokemonList[index] = updatedPokemon

                _state.update { it.copy(
                    pokemonList = mutablePokemonList
                ) }
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
                .onSuccess { pokemonResult ->
                    val appendedPokemonList = state.value.pokemonList + pokemonResult.pokemonList
                    _state.update {
                        it.copy(
                            pokemonList = appendedPokemonList,
                            cachedPokemonList = appendedPokemonList,
                            isLoading = false,
                            loadError = null,
                            currentPage = state.value.currentPage + 1,
                            endReached = state.value.currentPage * PAGE_SIZE >= pokemonResult.count
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
        _state.update { it.copy(isSearching = true) }

        if (query.isEmpty() && state.value.isSearching) {
            _state.update { it.copy(pokemonList = it.cachedPokemonList, isSearching = false) }
        }

        val filteredList = state.value.cachedPokemonList.filter {
            it.name.contains(query.trim(), ignoreCase = true) || it.number.toString() == query.trim()
        }

        _state.update { it.copy(pokemonList = filteredList) }
    }

    private fun calculateDominantColor(drawable: Drawable): Color? {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        val palette = Palette.from(bmp).generate()
        return palette.dominantSwatch?.rgb?.let { colorValue ->
            Color(colorValue)
        }
    }
}