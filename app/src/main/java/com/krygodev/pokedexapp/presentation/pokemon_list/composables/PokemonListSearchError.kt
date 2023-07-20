package com.krygodev.pokedexapp.presentation.pokemon_list.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.krygodev.pokedexapp.util.TestTags.SEARCH_ERROR

@Composable
fun PokemonListSearchError() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "No results to show.\n Try to load more Pokemons before searching.",
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag(SEARCH_ERROR)
        )
        Spacer(modifier = Modifier.height(64.dp))
    }
}