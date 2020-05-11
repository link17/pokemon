package com.kakao.mobility.pokemondictionary.repository

import com.kakao.mobility.pokemondictionary.api.MockdataApiService
import com.kakao.mobility.pokemondictionary.api.PokemonApiService
import com.kakao.mobility.pokemondictionary.data.LocationData
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import java.util.concurrent.TimeUnit


class RemoteDataSource : PokemonDataSource {

    private val TIMEOUT = 15L

    private val mockApiService =
        createRetrofit("https://demo0928971.mockable.io/", MockdataApiService::class.java)
    private val pokemonApiService =
        createRetrofit("https://pokeapi.co", PokemonApiService::class.java)

    private fun <T> createRetrofit(baseUrl: String, serviceClass: Class<T>) =
        Retrofit.Builder().baseUrl(baseUrl).client(
            OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(
                    TIMEOUT,
                    TimeUnit.SECONDS
                ).addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        ).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(
            GsonConverterFactory.create()
        ).build().create(serviceClass)

    override fun getPokemonLocations() =
        mockApiService.getPokemonLocations().map { it.pokemons }.map { it ->

            mutableMapOf<Int, ArrayList<LocationData>>().apply {
                it.forEach { response ->
                    takeIf { containsKey(response.id) }?.run {
                        get(response.id)?.add(LocationData(response.lat, response.lng))
                    } ?: run {
                        put(
                            response.id,
                            arrayListOf<LocationData>().apply {
                                add(
                                    LocationData(
                                        response.lat,
                                        response.lng
                                    )
                                )
                            })
                    }

                }

            }
        }

    override fun getPokemon(id: Int) = pokemonApiService.getPokemon(id).subscribeOn(Schedulers.io())


    fun getPokemonNames() = mockApiService.getPokemonNames()
}