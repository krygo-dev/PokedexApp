package com.krygodev.pokedexapp.presentation.pokemon_details.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.krygodev.pokedexapp.domain.model.Pokemon

@Composable
fun PokemonDetailsDataSection(
    pokemon: Pokemon
) {
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        AsyncImage(
            model = pokemon.frontSprite,
            contentDescription = "Pokemon front sprite",
            modifier = Modifier.size(200.dp)
        )

        Text(
            text = "#${pokemon.id} ${pokemon.name}",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(size = 40f))
                .background(Color.White)
                .padding(16.dp)
        ) {
            PokemonDetailsTypeSection(pokemon = pokemon)
            PokemonDetailsWeightAndHeightSection(pokemon = pokemon)
            PokemonDetailsStatSection(pokemon = pokemon)
        }
    }
}