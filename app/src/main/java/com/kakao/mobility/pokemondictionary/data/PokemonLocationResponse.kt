package com.kakao.mobility.pokemondictionary.data

data class LocationResponse(val pokemons:List<PokemonLocationResponse>)

data class PokemonLocationResponse(
    val id: Int, // id
    val lat: Double, // 위도
    val lng: Double // 경도
)