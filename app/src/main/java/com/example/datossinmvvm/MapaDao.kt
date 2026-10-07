package com.example.datossinmvvm

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MapaDao {
    @Query("SELECT * FROM mapa")
    suspend fun getAll(): List<Mapa>

    @Insert
    suspend fun insert(mapa: Mapa)

    @Update
    suspend fun update(mapa: Mapa)

    @Delete
    suspend fun delete(mapa: Mapa)
}