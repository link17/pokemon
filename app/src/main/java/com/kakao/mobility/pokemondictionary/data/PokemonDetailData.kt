package com.kakao.mobility.pokemondictionary.data

import android.os.Parcel
import android.os.Parcelable

/**
 * 포켓몬 상세정보.
 */
data class PokemonDetailData(
    val height: Int, // 키 decimeter
    val weight: Int, // 몸무게
    var locationData: ArrayList<LocationData>?, // null 일 경우 알려진 서식지 정보가 없다.

    val sprites: Sprites? = null // 이미지 url 객체


) : PokemonData() {

    var imageUrl: String? = null
        get() = sprites?.let {
            it.front_default ?: it.front_shiny ?: it.back_default ?: it.back_female ?: it.back_shiny
            ?: it.back_shiny_female ?: it.front_female ?: it.front_shiny_female
        } // 이미지 url

    var heightToCentimeter: Int? = null
        get() = height * 10

    var weightToGram: Int? = null
        get() = weight * 100
//    init {
//        imageUrl = sprites?.let {
//            it.front_default ?: it.front_shiny ?: it.back_default ?: it.back_female ?: it.back_shiny
//            ?: it.back_shiny_female ?: it.front_female ?: it.front_shiny_female
//        }
//    }
}

/**
 * 위치정보
 */
data class LocationData(
    val lat: Double, // 위도
    val lng: Double // 경도
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(lat)
        parcel.writeDouble(lng)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LocationData> {
        override fun createFromParcel(parcel: Parcel): LocationData {
            return LocationData(parcel)
        }

        override fun newArray(size: Int): Array<LocationData?> {
            return arrayOfNulls(size)
        }
    }
}

data class Sprites(
    val back_default: String?,
    val back_female: String?,
    val back_shiny: String?,
    val back_shiny_female: String?,
    val front_default: String?, // default image url
    val front_female: String?,
    val front_shiny: String?,
    val front_shiny_female: String?
)