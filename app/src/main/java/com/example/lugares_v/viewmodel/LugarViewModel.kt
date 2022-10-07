package com.example.lugares_v.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.lugares_v.data.LugarDao
import com.example.lugares_v.data.LugarDatabase
import com.example.lugares_v.model.Lugar
import com.example.lugares_v.repository.LugarRepository
import kotlinx.coroutines.launch

class LugarViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: LugarRepository
    val getLugares: LiveData<List<Lugar>>
    init {
        val lugarDao = LugarDatabase.getDatabase(application).lugarDao()
        repository = LugarRepository(lugarDao)
        getLugares = repository.getLugares
    }
    fun saveLugar(lugar: Lugar) {
        viewModelScope.launch { repository.saveLugar(lugar) }
    }
    fun deleteLugar(lugar: Lugar) {
        viewModelScope.launch { repository.deleteLugar(lugar) }
    }
}