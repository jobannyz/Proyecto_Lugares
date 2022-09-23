package com.example.lugares_v.repository

import androidx.lifecycle.LiveData
import com.example.lugares_v.data.LugarDao
import com.example.lugares_v.model.Lugar

class LugarRepository(private val lugarDao: LugarDao) {
    suspend fun addLugar(lugar: Lugar) {
        lugarDao.addLugar(lugar)
    }
    suspend fun updateLugar(lugar: Lugar) {
        lugarDao.updateLugar(lugar)
    }
    suspend fun deleteLugar(lugar: Lugar) {
        lugarDao.deleteLugar(lugar)
    }
    val getLugares : LiveData<List<Lugar>> = lugarDao.getLugares()
}