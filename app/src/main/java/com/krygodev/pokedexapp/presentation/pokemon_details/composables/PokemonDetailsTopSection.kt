package com.krygodev.pokedexapp.presentation.pokemon_details.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.krygodev.pokedexapp.util.Constants.BACK
import com.krygodev.pokedexapp.util.TestTags.BACK_ARROW

@Composable
fun PokemonDetailsTopSection(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(
            onClick = { onBackClick() },
            modifier = Modifier
                .size(44.dp)
                .offset(8.dp, 8.dp)
                .testTag(BACK_ARROW)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = BACK,
                tint = Color.White,
            )
        }
    }
}