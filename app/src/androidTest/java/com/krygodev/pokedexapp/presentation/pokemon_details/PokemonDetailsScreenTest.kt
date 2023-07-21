package com.krygodev.pokedexapp.presentation.pokemon_details

import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.krygodev.pokedexapp.MainActivity
import com.krygodev.pokedexapp.di.AppModule
import com.krygodev.pokedexapp.ui.theme.PokedexAppTheme
import com.krygodev.pokedexapp.util.Constants
import com.krygodev.pokedexapp.util.Constants.HEIGHT
import com.krygodev.pokedexapp.util.Constants.POKEMON_FRONT_SPRITE
import com.krygodev.pokedexapp.util.Constants.WEIGHT
import com.krygodev.pokedexapp.util.TestTags.BACK_ARROW
import com.krygodev.pokedexapp.util.TestTags.POKEMON_STAT
import com.krygodev.pokedexapp.util.TestTags.POKEMON_TYPE
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
@HiltAndroidTest
@UninstallModules(AppModule::class)
class PokemonDetailsScreenTest {

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
                    startDestination = "${Constants.POKEMON_DETAILS_SCREEN}/{${Constants.POKEMON_COLOR}}/{${Constants.POKEMON_NAME}}"
                ) {
                    composable(
                        route = "${Constants.POKEMON_DETAILS_SCREEN}/{${Constants.POKEMON_COLOR}}/{${Constants.POKEMON_NAME}}",
                        arguments = listOf(navArgument(Constants.POKEMON_COLOR) { type = NavType.IntType })
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
    fun backButton_isVisibleAndClickable() {
        composeRule.onNodeWithTag(BACK_ARROW).assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun pokemonData_isVisibleAndCorrect() {
        composeRule.apply {
            waitUntilExactlyOneExists(hasContentDescription(POKEMON_FRONT_SPRITE))
            waitUntilExactlyOneExists(hasText("#25 Pikachu"))
            waitUntil {
                composeRule.onAllNodesWithTag(POKEMON_TYPE).fetchSemanticsNodes().size == 1 &&
                composeRule.onAllNodesWithTag(POKEMON_STAT).fetchSemanticsNodes().size == 6
            }
            waitUntilExactlyOneExists(hasContentDescription(WEIGHT))
            waitUntilExactlyOneExists(hasContentDescription(HEIGHT))

        }
    }
}