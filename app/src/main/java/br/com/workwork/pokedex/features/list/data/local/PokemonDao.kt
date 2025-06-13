package br.com.workwork.pokedex.features.list.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemons WHERE pokemonNameNorm LIKE '%' || :search || '%' ORDER BY number ASC")
    fun pagingSource(search: String): PagingSource<Int, PokemonEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemons(pokemons: List<PokemonEntity>)

    @Query("DELETE FROM pokemons")
    suspend fun deletePokemons()

    @Query("SELECT COUNT(number) FROM pokemons")
    suspend fun getPokemonCount(): Int

    @Query("UPDATE pokemons SET dominantColor = :dominantColor WHERE number = :number")
    suspend fun updateDominantColor(number: Int, dominantColor: Long)
}
