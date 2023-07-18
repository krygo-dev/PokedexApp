package com.krygodev.pokedexapp.presentation.pokemon_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.krygodev.pokedexapp.presentation.pokemon_details.composables.PokemonDetailsDataSection
import com.krygodev.pokedexapp.presentation.pokemon_details.composables.PokemonDetailsErrorSection
import com.krygodev.pokedexapp.presentation.pokemon_details.composables.PokemonDetailsLoadingSection
import com.krygodev.pokedexapp.presentation.pokemon_details.composables.PokemonDetailsTopSection

@Composable
fun PokemonDetailsScreen(
    state: PokemonDetailsState,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(state.pokemonDominantColor!!),
    ) {
        PokemonDetailsTopSection(onBackClick = { navController.popBackStack() })
        if (state.pokemon != null) PokemonDetailsDataSection(state.pokemon)
        if (!state.loadError.isNullOrEmpty()) PokemonDetailsErrorSection(state.loadError)
        if (state.isLoading) PokemonDetailsLoadingSection()
    }
}