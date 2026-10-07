package com.example.datossinmvvm

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mapa")
data class Mapa(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val niveles: Int,
    val precio: Int
)