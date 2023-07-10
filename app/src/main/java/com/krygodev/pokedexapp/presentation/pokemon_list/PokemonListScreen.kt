package com.krygodev.pokedexapp.presentation.pokemon_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.krygodev.pokedexapp.R
import com.krygodev.pokedexapp.ui.theme.RobotoCondensed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    state: PokemonListState,
    onEvent: (PokemonListEvent) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.ic_pokemon_logo),
                contentDescription = "Pokemon",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TextField(
                    value = state.searchQuery,
                    onValueChange = { onEvent(PokemonListEvent.SetSearchQuery(it)) },
                    placeholder = { Text(text = "Search..") },
                    maxLines = 1,
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(5.dp, CircleShape)
                        .background(Color.White, CircleShape)
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                )
            }

            if (state.pokemonList.isEmpty()) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    Text(text = "No results to show.")
                    Text(text = "Try to load more Pokemons before searching.")
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(30.dp)
            ) {
                items(state.pokemonList) { pokemonListEntry ->
                    if (pokemonListEntry == state.pokemonList.last() &&
                        !state.endReached &&
                        !state.isLoading &&
                        !state.isSearching
                    ) {
                        LaunchedEffect(key1 = true) {
                            onEvent(PokemonListEvent.LoadPokemonPaginated)
                        }
                    }

                    Box(
                        contentAlignment = Center,
                        modifier = Modifier
                            .padding(8.dp)
                            .shadow(5.dp, RoundedCornerShape(10.dp))
                            .clip(RoundedCornerShape(10.dp))
                            .aspectRatio(1f)
                            .background(Color.LightGray)
                            .padding(4.dp)
                            .clickable {
                                println("DEBUG: ${pokemonListEntry.name}")
                            }
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = CenterHorizontally
                        ) {
                            SubcomposeAsyncImage(
                                model = pokemonListEntry.imageUrl,
                                contentDescription = pokemonListEntry.name,
                                loading = {
                                    CircularProgressIndicator()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(150.dp),
                            )
                            Text(
                                text = pokemonListEntry.name,
                                fontFamily = RobotoCondensed,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            Box(
                contentAlignment = Center,
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
                if (!state.loadError.isNullOrEmpty()) {
                    Column {
                        Text(state.loadError, color = Color.Red, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { onEvent(PokemonListEvent.LoadPokemonPaginated) },
                            modifier = Modifier.align(CenterHorizontally)
                        ) {
                            Text(text = "Retry")
                        }
                    }
                }
            }
        }
    }
}