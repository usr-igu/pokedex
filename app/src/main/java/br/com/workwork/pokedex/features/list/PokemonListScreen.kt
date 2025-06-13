package br.com.workwork.pokedex.features.list

import android.graphics.Bitmap
import android.graphics.drawable.Icon
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.fromColorLong
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColor
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import br.com.workwork.pokedex.R
import br.com.workwork.pokedex.features.list.data.local.PokemonEntity
import br.com.workwork.pokedex.ui.theme.PokeBlue
import br.com.workwork.pokedex.ui.theme.PokeYellow
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.toBitmap
import org.koin.androidx.compose.koinViewModel

@Composable
fun PokemonListScreen(
    modifier: Modifier = Modifier,
    onEntryClick: (Int, Color) -> Unit = { _, _ -> },
    viewModel: PokemonListViewModel = koinViewModel()
) {
    val query by viewModel.query.collectAsStateWithLifecycle(initialValue = "")
    val pokemons = viewModel.pokemons.collectAsLazyPagingItems()

    Surface(
        color = MaterialTheme.colorScheme.background, modifier = modifier.fillMaxSize()
    ) {
        Column {
            PokedexHeaderSection(
                query = query,
                onSearch = viewModel::onSearch,
            )
            PokedexGrid(
                onEntryClick = onEntryClick,
                pokemons = pokemons,
                onDominantColor = viewModel::onDominantColor
            )
        }
    }
}

@Composable
fun PokedexHeaderSection(
    modifier: Modifier = Modifier,
    query: String,
    onSearch: (String) -> Unit,
) {
    Column(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.ic_international_pok_mon_logo),
            contentDescription = "Pokémon Logo",
            modifier = Modifier
                .fillMaxWidth()
                .align(CenterHorizontally)
        )
        SearchBar(
            hint = "Charmander",
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            query = query,
            onSearch = onSearch
        )
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    query: String,
    onSearch: (String) -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }

    val isHintDisplayed = query.isBlank() && !isFocused

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainer, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp), // Adjusted vertical padding
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search Icon",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(8.dp)) // Add space between icon and text field
            BasicTextField(
                value = query,
                onValueChange = {
                    onSearch(it)
                },
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged {
                        isFocused = it.isFocused
                    },
                decorationBox = { innerTextField ->
                    Box {
                        if (isHintDisplayed) {
                            BasicText(
                                text = hint,
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                        alpha = 0.6f
                                    )
                                ),
                            )
                        }
                        innerTextField()
                    }
                })
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PokedexGrid(
    pokemons: LazyPagingItems<PokemonEntity>,
    modifier: Modifier = Modifier,
    onEntryClick: (Int, Color) -> Unit = { _, _ -> },
    onDominantColor: (Int, Bitmap, (Color) -> Unit) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp), modifier = modifier.padding(horizontal = 8.dp)
    ) {
        items(
            count = pokemons.itemCount,
            key = pokemons.itemKey { p -> p.number },
            contentType = pokemons.itemContentType { "PokedexEntry" },
        ) { index ->
            val entry = pokemons[index]
            if (entry != null) {
                PokedexEntry(
                    modifier = Modifier
                        .animateItem()
                        .padding(8.dp),
                    entry = entry,
                    onEntryClick = onEntryClick,
                    onDominantColor = onDominantColor
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PokedexEntry(
    entry: PokemonEntity,
    modifier: Modifier = Modifier,
    onEntryClick: (Int, Color) -> Unit = { _, _ -> },
    onDominantColor: (Int, Bitmap, (Color) -> Unit) -> Unit = { _, _, _ -> }
) {
    val defaultDominantColor: Color =
        entry.dominantColor?.let { Color.fromColorLong(it) } ?: MaterialTheme.colorScheme.surface

    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).data(entry.imageUrl).build(),
        filterQuality = FilterQuality.None,
    )

    val state by painter.state.collectAsStateWithLifecycle()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(dominantColor)
            .clickable {
                onEntryClick(entry.number, dominantColor)
            }) {
        when (state) {
            is AsyncImagePainter.State.Empty -> {}

            is AsyncImagePainter.State.Loading -> {
                ContainedLoadingIndicator(
                    indicatorColor = PokeYellow,
                    containerColor = PokeBlue,
                    modifier = Modifier
                        .size(120.dp)
                        .scale(0.5f)
                        .align(Alignment.Center)
                )
            }

            is AsyncImagePainter.State.Success -> {
                val success = state as AsyncImagePainter.State.Success
                if (entry.dominantColor == null) {
                    onDominantColor(entry.number, success.result.image.toBitmap()) {
                        dominantColor = it
                    }
                }
                Column {
                    Image(
                        painter = painter,
                        contentDescription = entry.pokemonName,
                        modifier = Modifier
                            .size(130.dp)
                            .align(CenterHorizontally)
                    )
                    Text(
                        text = entry.pokemonName,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                            )
                    )
                }
            }

            is AsyncImagePainter.State.Error -> {
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.who_is_pok),
                        contentDescription = entry.pokemonName,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    Text(
                        text = entry.pokemonName,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomEnd)
                            .padding(bottom = 20.dp)
                            .background(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                            )
                    )
                }
            }
        }
    }
}
