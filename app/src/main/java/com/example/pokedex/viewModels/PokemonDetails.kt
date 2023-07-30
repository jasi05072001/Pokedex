package com.example.pokedex.viewModels

import androidx.lifecycle.ViewModel
import com.example.pokedex.data.remote.responses.Pokemon
import com.example.pokedex.repository.PokemonRepository
import com.example.pokedex.utils.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetails  @Inject constructor(
    private val repository: PokemonRepository
): ViewModel() {

    suspend fun getPokemonInfo(pokemonName: String) :Resources<Pokemon>{
        return repository.getPokemonDetails(pokemonName)

    }


}