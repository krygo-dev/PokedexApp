package com.krygodev.pokedexapp.presentation.pokemon_list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.krygodev.pokedexapp.domain.model.PokemonListEntry
import com.krygodev.pokedexapp.domain.repository.PokemonRepository
import com.krygodev.pokedexapp.util.Constants.UNEXPECTED_ERROR
import com.krygodev.pokedexapp.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonListViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: PokemonListViewModel
    private lateinit var pokemonList: List<PokemonListEntry>
    private val mockPokemonRepository = mockk<PokemonRepository>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = PokemonListViewModel(mockPokemonRepository)

        pokemonList = (0..20).map { index ->
            PokemonListEntry(name = "Pokemon$index", imageUrl = "www.pokemon.pl/image/$index")
        }
    }

    @Test
    fun `loadPokemonPaginated, result successful, returns list of PokemonListEntry`() {
        coEvery { mockPokemonRepository.getPokemonList(any(), any()) } returns Result.success(pokemonList)
        val prevState = viewModel.state.value

        viewModel.loadPokemonPaginated()

        assertThat(viewModel.state.value.pokemonList).isEqualTo(prevState.pokemonList + pokemonList)
        assertThat(viewModel.state.value.currentPage).isEqualTo(prevState.currentPage + 1)
        assertThat(viewModel.state.value.loadError).isNull()
    }

    @Test
    fun `loadPokemonPaginated, result unsuccessful, returns failure`() {
        coEvery { mockPokemonRepository.getPokemonList(any(), any()) } returns Result.failure(
            Throwable(UNEXPECTED_ERROR)
        )

        viewModel.loadPokemonPaginated()

        assertThat(viewModel.state.value.loadError).isEqualTo(UNEXPECTED_ERROR)
    }
}