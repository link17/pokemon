package com.kakao.mobility.pokemondictionary.api

import com.kakao.mobility.pokemondictionary.data.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApiService {
    @GET("/api/v2/pokemon/{id}")
    fun getPokemon(@Path("id") id:Int): Single<PokemonDetailData>
}

interface MockdataApiService{
    @GET("pokemon_name")
    fun getPokemonNames() : Single<NameResponse>

    @GET("pokemon_locations")
    fun getPokemonLocations():Single<LocationResponse>
}