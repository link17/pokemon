package com.kakao.mobility.pokemondictionary.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * 포켓몬 기본 정보.(한글이름을 가지고 있는것이 특징)
 */
open class PokemonData(
    open val id: Int = 0, // id
    open var korName: String = "", // 한글 이름
    @SerializedName("name")
    open val engName: String = "" // 영어 이름
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(korName)
        parcel.writeString(engName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PokemonData> {
        override fun createFromParcel(parcel: Parcel): PokemonData {
            return PokemonData(parcel)
        }

        override fun newArray(size: Int): Array<PokemonData?> {
            return arrayOfNulls(size)
        }
    }
}