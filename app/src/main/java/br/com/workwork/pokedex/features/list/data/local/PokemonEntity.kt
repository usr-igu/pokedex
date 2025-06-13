package br.com.workwork.pokedex.features.list.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemons")
data class PokemonEntity(
    @PrimaryKey val number: Int,
    val pokemonName: String,
    val pokemonNameNorm: String,
    val imageUrl: String,
    val dominantColor: Long?
)