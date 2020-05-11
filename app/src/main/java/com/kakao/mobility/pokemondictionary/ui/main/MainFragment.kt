package com.kakao.mobility.pokemondictionary.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.widget.RxTextView
import com.kakao.mobility.pokemondictionary.R
import com.kakao.mobility.pokemondictionary.databinding.FragmentMainBinding
import com.kakao.mobility.pokemondictionary.ui.detail.DetailFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java).apply {
            this.getNames()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMainBinding.inflate(inflater, container, false).apply {
        this@MainFragment.viewModel.apply {
            viewModel = this
            recyclerView.adapter = SearchAdapter(this)
            recyclerView.layoutManager = LinearLayoutManager(context)
        }

        lifecycleOwner = this@MainFragment
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 포켓몬 검색 (서버나 데이터가 많을시 debounce 등을 이용하여 다연속 검색을 줄이는 방법을 사용하면 좋을 것 같다..
        RxTextView.textChanges(view.findViewById<EditText>(R.id.editTextSearch)).skipInitialValue()
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
            viewModel.searchPokemon(it.toString().toUpperCase(Locale.getDefault()).trim().hashCode())
        }

        viewModel.action.observe(this, Observer {
            fragmentManager?.let { it1 ->
                it?.let { pokemonData ->
                    it1.beginTransaction().addToBackStack(null).setCustomAnimations(
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right
                    )
                        .add(
                            R.id.container,
                            DetailFragment.newInstance(pokemonData),
                            DetailFragment.toString()
                        ).commit()
                }

            }
        })

//        RxView.clicks(view.findViewById<Button>(R.id.button))
//            .throttleFirst(3000L, TimeUnit.MILLISECONDS).subscribeBy(onComplete = {
//
//            }, onError = {
//                Timber.e("Button")
//            })
//        viewModel.getUser()

//        view.findViewById<Button>(R.id.button).clicks().observeOn(AndroidSchedulers.mainThread()).subscribe {
//
//            Log.e("","Button click")
//
//        }
    }

    fun isLetter(c: Char) = c in 'a'..'z' || c in 'A'..'Z'
    fun isNotDigit(c: Char) = c !in '0'..'9'


}

//class User(val id: Int, val name: String, val address: String).
//
fun String.lastChar(): Char = this[this.length - 1]
//
//
//fun User.validateBeforeSave() { //User class의 확장 함수로 정의
//    fun validate(value: String, fieldName: String) {
//        if (value.isEmpty()) { // Extension function이므로 id값에 바로 접근가능
//            throw IllegalArgumentException("Can't save user $id: empty $fieldName")
//        }
//    }
//
//    validate(name, "Name")
//    validate(address, "Address")
//}
