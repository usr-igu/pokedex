package br.com.workwork.pokedex.features.list

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toColorLong
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.palette.graphics.Palette
import br.com.workwork.pokedex.features.list.data.local.PokemonEntity
import br.com.workwork.pokedex.features.list.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext


@OptIn(ExperimentalCoroutinesApi::class)
class PokemonListViewModel(private val repository: PokemonRepository) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query: Flow<String> = _query.asStateFlow()

    @OptIn(FlowPreview::class)
    val pokemons: Flow<PagingData<PokemonEntity>> = _query.debounce(SEARCH_DEBOUNCE_MS).flatMapLatest { query ->
        repository.fetchPokemonPagingData(query.lowercase())
    }.cachedIn(viewModelScope)

    fun onSearch(query: String) {
        _query.value = query
    }

    fun onDominantColor(number: Int, bmp: Bitmap, onFinish: (Color) -> Unit) {
        val copy = bmp.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(copy).generate { palette ->
            val color = palette?.dominantSwatch?.rgb?.let { Color(it) } ?: Color.White
            onFinish(color)

            runCatching {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.updateDominantColor(number, color.toColorLong())
                }
            }
        }
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 300L
    }
}
