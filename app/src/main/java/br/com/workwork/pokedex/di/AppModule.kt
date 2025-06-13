package br.com.workwork.pokedex.di

import androidx.room.Room
import br.com.workwork.pokedex.features.detail.PokemonDetailViewModel
import br.com.workwork.pokedex.features.list.PokemonListViewModel
import br.com.workwork.pokedex.features.list.data.local.PokemonDatabase
import br.com.workwork.pokedex.features.list.data.remote.PokeApi
import br.com.workwork.pokedex.features.list.repository.PokemonRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module  {
    singleOf(::PokeApi)

    // Database
    single {
        Room.databaseBuilder(
            androidApplication(),
            PokemonDatabase::class.java,
            "pokedex.db"
        ).build()
    }

    // Repository
    singleOf(::PokemonRepository)

    // ViewModel
    viewModelOf(::PokemonDetailViewModel)
    viewModelOf(::PokemonListViewModel)
}

val networkModule = module {
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
    }
}