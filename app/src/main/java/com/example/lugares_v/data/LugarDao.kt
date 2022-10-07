package com.example.lugares_v.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.lugares_v.model.Lugar
@Dao
interface LugarDao {
    //CRUD Create Read Update Delete
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLugar(lugar: Lugar)
    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateLugar(lugar: Lugar)
    @Delete
    suspend fun deleteLugar(lugar: Lugar)
    @Query("SELECT * FROM LUGAR")
    fun getLugares() : LiveData<List<Lugar>>
}