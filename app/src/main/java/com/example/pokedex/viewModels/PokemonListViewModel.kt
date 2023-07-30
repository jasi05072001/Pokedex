package com.example.pokedex.viewModels

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.pokedex.data.models.PokeDexListEntry
import com.example.pokedex.repository.PokemonRepository
import com.example.pokedex.utils.Constants.PAGE_SIZE
import com.example.pokedex.utils.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
):ViewModel() {

    private var currentPage = 0
    var pokemonList = mutableStateOf<List<PokeDexListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var cachedPokemonList = listOf<PokeDexListEntry>()
    private var isSearchStarted = true
    var isSearching = mutableStateOf(false)


    init {
        loadPokemon()
    }

    fun loadPokemon(){
        viewModelScope.launch {
            isLoading.value = true

            when(
                val result = repository.getPokemonList(
                    PAGE_SIZE,
                    currentPage * PAGE_SIZE
                )
            ){
                is Resources.Success -> {
                    endReached.value = currentPage * PAGE_SIZE >= result.data!!.count

                    val pokedexEntries = result.data.results.mapIndexed { _, entry ->
                        val number =  if(entry.url.endsWith("/")){
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        }
                        else{
                            entry.url.takeLastWhile { it.isDigit() }
                        }
                        val url =  "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                        PokeDexListEntry(entry.name.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        },url,number.toInt())
                    }

                    currentPage ++
                    loadError.value = ""
                    isLoading.value = false
                    pokemonList.value += pokedexEntries

                }
                is  Resources.Error ->{
                    loadError.value = result.message!!
                    isLoading.value = false
                }
                is Resources.Loading ->{

                }
            }
        }

    }

    fun getDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888,true)

        Palette.from(bitmap).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(color = colorValue))
            }
        }
    }

    fun searchPokemonList(name:String){
        val listToSearch = if (isSearchStarted){
            pokemonList.value
        }else{
            cachedPokemonList
        }

        viewModelScope.launch(Dispatchers.Default) {

            if (name.isEmpty()){
                pokemonList.value = cachedPokemonList
                isSearching.value = false
                isSearchStarted = true
                return@launch
            }
            val results = listToSearch.filter {
                it.pokemonName.contains(name.trim(), ignoreCase = true) ||
                        it.number.toString() == name.trim()
            }
            if (isSearchStarted){
                cachedPokemonList = pokemonList.value
                isSearchStarted = false
            }
            pokemonList.value = results
            isSearching.value = true
        }
    }
}