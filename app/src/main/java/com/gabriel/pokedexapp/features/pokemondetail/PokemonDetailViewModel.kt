package com.gabriel.pokedexapp.features.pokemondetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabriel.pokedexapp.data.remote.response.Pokemon
import com.gabriel.pokedexapp.repository.PokemonRepository
import com.gabriel.pokedexapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {
    private val _pokemonInfo = MutableStateFlow<Resource<Pokemon>>(Resource.Loading())
    val pokemonInfo: StateFlow<Resource<Pokemon>> = _pokemonInfo.asStateFlow()

    fun fetchPokemonInfo(pokemonName: String) {
        viewModelScope.launch {
            _pokemonInfo.value = repository.getPokemonInfo(pokemonName)
        }
    }
}