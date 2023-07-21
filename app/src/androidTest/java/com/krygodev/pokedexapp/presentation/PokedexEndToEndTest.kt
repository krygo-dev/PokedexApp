package com.krygodev.pokedexapp.presentation

import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextInput
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.krygodev.pokedexapp.MainActivity
import com.krygodev.pokedexapp.di.AppModule
import com.krygodev.pokedexapp.presentation.pokemon_details.PokemonDetailsScreen
import com.krygodev.pokedexapp.presentation.pokemon_details.PokemonDetailsViewModel
import com.krygodev.pokedexapp.presentation.pokemon_list.PokemonListScreen
import com.krygodev.pokedexapp.presentation.pokemon_list.PokemonListViewModel
import com.krygodev.pokedexapp.ui.theme.PokedexAppTheme
import com.krygodev.pokedexapp.util.Constants
import com.krygodev.pokedexapp.util.Constants.POKEMON_LOGO
import com.krygodev.pokedexapp.util.TestTags.BACK_ARROW
import com.krygodev.pokedexapp.util.TestTags.FAB
import com.krygodev.pokedexapp.util.TestTags.LAZY_GRID
import com.krygodev.pokedexapp.util.TestTags.SEARCH_FIELD
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
@HiltAndroidTest
@UninstallModules(AppModule::class)
class PokedexEndToEndTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.activity.setContent {
            PokedexAppTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Constants.POKEMON_LIST_SCREEN
                ) {
                    composable(Constants.POKEMON_LIST_SCREEN) {
                        val viewModel = hiltViewModel<PokemonListViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        PokemonListScreen(
                            state = state,
                            onEvent = viewModel::onEvent,
                            navController = navController
                        )
                    }
                    composable(
                        route = "${Constants.POKEMON_DETAILS_SCREEN}/{${Constants.POKEMON_COLOR}}/{${Constants.POKEMON_NAME}}",
                        arguments = listOf(navArgument(Constants.POKEMON_COLOR) {
                            type = NavType.IntType
                        })
                    ) {
                        val viewModel = hiltViewModel<PokemonDetailsViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        PokemonDetailsScreen(
                            state = state,
                            navController = navController
                        )
                    }
                }
            }
        }
    }

    @Test
    fun scrollList_searchPokemon_openPokemonDetails_thenGoBack() {
        composeRule.apply {
            // Scroll list 3 times
            onNodeWithTag(LAZY_GRID).performScrollToIndex(19)
            onNodeWithTag(LAZY_GRID).performScrollToIndex(39)
            onNodeWithTag(LAZY_GRID).performScrollToIndex(49)

            // Click FAB to go back to top of the list
            onNodeWithTag(FAB).assertIsDisplayed().performClick()
            waitForIdle()

            // Type in 'Pikachu' in search field
            onNodeWithTag(SEARCH_FIELD).performTextInput("Pikachu")
            waitForIdle()

            // Check if Pikachu - tag 25 - list item is displayed and click on it
            waitUntilExactlyOneExists(hasTestTag("25"))
            onNodeWithTag("25").performClick()

            // Check if PokemonDetailsScreen is loaded
            onNodeWithText("#25 Pikachu").assertIsDisplayed()

            // Go back to PokemonListScreen
            onNodeWithTag(BACK_ARROW).performClick()

            // Check navigating back worked properly
            onNodeWithContentDescription(POKEMON_LOGO).assertIsDisplayed()
        }
    }
}