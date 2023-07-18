package com.krygodev.pokedexapp.presentation.pokemon_list.composables

import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.krygodev.pokedexapp.domain.model.PokemonListEntry
import com.krygodev.pokedexapp.ui.theme.RobotoCondensed

@Composable
fun PokemonListItem(
    pokemonEntry: PokemonListEntry,
    onClick: () -> Unit,
    onLoadImageSuccess: (drawable: Drawable) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(8.dp)
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        pokemonEntry.dominantColor,
                        Color.LightGray
                    )
                )
            )
            .padding(4.dp)
            .clickable {
                onClick()
            }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SubcomposeAsyncImage(
                model = pokemonEntry.imageUrl,
                contentDescription = pokemonEntry.name,
                loading = {
                    Box(
                        modifier = Modifier.height(150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                },
                onSuccess = { success ->
                    onLoadImageSuccess(success.result.drawable)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp),
            )
            Text(
                text = pokemonEntry.name,
                fontFamily = RobotoCondensed,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}