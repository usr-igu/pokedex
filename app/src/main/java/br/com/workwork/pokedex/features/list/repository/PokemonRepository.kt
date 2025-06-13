package br.com.workwork.pokedex.features.list.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import br.com.workwork.pokedex.features.list.data.local.PokemonDatabase
import br.com.workwork.pokedex.features.list.data.local.PokemonEntity
import br.com.workwork.pokedex.features.list.data.remote.PokeApi
import br.com.workwork.pokedex.features.list.data.remote.PokemonRemoteMediator
import br.com.workwork.pokedex.features.list.data.remote.responses.PokemonInfo
import br.com.workwork.pokedex.util.Resource
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalPagingApi::class)
class PokemonRepository(
    private val pokeApi: PokeApi,
    private val pokemonDb: PokemonDatabase,
) {
    private val pokemonDao = pokemonDb.pokemonDao()

    fun fetchPokemonPagingData(search: String = ""): Flow<PagingData<PokemonEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = PokeApi.POKEMON_PAGE_SIZE,
            ), remoteMediator = PokemonRemoteMediator(
                pokeApi = pokeApi, pokemonDb = pokemonDb
            ), pagingSourceFactory = {
                pokemonDao.pagingSource(search)
            }).flow
    }

    suspend fun fetchPokemon(number: Int): Resource<PokemonInfo> {
        val response = try {
            pokeApi.fetchPokemon(number)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occured")
        }
        return Resource.Success(response)
    }

    suspend fun updateDominantColor(number: Int, dominantColor: Long) {
        pokemonDao.updateDominantColor(number, dominantColor)
    }
}
