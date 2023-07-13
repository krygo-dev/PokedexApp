package com.krygodev.pokedexapp.presentation.pokemon_details

import androidx.compose.foundation.background
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.krygodev.pokedexapp.R
import com.krygodev.pokedexapp.util.parseTypeToColor
import java.util.Locale
import kotlin.math.round

@Composable
fun PokemonDetailsScreen(
    state: PokemonDetailsState,
    navController: NavController
) {
    val scrollState = rememberScrollState()
    val weightInKg = remember {
//        round(state.pokemon!!.weight * 100f) / 1000f
        round(state.pokemon?.weight?.times(100f)?.div(1000f) ?: 0f)
    }
    val heightInMeters = remember {
//        round(state.pokemon!!.height * 100f) / 1000f
        round(state.pokemon?.height?.times(100f)?.div(1000f) ?: 0f)
    }

    Surface(
        color = state.pokemonDominantColor!!,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    ) {
        PokemonDetailsTopSection(onBackClick = { navController.popBackStack() })

        AsyncImage(
            model = state.pokemon?.frontSprite,
            contentDescription = "Pokemon front sprite",
            modifier = Modifier.size(200.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Text(
                text = "#${state.pokemon?.id} ${state.pokemon?.name}",
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(16.dp)
            ) {
                state.pokemon?.types?.forEach { type ->
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

                Spacer(modifier = Modifier
                    .size(1.dp, 80.dp)
                    .background(Color.LightGray))

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
        }
        
        if (!state.loadError.isNullOrEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = state.loadError)
            }
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun PokemonDetailsTopSection(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f),
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