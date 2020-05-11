package com.kakao.mobility.pokemondictionary.repository

import com.kakao.mobility.pokemondictionary.data.LocationData
import com.kakao.mobility.pokemondictionary.data.PokemonData
import com.kakao.mobility.pokemondictionary.data.PokemonDetailData
import com.kakao.mobility.pokemondictionary.data.PokemonLocationResponse
import io.reactivex.Single

interface PokemonDataSource {

    fun getPokemonLocations(): Single<MutableMap<Int, ArrayList<LocationData>>>
    fun getPokemon(id:Int):Single<PokemonDetailData?>
}