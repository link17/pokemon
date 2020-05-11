package com.kakao.mobility.pokemondictionary.repository

import com.kakao.mobility.pokemondictionary.data.LocationData
import com.kakao.mobility.pokemondictionary.data.PokemonDetailData
import com.kakao.mobility.pokemondictionary.data.PokemonLocationResponse
import com.kakao.mobility.pokemondictionary.data.PokemonNameResponse
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers

object PokemonRepository {

    private val remote = RemoteDataSource()
    private val local = LocalDataSource()

    fun getPokemon(id: Int): Flowable<PokemonDetailData?> =
        Single.concat(
            local.getPokemon(id).onErrorResumeNext { loadRemotePokemon(id) },
            loadRemotePokemon(id)
        )

    private fun loadRemotePokemon(id: Int) =
        remote.getPokemon(id).subscribeOn(Schedulers.io()).flatMap { local.putPokemon(it) }

    private fun loadRemoteLocations() = remote.getPokemonLocations().subscribeOn(Schedulers.io())
        .flatMap { local.saveLocations(it) }

    fun getPokemonLocations(): Flowable<MutableMap<Int, ArrayList<LocationData>>> =
        Single.concat(
            local.getPokemonLocations().onErrorResumeNext { loadRemoteLocations() },
            loadRemoteLocations()
        )

    fun getPokemonNames() = remote.getPokemonNames().subscribeOn(Schedulers.io())
}