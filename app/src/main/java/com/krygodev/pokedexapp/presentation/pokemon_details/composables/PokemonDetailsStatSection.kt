package com.krygodev.pokedexapp.presentation.pokemon_details.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krygodev.pokedexapp.domain.model.Pokemon

@Composable
fun PokemonDetailsStatSection(
    pokemon: Pokemon
) {
    val maxStatValue = remember {
        pokemon.stats.maxOf { it.base_stat }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Base stats:",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        pokemon.stats.forEachIndexed { index, stat ->
            PokemonStat(
                stat = stat,
                maxValue = maxStatValue,
                animDelay = index * 100
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}