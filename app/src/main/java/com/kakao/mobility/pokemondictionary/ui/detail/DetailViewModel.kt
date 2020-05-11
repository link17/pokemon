package com.kakao.mobility.pokemondictionary.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakao.mobility.pokemondictionary.data.LocationData
import com.kakao.mobility.pokemondictionary.data.PokemonData
import com.kakao.mobility.pokemondictionary.data.PokemonDetailData
import com.kakao.mobility.pokemondictionary.repository.PokemonRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.zipWith
import timber.log.Timber

class DetailViewModel : ViewModel() {

    // 포켓몬 상세 데이터
    // 서식지 정보와 나눌까 고민중..
    val pokemonDetailData: MutableLiveData<PokemonDetailData> = MutableLiveData()

    // 서식지 정보 버튼 클릭시 호출됨.
    // 지도화면으로 넘어갈 서식지 정보들을 넣어줘야 함.
    private val _action: MutableLiveData<ArrayList<LocationData>> = MutableLiveData()
    val action: LiveData<ArrayList<LocationData>> get() = _action

    fun onClickMap() {
        _action.value = pokemonDetailData.value?.locationData
    }

    /**
     * 스트림을 한번에 두 서버데이터를 zip 을 이용하여 동시에 view에 보내주도록 하는 함수.
     */

    fun getPokemonZip(pokemonData: PokemonData?) = pokemonData?.let {
        PokemonRepository.run {

            // 포켓몬 api 서버 데이터 받아옴.
            getPokemon(it.id).observeOn(AndroidSchedulers.mainThread()).doOnNext {
                // 로컬(캐쉬) -> 서버 데이터 순으로 데이터가 내려와 view 에 보내줌.
                it?.let {
                    it.locationData = pokemonDetailData.value?.locationData
                    pokemonDetailData.value = it
                }
            }.zipWith(getPokemonLocations() // zip 메소드를 활용하여 다른 퍼블리셔에 스트림을 한번에 처리 하도록 함.
                .observeOn(AndroidSchedulers.mainThread()).doOnNext {
                    // 로컬(캐쉬) -> 서버 데이터 순으로 데이터가 내려와 view 에 보내줌.
                    it.let {
                        pokemonDetailData.value?.let { pokemonData ->
                            pokemonData.locationData = it[pokemonData.id]
                        }
                    }
                }
            ).subscribeBy(onError = {
                Timber.log(Log.ERROR, "$it")
            }, onComplete = {
                // onComplete
                // 서버데이터 까지 모두 받아온 상태이다.
            }, onNext = { pair ->
                pair.first?.let {
                    it.locationData = pair.second[it.id]
                    it.korName = pokemonData.korName
                    pokemonDetailData.value = it
                }
            })

        }


    }

    /**
     * 스트림을 두 서버데이터에 데이터가 올때 먼저온 데이터를 먼저 보여주는 함수.
     */
    fun getPokemon(pokemonData: PokemonData) {
        pokemonDetailData.value = PokemonDetailData(0, 0, null)

        PokemonRepository.run {
            getPokemon(pokemonData.id).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it?.let {
                        it.locationData = pokemonDetailData.value?.locationData
                        pokemonDetailData.value = it
                    }

                }, {
                    Timber.log(Log.ERROR, "$it")
                }, {
                    // onComplete
                    // 서버데이터 까지 모두 받아온 상태이다.
                }
                )

            getPokemonLocations().observeOn(AndroidSchedulers.mainThread()).subscribeBy(onError = {
                Timber.log(Log.ERROR, "onError $it")
            }, onNext = {
                Timber.log(Log.ERROR, "onNext $it")
                it.let {
                    pokemonDetailData.value?.let { pokemonData ->
                        pokemonData.locationData = it[pokemonData.id]
                    }
                }

            }, onComplete = {

                Timber.log(Log.ERROR, "onComplete")

            })
        }
    }
}