package br.com.workwork.pokedex.features.list.data.remote.responses


import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable()
@JsonIgnoreUnknownKeys
data class PokemonInfo(
    @SerialName("forms")
    val forms: List<Form>,
    @SerialName("height")
    val height: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("order")
    val order: Int,
    @SerialName("sprites")
    val sprites: Sprites,
    @SerialName("stats")
    val stats: List<Stat>,
    @SerialName("types")
    val types: List<Type>,
    @SerialName("weight")
    val weight: Int
) {
    @Serializable
    data class Form(
        @SerialName("name")
        val name: String,
        @SerialName("url")
        val url: String
    )

    @Serializable
    data class Sprites(
        @SerialName("front_default")
        val frontDefault: String
    )

    @Serializable
    data class Stat(
        @SerialName("base_stat")
        val baseStat: Int,
        @SerialName("stat")
        val stat: Stat
    ) {
        @Serializable
        data class Stat(
            @SerialName("name")
            val name: String
        )
    }

    @Serializable
    data class Type(
        @SerialName("slot")
        val slot: Int,
        @SerialName("type")
        val type: Type
    ) {
        @Serializable
        data class Type(
            @SerialName("name")
            val name: String
        )
    }
}