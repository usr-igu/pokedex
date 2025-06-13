package br.com.workwork.pokedex.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import br.com.workwork.pokedex.features.detail.PokemonDetailScreen
import br.com.workwork.pokedex.features.list.PokemonListScreen
import io.ktor.http.parametersOf
import kotlinx.serialization.Serializable
import org.koin.core.parameter.parametersOf

@Serializable
data object PokemonListNav : NavKey

@Serializable
data class PokemonDetailNav(
    val pokemonNumber: Int,
    val color: ULong,
) : NavKey

@Composable
fun NavigationRoot(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(PokemonListNav)
    NavDisplay(
        modifier = modifier, backStack = backStack, entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
        ), entryProvider = { key ->
            when (key) {
                is PokemonListNav -> NavEntry(key) {
                    PokemonListScreen(
                        onEntryClick = { pokemonNumber, color ->
                            backStack.add(PokemonDetailNav(pokemonNumber, color.value))
                        }
                    )
                }

                is PokemonDetailNav -> NavEntry(key) { PokemonDetailScreen(
                    pokemonNumber = key.pokemonNumber,
                    color = key.color,
                ) }
                else -> error("Unknown key: $key")
            }
        })
}