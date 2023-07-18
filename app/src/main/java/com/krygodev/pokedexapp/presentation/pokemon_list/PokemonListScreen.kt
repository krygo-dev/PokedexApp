package com.krygodev.pokedexapp.presentation.pokemon_list

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.krygodev.pokedexapp.R
import com.krygodev.pokedexapp.presentation.pokemon_list.composables.PokemonListErrorSection
import com.krygodev.pokedexapp.presentation.pokemon_list.composables.PokemonListItem
import com.krygodev.pokedexapp.presentation.pokemon_list.composables.PokemonListSearchError
import com.krygodev.pokedexapp.ui.theme.lightGrey
import com.krygodev.pokedexapp.util.Constants.POKEMON_DETAILS_SCREEN
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    state: PokemonListState,
    onEvent: (PokemonListEvent) -> Unit,
    navController: NavController
) {
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            if (gridState.canScrollBackward) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            gridState.animateScrollToItem(0)
                        }
                    },
                    contentColor = Color.White,
                    containerColor = lightGrey,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Scroll to top",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.ic_pokemon_logo),
                contentDescription = "Pokemon",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { onEvent(PokemonListEvent.SetSearchQuery(it)) },
                placeholder = { Text(text = "Search..") },
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(color = Color.Black),
                shape = RoundedCornerShape(10.dp),
                trailingIcon = {
                    if (state.searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { onEvent(PokemonListEvent.SetSearchQuery("")) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear text field"
                            )
                        }
                    }
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(vertical = 16.dp),
                modifier = Modifier.fillMaxWidth(),
                state = gridState
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

                    PokemonListItem(
                        pokemonEntry = pokemonListEntry,
                        onClick = {
                            navController.navigate(
                                "$POKEMON_DETAILS_SCREEN/" +
                                        "${pokemonListEntry.dominantColor.toArgb()}/" +
                                        pokemonListEntry.name
                            )
                        },
                        onLoadImageSuccess = {
                            onEvent(
                                PokemonListEvent.CalculateDominantColor(
                                    pokemonListEntry = pokemonListEntry,
                                    drawable = it
                                )
                            )
                        }
                    )
                }
            }

            if (state.pokemonList.isEmpty() && !state.isLoading) PokemonListSearchError()

            Box(
                contentAlignment = Center,
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
                if (!state.loadError.isNullOrEmpty()) {
                    PokemonListErrorSection(
                        errorText = state.loadError,
                        onClick = { onEvent(PokemonListEvent.LoadPokemonPaginated) }
                    )
                }
            }
        }
    }
}