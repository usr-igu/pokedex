package br.com.workwork.pokedex.features.list.data.remote.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokedexList(
    @SerialName("count")
    val count: Int,
    @SerialName("next")
    val next: String?,
    @SerialName("previous")
    val previous: String?,
    @SerialName("results")
    val results: List<PokedexListResult>
) {
    @Serializable
    data class PokedexListResult(
        @SerialName("name")
        val name: String,
        @SerialName("url")
        val url: String
    )
}