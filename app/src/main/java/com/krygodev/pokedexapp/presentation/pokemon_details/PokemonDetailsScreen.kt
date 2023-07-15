package com.krygodev.pokedexapp.presentation.pokemon_details

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.krygodev.pokedexapp.R
import com.krygodev.pokedexapp.data.model.pokemon_details.Stat
import com.krygodev.pokedexapp.domain.model.Pokemon
import com.krygodev.pokedexapp.util.parseStatToAbbr
import com.krygodev.pokedexapp.util.parseStatToColor
import com.krygodev.pokedexapp.util.parseTypeToColor
import java.util.Locale
import kotlin.math.round

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
        PokemonDetailsStateWrapper(state = state)
    }
}

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
                .size(36.dp)
                .offset(16.dp, 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black,
            )
        }
    }
}

@Composable
fun PokemonDetailsStateWrapper(
    state: PokemonDetailsState
) {
    if (state.pokemon != null) PokemonDetailsDataSection(state.pokemon)
    if (!state.loadError.isNullOrEmpty()) PokemonDetailsErrorSection(state.loadError)
    if (state.isLoading) PokemonDetailsLoadingSection()
}

@Composable
fun PokemonDetailsDataSection(
    pokemon: Pokemon
) {
    val scrollState = rememberScrollState()
    val weightInKg = remember {
        round(pokemon.weight * 100f) / 1000f
    }
    val heightInMeters = remember {
        round(pokemon.height * 100f) / 1000f
    }

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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            ) {
                pokemon.types.forEach { type ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                            .clip(CircleShape)
                            .background(parseTypeToColor(type))
                            .height(35.dp)
                    ) {
                        Text(
                            text = type.type.name.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.ROOT
                                ) else it.toString()
                            },
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_weight),
                        contentDescription = "Weight",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$weightInKg kg",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(
                    modifier = Modifier
                        .size(1.dp, 80.dp)
                        .background(Color.LightGray)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_height),
                        contentDescription = "Height",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$heightInMeters m",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

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
    }
}

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

@Composable
fun PokemonDetailsErrorSection(
    errorMessage: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = errorMessage)
    }
}

@Composable
fun PokemonDetailsLoadingSection() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
