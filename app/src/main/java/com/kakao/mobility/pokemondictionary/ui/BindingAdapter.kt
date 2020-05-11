package com.kakao.mobility.pokemondictionary.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kakao.mobility.pokemondictionary.data.PokemonData
import com.kakao.mobility.pokemondictionary.data.PokemonNameResponse
import com.kakao.mobility.pokemondictionary.ui.main.SearchAdapter


@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) =
    url?.let { Glide.with(view.context).load(url).into(view) }

@BindingAdapter(value = ["pokemons"])
fun pokemons(rvList: RecyclerView, list: List<PokemonData>?) {
    list?.run {
        (rvList.adapter as SearchAdapter).submitList(this)
    }
}


