package com.example.lugares_v.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.lugares_v.data.LugarDatabase
import com.example.lugares_v.model.Lugar
import com.example.lugares_v.repository.LugarRepository
import kotlinx.coroutines.launch

class LugarViewModel(application: Application) : AndroidViewModel(application) {
    val getLugares : LiveData<List<Lugar>>
    private val repository: LugarRepository
    init {
        val lugarDao = LugarDatabase.getDatabase(application).lugarDao()
        repository = LugarRepository(lugarDao)
        getLugares = repository.getLugares
    }
    fun saveLugar(lugar: Lugar) {
        viewModelScope.launch { repository.saveLugar(lugar) }
    }
    fun deleteLugar(lugar: Lugar) {
        viewModelScope.launch { repository.deleteLugar(lugar)}
    }
}