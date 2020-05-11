package com.kakao.mobility.pokemondictionary.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakao.mobility.pokemondictionary.data.LocationData
import com.kakao.mobility.pokemondictionary.data.PokemonData
import com.kakao.mobility.pokemondictionary.data.PokemonDetailData
import com.kakao.mobility.pokemondictionary.data.PokemonNameResponse
import com.kakao.mobility.pokemondictionary.repository.PokemonRepository
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class MainViewModel : ViewModel() {

    val map = mutableMapOf<Int, MutableList<PokemonData>>()
    val searchList: MutableLiveData<List<PokemonData>> = MutableLiveData()

    private val _action: MutableLiveData<PokemonData> = MutableLiveData()
    val action: LiveData<PokemonData> get() = _action

    fun getNames() {

        PokemonRepository.getPokemonNames().subscribeBy(onError = {}, onSuccess = { it ->

            Timber.log(Log.ERROR, "onSuccess ")

            // cache 가 없는 경우 데이터를 맵에 넣어준다. map 에 넣어 O(1)로 검색하여 빠른 검색을 하기위한 구현.
            map.takeIf { it.isEmpty() }.apply {
                // 포켓몬 이름 api에서 받아온 데이터를 전체 한번 루프를 돌아 데이터를 하나씩 꺼낸다.
                it.pokemons.forEach { pokemonName ->
                    // 포켓몬 이름이 배열로 0번째 인덱스는 한글이름, 1번째 인덱스에는 영어이름이 들어있다.
                    pokemonName.names.forEach { name ->
                        // 검색어를 영문대소문자를 무시해야한다.
                        name.toUpperCase().toCharArray().apply {
                            // 문자열(이름)을 조합(고등수학 수열, 조합에서 조합)을 이용하여 ["a,b,c"] 배열을 [a],[b],[c] [ab],[bc] [abc] 로 구분하여 해당 문자열을 hashCode를 뽑아내어
                            // 맵에 해쉬코드 값을 구분지어 같은 해쉬코드에 해당하는 포켓몬 이름들을 리스트에 묶어 검색하는 방법을 구현하였음.
                            for (index in 1..this.size) {
                                for (x in 0..this.size - index) {
                                    val hashKey = name.toUpperCase().substring(x, x + index).hashCode() // hash 값을 만듬
                                    takeIf { map.containsKey(hashKey) }.run {
                                        map[hashKey]?.add(PokemonData(pokemonName.id,pokemonName.names[0],pokemonName.names[1]))
                                    } ?: kotlin.run {
                                        map.put(hashKey, mutableListOf(PokemonData(pokemonName.id,pokemonName.names[0],pokemonName.names[1])))
                                    }

                                }
                            }
                        }

                    }
                }
            }



            Timber.log(Log.ERROR, "onSuccess ")

//            val map=it.pokemons.map { it.id to it }


        })

    }

    fun searchPokemon(hashCode: Int) {
        searchList.value = map[hashCode] ?: listOf()
        Timber.log(Log.ERROR, map[hashCode].toString())
    }

    fun getPokemonName(position: Int) = searchList.value?.get(position)?.korName

    fun clickPokemon(position: Int){
        _action.value = searchList.value?.get(position)
    }
}
