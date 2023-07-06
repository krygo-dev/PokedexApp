package com.krygodev.pokedexapp.presentation.pokemon_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krygodev.pokedexapp.R

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
                BasicTextField(
                    value = state.searchQuery,
                    onValueChange = { onEvent(PokemonListEvent.SetSearchQuery(it)) },
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

                    Text(text = pokemonListEntry.name)
                }
            }

            Box(
                contentAlignment = Center,
                modifier = Modifier.fillMaxSize()
            ) {
                if(state.isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
                if(!state.loadError.isNullOrEmpty()) {
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