package br.com.workwork.pokedex.features.list.data.remote

import br.com.workwork.pokedex.features.list.data.remote.responses.PokedexList
import br.com.workwork.pokedex.features.list.data.remote.responses.PokemonInfo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class PokeApi(
    private val httpClient: HttpClient
) {
    suspend fun fetchPokemons(
        limit: Int,
        offset: Int
    ) : PokedexList {
        val response =  httpClient.get("https://pokeapi.co/api/v2/pokemon") {
            parameter("limit", limit)
            parameter("offset", offset)
        }

        return response.body()
    }

    suspend fun fetchPokemon(
        number: Int
    ) : PokemonInfo {
        val response =  httpClient.get("https://pokeapi.co/api/v2/pokemon/$number")
        return response.body()
    }

    companion object {
        const val POKEMON_STARTING_PAGE_INDEX = 0
        const val POKEMON_PAGE_SIZE = 100
    }
}