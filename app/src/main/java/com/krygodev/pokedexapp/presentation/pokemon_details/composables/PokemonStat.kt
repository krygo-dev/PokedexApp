package com.krygodev.pokedexapp.presentation.pokemon_details.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.krygodev.pokedexapp.data.model.pokemon_details.Stat
import com.krygodev.pokedexapp.util.parseStatToAbbr
import com.krygodev.pokedexapp.util.parseStatToColor

@Composable
fun PokemonStat(
    stat: Stat,
    maxValue: Int,
    height: Dp = 28.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    var animationPlayed by remember { mutableStateOf(false) }
    val currentPercent = animateFloatAsState(
        targetValue = if (animationPlayed) {
            stat.base_stat / maxValue.toFloat()
        } else 0f,
        animationSpec = tween(animDuration, animDelay)
    )
    val statAbbr = remember { parseStatToAbbr(stat) }
    val statColor = remember { parseStatToColor(stat) }

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(height)
        .clip(CircleShape)
        .background(
            if (isSystemInDarkTheme()) {
                Color(0xFF505050)
            } else {
                Color.LightGray
            }
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(currentPercent.value)
                .clip(CircleShape)
                .background(statColor)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = statAbbr,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = (currentPercent.value * maxValue).toInt().toString(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}