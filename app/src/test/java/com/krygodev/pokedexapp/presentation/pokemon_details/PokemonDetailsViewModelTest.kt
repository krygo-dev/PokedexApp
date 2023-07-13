package com.krygodev.pokedexapp.presentation.pokemon_details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.krygodev.pokedexapp.data.model.pokemon_details.Sprites
import com.krygodev.pokedexapp.data.model.pokemon_details.Stat
import com.krygodev.pokedexapp.data.model.pokemon_details.StatX
import com.krygodev.pokedexapp.data.model.pokemon_details.Type
import com.krygodev.pokedexapp.data.model.pokemon_details.TypeX
import com.krygodev.pokedexapp.domain.model.Pokemon
import com.krygodev.pokedexapp.domain.repository.PokemonRepository
import com.krygodev.pokedexapp.util.Constants.POKEMON_COLOR
import com.krygodev.pokedexapp.util.Constants.POKEMON_NAME
import com.krygodev.pokedexapp.util.Constants.UNEXPECTED_ERROR
import com.krygodev.pokedexapp.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonDetailsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: PokemonDetailsViewModel
    private lateinit var fakeSavedStateHandle: SavedStateHandle
    private val mockPokemonRepository = mockk<PokemonRepository>(relaxed = true)

    @Before
    fun setUp() {
        fakeSavedStateHandle = SavedStateHandle(
            mapOf(
                POKEMON_NAME to "Pikachu",
                POKEMON_COLOR to Color.Yellow.toArgb()
            )
        )

        viewModel = PokemonDetailsViewModel(
            repository = mockPokemonRepository,
            savedStateHandle = fakeSavedStateHandle
        )
    }

    @Test
    fun `ViewModel init, pokemon name and color navigation args exists, updates state accordingly`() {
        assertThat(viewModel.state.value.pokemonName).isEqualTo("Pikachu")
        assertThat(viewModel.state.value.pokemonDominantColor).isEqualTo(Color.Yellow)
    }

    @Test
    fun `ViewModel init, pokemon name and color navigation args do not exists, updates state accordingly`() {
        fakeSavedStateHandle = SavedStateHandle()

        viewModel = PokemonDetailsViewModel(
            repository = mockPokemonRepository,
            savedStateHandle = fakeSavedStateHandle
        )

        assertThat(viewModel.state.value.pokemonName).isEqualTo("")
        assertThat(viewModel.state.value.pokemonDominantColor).isEqualTo(Color.Black)
    }

    @Test
    fun `loadPokemonDetails, result successful, returns Pokemon object`() {
        val testName = "Pikachu"
        val testPokemon = Pokemon(
            name = "Pikachu",
            id = 25,
            types = listOf(Type(slot = 1, type = TypeX("", ""))),
            height = 100,
            weight = 50,
            stats = listOf(Stat(1, 1, StatX("", ""))),
            frontSprite = ""
        )

        coEvery { mockPokemonRepository.getPokemonDetails(any()) } returns Result.success(testPokemon)

        viewModel.loadPokemonDetails()

        coVerify { mockPokemonRepository.getPokemonDetails(testName) }
        assertThat(viewModel.state.value.pokemon).isEqualTo(testPokemon)
        assertThat(viewModel.state.value.loadError).isEqualTo(null)
    }

    @Test
    fun `loadPokemonDetails, result unsuccessful, return failure`() {
        val testName = "Pikachu"

        coEvery { mockPokemonRepository.getPokemonDetails(any()) } returns Result.failure(
            Throwable(UNEXPECTED_ERROR)
        )

        viewModel.loadPokemonDetails()

        coVerify { mockPokemonRepository.getPokemonDetails(testName) }
        assertThat(viewModel.state.value.pokemon).isEqualTo(null)
        assertThat(viewModel.state.value.loadError).isEqualTo(UNEXPECTED_ERROR)
    }
}