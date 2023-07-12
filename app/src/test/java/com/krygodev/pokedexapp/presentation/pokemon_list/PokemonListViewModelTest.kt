package com.krygodev.pokedexapp.presentation.pokemon_list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.graphics.Color
import com.google.common.truth.Truth.assertThat
import com.krygodev.pokedexapp.domain.model.PokemonListEntry
import com.krygodev.pokedexapp.domain.model.PokemonResult
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
    private lateinit var pokemonResult: PokemonResult
    private val mockPokemonRepository = mockk<PokemonRepository>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = PokemonListViewModel(mockPokemonRepository)

        val pokemonList = (0..20).map { index ->
            PokemonListEntry(
                name = "Pokemon$index",
                imageUrl = "www.pokemon.pl/image/$index",
                number = index,
                dominantColor = Color.Black
            )
        }
        pokemonResult = PokemonResult(count = 20, pokemonList = pokemonList)
    }

    @Test
    fun `loadPokemonPaginated, result successful, returns list of PokemonListEntry`() {
        coEvery { mockPokemonRepository.getPokemonList(any(), any()) } returns Result.success(
            pokemonResult
        )
        val prevState = viewModel.state.value

        viewModel.onEvent(PokemonListEvent.LoadPokemonPaginated)

        assertThat(viewModel.state.value.pokemonList).isEqualTo(
            prevState.pokemonList + pokemonResult.pokemonList
        )
        assertThat(viewModel.state.value.currentPage).isEqualTo(prevState.currentPage + 1)
        assertThat(viewModel.state.value.loadError).isNull()
    }

    @Test
    fun `loadPokemonPaginated, result unsuccessful, returns failure`() {
        coEvery { mockPokemonRepository.getPokemonList(any(), any()) } returns Result.failure(
            Throwable(UNEXPECTED_ERROR)
        )

        viewModel.onEvent(PokemonListEvent.LoadPokemonPaginated)

        assertThat(viewModel.state.value.loadError).isEqualTo(UNEXPECTED_ERROR)
    }

    @Test
    fun `searchPokemonList, filtered results is not empty list, set pokemonList state to filtered results`() {
        val testQuery = "1"
        val prevState = viewModel.state.value

        viewModel.onEvent(PokemonListEvent.SetSearchQuery(testQuery))

        val expectedList = viewModel.state.value.pokemonList.filter { it.name.contains(testQuery) }

        assertThat(viewModel.state.value.pokemonList).isEqualTo(expectedList)
        assertThat(viewModel.state.value.cachedPokemonList).isEqualTo(prevState.cachedPokemonList)
    }

    @Test
    fun `searchPokemonList, filtered results is empty list, set pokemonList state to filtered results`() {
        val testQuery = "100"
        val prevState = viewModel.state.value

        viewModel.onEvent(PokemonListEvent.SetSearchQuery(testQuery))

        val expectedList = viewModel.state.value.pokemonList.filter { it.name.contains(testQuery) }

        assertThat(viewModel.state.value.pokemonList).isEqualTo(expectedList)
        assertThat(viewModel.state.value.cachedPokemonList).isEqualTo(prevState.cachedPokemonList)
    }
}