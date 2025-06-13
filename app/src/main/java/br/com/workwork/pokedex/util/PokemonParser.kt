package br.com.workwork.pokedex.util


import androidx.compose.ui.graphics.Color
import br.com.workwork.pokedex.features.list.data.remote.responses.PokemonInfo

val LightBlue = Color(0xFFBAC7FF)
val lightGrey = Color(0xFFAAAAAA)

val HPColor = Color(0xFFF5FF00)
val AtkColor = Color(1f, 0f, 0f, 0.66f)
val DefColor = Color(0f, 0f, 1f, 0.44f)
val SpAtkColor = Color(0.671f, 0f, 1f, 0.57f)
val SpDefColor = Color(1f, 0f, 0.8f, 0.7f)
val SpdColor = Color(0f, 1f, 0.063f, 0.55f)

val TypeNormal = Color(0xFFA8A77A)
val TypeFire = Color(0xFFEE8130)
val TypeWater = Color(0xFF6390F0)
val TypeElectric = Color(0xFFF7D02C)
val TypeGrass = Color(0xFF7AC74C)
val TypeIce = Color(0xFF96D9D6)
val TypeFighting = Color(0xFFC22E28)
val TypePoison = Color(0xFFA33EA1)
val TypeGround = Color(0xFFE2BF65)
val TypeFlying = Color(0xFFA98FF3)
val TypePsychic = Color(0xFFF95587)
val TypeBug = Color(0xFFA6B91A)
val TypeRock = Color(0xFFB6A136)
val TypeGhost = Color(0xFF735797)
val TypeDragon = Color(0xFF6F35FC)
val TypeDark = Color(0xFF705746)
val TypeSteel = Color(0xFFB7B7CE)
val TypeFairy = Color(0xFFD685AD)

fun parseTypeToColor(type: PokemonInfo.Type): Color {
    return when(type.type.name.lowercase()) {
        "normal" -> TypeNormal
        "fire" -> TypeFire
        "water" -> TypeWater
        "electric" -> TypeElectric
        "grass" -> TypeGrass
        "ice" -> TypeIce
        "fighting" -> TypeFighting
        "poison" -> TypePoison
        "ground" -> TypeGround
        "flying" -> TypeFlying
        "psychic" -> TypePsychic
        "bug" -> TypeBug
        "rock" -> TypeRock
        "ghost" -> TypeGhost
        "dragon" -> TypeDragon
        "dark" -> TypeDark
        "steel" -> TypeSteel
        "fairy" -> TypeFairy
        else -> Color.Black
    }
}

fun parseStatToColor(stat: PokemonInfo.Stat): Color {
    return when(stat.stat.name.lowercase()) {
        "hp" -> HPColor
        "attack" -> AtkColor
        "defense" -> DefColor
        "special-attack" -> SpAtkColor
        "special-defense" -> SpDefColor
        "speed" -> SpdColor
        else -> Color.White
    }
}

fun parseStatToAbbr(stat: PokemonInfo.Stat): String {
    return when(stat.stat.name.lowercase()) {
        "hp" -> "HP"
        "attack" -> "Atk"
        "defense" -> "Def"
        "special-attack" -> "SpAtk"
        "special-defense" -> "SpDef"
        "speed" -> "Spd"
        else -> ""
    }
}