package com.example.lugares_v.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lugares_v.data.LugarDao
import com.example.lugares_v.model.Lugar

class LugarRepository(private val lugarDao: LugarDao) {
    fun saveLugar(lugar: Lugar) {
        lugarDao.saveLugar(lugar)
    }
    fun deleteLugar(lugar: Lugar) {
        lugarDao.deleteLugar(lugar)
    }
    val getLugares : MutableLiveData<List<Lugar>> = lugarDao.getLugares()
}