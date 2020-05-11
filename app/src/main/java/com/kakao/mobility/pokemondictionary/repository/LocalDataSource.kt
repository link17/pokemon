package com.kakao.mobility.pokemondictionary.repository

import androidx.annotation.Nullable
import com.kakao.mobility.pokemondictionary.data.LocationData
import com.kakao.mobility.pokemondictionary.data.PokemonDetailData
import io.reactivex.Single

class LocalDataSource : PokemonDataSource {

    // 위치정보 map (위치 정보가 여러개인 포켓몬들이 있다.)
    private var locationMap: MutableMap<Int, ArrayList<LocationData>>? = null

    var pokemonDataMap = mutableMapOf<Int, PokemonDetailData>()

    override fun getPokemonLocations(): Single<MutableMap<Int, ArrayList<LocationData>>> =
        Single.just(Optional(locationMap)).flatMap { Single.just(it.get()) }

    override fun getPokemon(id: Int): Single<PokemonDetailData?> =
        Single.just(Optional(pokemonDataMap[id])).flatMap { Single.just(it.get()) }

    fun saveLocations(locations: MutableMap<Int, ArrayList<LocationData>>): Single<MutableMap<Int, ArrayList<LocationData>>> {
        locationMap = locations
        return Single.just(locations)
    }
//        Single.just{ locationMap = locations}.flatMap { Single.just(locations) }

    fun putPokemon(pokemonDetailData: PokemonDetailData): Single<PokemonDetailData>? {
        pokemonDataMap[pokemonDetailData.id] = pokemonDetailData

        return Single.just(pokemonDetailData)
    }
}

class Optional<M>(@Nullable optional: M) {
    private val optional: M?
    val isEmpty: Boolean
        get() = optional == null

    fun get(): M {
        if (optional == null) {
            throw NoSuchElementException("No value present")
        }
        return optional
    }

    init {
        this.optional = optional
    }
}