package br.com.workwork.pokedex.features.list.data.remote

import androidx.core.net.toUri
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import br.com.workwork.pokedex.features.list.data.local.PokemonDatabase
import br.com.workwork.pokedex.features.list.data.local.PokemonEntity

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator(
    private val pokeApi: PokeApi,
    private val pokemonDb: PokemonDatabase
) : RemoteMediator<Int, PokemonEntity>() {

    private val pokemonDao = pokemonDb.pokemonDao()

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonEntity>
    ): MediatorResult {
        try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    PokeApi.POKEMON_STARTING_PAGE_INDEX
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val count = pokemonDao.getPokemonCount()
                    if (count == 0) {
                        PokeApi.POKEMON_STARTING_PAGE_INDEX
                    } else {
                        count
                    }
                }
            }

            val response = pokeApi.fetchPokemons(
                limit = PokeApi.POKEMON_PAGE_SIZE,
                offset = loadKey
            )

            val results = response.results
            val endOfPaginationReached = response.next == null

            val entities = results.map { result ->
                val uri = result.url.toUri()
                val number = uri.pathSegments.lastOrNull()?.toInt() ?: 0
                PokemonEntity(
                    pokemonName = result.name.replaceFirstChar { it.uppercase() },
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png",
                    number = number,
                    pokemonNameNorm = result.name.lowercase(),
                    dominantColor = null
                )
            }

            pokemonDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    pokemonDao.deletePokemons()
                }
                pokemonDao.insertPokemons(entities)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}
