package com.kakao.mobility.pokemondictionary.data

data class NameResponse(val pokemons:List<PokemonNameResponse>)

data class PokemonNameResponse(val id:Int, val names:ArrayList<String>)