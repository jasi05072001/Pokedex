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

    init {
        loadPokemon()
    }

    fun getDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888,true)

        Palette.from(bitmap).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue -> /// can use dominantSwatch
                onFinish(Color(color =colorValue))
            }
        }
    }

    fun loadPokemon(){
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getPokemonList(limit = PAGE_SIZE, offset =currentPage * PAGE_SIZE)

            when(result){
                is Resources.Success ->{
                    endReached.value = currentPage * PAGE_SIZE >= result.data!!.count
                    val pokedexEntries = result.data.results.mapIndexed { index, entry ->
                        val number =  if(entry.url.endsWith("/")){
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        }
                        else{
                            entry.url.takeLastWhile { it.isDigit() }
                        }
                        val url =  "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                        PokeDexListEntry(entry.name.capitalize(Locale.ROOT),url,number.toInt())
                    }

                    currentPage++
                    loadError.value = ""
                    isLoading.value = false
                    pokemonList.value += pokedexEntries

                }
                is  Resources.Error ->{
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }

        }

    }
}