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
    suspend fun addLugar(lugar: Lugar) {
        viewModelScope.launch { repository.addLugar(lugar) }
    }
    suspend fun updateLugar(lugar: Lugar) {
        viewModelScope.launch { repository.updateLugar(lugar) }
    }
    suspend fun deleteLugar(lugar: Lugar) {
        viewModelScope.launch { repository.deleteLugar(lugar) }
    }
}