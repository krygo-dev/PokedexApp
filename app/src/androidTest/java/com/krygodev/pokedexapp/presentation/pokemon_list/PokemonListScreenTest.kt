package com.krygodev.pokedexapp.presentation.pokemon_list

import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextInput
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.krygodev.pokedexapp.MainActivity
import com.krygodev.pokedexapp.di.AppModule
import com.krygodev.pokedexapp.ui.theme.PokedexAppTheme
import com.krygodev.pokedexapp.util.Constants.POKEMON_LIST_SCREEN
import com.krygodev.pokedexapp.util.Constants.POKEMON_LOGO
import com.krygodev.pokedexapp.util.TestTags.FAB
import com.krygodev.pokedexapp.util.TestTags.LAZY_GRID
import com.krygodev.pokedexapp.util.TestTags.SEARCH_ERROR
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
class PokemonListScreenTest {

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
                    startDestination = POKEMON_LIST_SCREEN
                ) {
                    composable(POKEMON_LIST_SCREEN) {
                        val viewModel = hiltViewModel<PokemonListViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        PokemonListScreen(
                            state = state,
                            onEvent = viewModel::onEvent,
                            navController = navController
                        )
                    }
                }
            }
        }
    }

    @Test
    fun checkPokemonLogoVisible_logoVisible() {
        composeRule.onNodeWithContentDescription(POKEMON_LOGO).assertIsDisplayed()
    }


    @Test
    fun loadPokemonPaginated_pokemonListTilesVisible() {
        val testText = "Bulbasaur"
        composeRule.waitUntilExactlyOneExists(hasText(testText))
        composeRule.waitUntilExactlyOneExists(hasContentDescription(testText))
    }

    @Test
    fun loadPokemonPaginated_loadNewPageUntilEndReached() {
        composeRule.waitUntilExactlyOneExists(hasTestTag("1"))
        composeRule.onNodeWithTag(LAZY_GRID).performScrollToIndex(19)
        composeRule.waitUntilExactlyOneExists(hasTestTag("21"))
        composeRule.onNodeWithTag(LAZY_GRID).performScrollToIndex(39)
        composeRule.waitUntilExactlyOneExists(hasTestTag("41"))
        composeRule.onNodeWithTag(LAZY_GRID).performScrollToIndex(49)
        composeRule.waitUntilExactlyOneExists(hasTestTag("50"))
        composeRule.onNodeWithTag("51").assertDoesNotExist()
    }

    @Test
    fun scrollBackToTop_fabVisible() {
        composeRule.onNodeWithTag(FAB).assertDoesNotExist()
        composeRule.onNodeWithTag(LAZY_GRID).performScrollToIndex(10)
        composeRule.onNodeWithTag(FAB).assertIsDisplayed()
    }

    @Test
    fun scrollBackToTop_fabOnClickWorksProperly() {
        composeRule.onNodeWithTag(FAB).assertDoesNotExist()
        composeRule.onNodeWithTag(LAZY_GRID).performScrollToIndex(10)
        composeRule.onNodeWithTag(FAB).assertIsDisplayed().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag(FAB).assertDoesNotExist()
        composeRule.onNodeWithText("Bulbasaur").assertIsDisplayed()
    }

    @Test
    fun searchForPokemon_resultsDisplayed() {
        composeRule.onNodeWithTag(SEARCH_FIELD).performTextInput("saur")
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Bulbasaur").assertIsDisplayed()
        composeRule.onNodeWithText("Ivysaur").assertIsDisplayed()
        composeRule.onNodeWithText("Venusaur").assertIsDisplayed()
    }

    @Test
    fun searchForPokemon_pokemonNotFoundErrorDisplayed() {
        composeRule.onNodeWithTag(SEARCH_FIELD).performTextInput("Soup")
        composeRule.waitUntilExactlyOneExists(hasTestTag(SEARCH_ERROR))
    }

    @Test
    fun pokemonListItem_isClickable() {
        composeRule.onNodeWithTag("1").assertIsDisplayed().assertHasClickAction()
    }
}