package br.com.workwork.pokedex.features.detail

import androidx.lifecycle.ViewModel
import br.com.workwork.pokedex.features.list.repository.PokemonRepository

class PokemonDetailViewModel(private val repository: PokemonRepository) : ViewModel() {
    suspend fun fetchPokemon(number: Int) = repository.fetchPokemon(number)
}