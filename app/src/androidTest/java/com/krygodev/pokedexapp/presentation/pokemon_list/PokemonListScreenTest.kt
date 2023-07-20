package com.krygodev.pokedexapp.presentation.pokemon_list

import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
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
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun loadPokemonPaginated_pokemonListTilesVisible() {
        val testText = "Bulbasaur"
        composeRule.waitUntilExactlyOneExists(hasText(testText))
        composeRule.waitUntilExactlyOneExists(hasContentDescription(testText))
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
}