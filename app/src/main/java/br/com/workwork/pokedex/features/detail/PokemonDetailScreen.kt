package br.com.workwork.pokedex.features.detail

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.workwork.pokedex.R
import br.com.workwork.pokedex.features.list.data.remote.responses.PokemonInfo
import br.com.workwork.pokedex.ui.theme.PokeBlue
import br.com.workwork.pokedex.ui.theme.PokeYellow
import br.com.workwork.pokedex.util.Resource
import br.com.workwork.pokedex.util.parseStatToAbbr
import br.com.workwork.pokedex.util.parseStatToColor
import br.com.workwork.pokedex.util.parseTypeToColor
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.round
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PokemonDetailScreen(
    pokemonNumber: Int,
    color: ULong,
    modifier: Modifier = Modifier,
    topPadding: Dp = 20.dp,
    pokemonImageSize: Dp = 200.dp,
    viewModel: PokemonDetailViewModel = koinViewModel()
) {
    val pokemonDetail = produceState(
        initialValue = Resource.Loading(),
    ) {
        value = viewModel.fetchPokemon(pokemonNumber)
    }.value

    val baseColor = Color(color)

    if (pokemonDetail is Resource.Loading<*>) {
        Box(
            modifier = modifier
                .background(baseColor)
                .fillMaxSize()
        ) {

            return ContainedLoadingIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(100.dp),
                indicatorColor = PokeYellow,
                containerColor = PokeBlue,
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                baseColor
            )
            .padding(bottom = 16.dp),
    ) {

        PokemonDetailStateWrapper(
            pokemonInfo = pokemonDetail,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + pokemonImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
        )
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize(),
        ) {
            pokemonDetail.data?.sprites?.let {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).crossfade(true)
                        .data(pokemonDetail.data.sprites.frontDefault).build(),
                    contentDescription = pokemonDetail.data.name,
                    filterQuality = FilterQuality.None,
                    modifier = Modifier
                        .size(pokemonImageSize)
                        .offset(y = topPadding)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PokemonDetailStateWrapper(
    modifier: Modifier = Modifier,
    pokemonInfo: Resource<PokemonInfo>,
) {
    when (pokemonInfo) {
        is Resource.Error<*> -> {
            Text(
                text = pokemonInfo.message!!,
                color = MaterialTheme.colorScheme.error,
                modifier = modifier
            )
        }

        is Resource.Success<*> -> {
            pokemonInfo.data?.let {
                PokemonDetailSection(
                    pokemonInfo = it, modifier = modifier.offset(y = (-20).dp)
                )
            }
        }

        else -> {}
    }
}


@Composable
fun PokemonDetailSection(
    modifier: Modifier = Modifier, pokemonInfo: PokemonInfo
) {
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 120.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "#${pokemonInfo.id} ${pokemonInfo.name.replaceFirstChar { it -> it.uppercase() }}",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onSurface,
        )
        PokemonTypeSection(types = pokemonInfo.types)
        PokemonDetailDataSection(
            pokemonWeight = pokemonInfo.weight,
            pokemonHeight = pokemonInfo.height,
        )
        PokemonBaseStat(pokemonInfo = pokemonInfo)
    }
}

@Composable
fun PokemonTypeSection(
    types: List<PokemonInfo.Type>
) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)
    ) {
        for (type in types) {
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
                    text = type.type.name.replaceFirstChar { it -> it.uppercase() },
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun PokemonDetailDataSection(
    pokemonWeight: Int,
    pokemonHeight: Int,
    sectionHeight: Dp = 80.dp,
) {
    val pokemonWeightInKg = remember {
        round(pokemonWeight * 100f) / 1000f
    }
    val pokemonHeightInMeters = remember {
        round(pokemonHeight * 100f) / 1000f
    }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        PokemonDetailDataItem(
            dataValue = pokemonWeightInKg,
            dataUnit = "kg",
            dataIcon = painterResource(id = R.drawable.ic_weight),
            modifier = Modifier.weight(1f)
        )
        Spacer(
            modifier = Modifier
                .size(1.dp, sectionHeight)
                .background(Color.LightGray)
        )
        PokemonDetailDataItem(
            dataValue = pokemonHeightInMeters,
            dataUnit = "m",
            dataIcon = painterResource(id = R.drawable.ic_height),
            modifier = Modifier.weight(1f)
        )
    }

}

@Composable
fun PokemonDetailDataItem(
    dataValue: Float, dataUnit: String, dataIcon: Painter, modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(
            painter = dataIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$dataValue$dataUnit", color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun PokemonStat(
    statName: String,
    statValue: Int,
    statMaxValue: Int,
    statColor: Color,
    animDuration: Duration = 1.seconds,
    animDelay: Duration = 0.seconds,
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }

    val currPercent = animateFloatAsState(
        targetValue = if (animationPlayed) {
            statValue / statMaxValue.toFloat()
        } else 0f, animationSpec = tween(
            animDuration.inWholeMilliseconds.toInt(), animDelay.inWholeMilliseconds.toInt()
        )
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true;
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp)
            .clip(CircleShape)
            .background(
                if (isSystemInDarkTheme()) {
                    Color(0xFF505050)
                } else {
                    Color.LightGray
                }
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(currPercent.value)
                .clip(CircleShape)
                .background(statColor)
        ) { }
        Text(
            text = statName,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(horizontal = 8.dp)
        )
        Text(
            text = (currPercent.value * statMaxValue).toInt().toString(),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(horizontal = 8.dp)
        )
    }

}

@Composable
fun PokemonBaseStat(
    pokemonInfo: PokemonInfo,
    animDelayPerItem: Duration = 100.milliseconds,
) {
    val maxBaseStat = remember {
        pokemonInfo.stats.maxOf { it.baseStat }
    }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Base Stats:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        for (i in pokemonInfo.stats.indices) {
            val stat = pokemonInfo.stats[i]
            PokemonStat(
                statName = parseStatToAbbr(stat),
                statValue = stat.baseStat,
                statMaxValue = maxBaseStat,
                statColor = parseStatToColor(stat),
                animDelay = animDelayPerItem.times(i),
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}