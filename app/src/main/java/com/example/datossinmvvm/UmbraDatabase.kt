package com.example.datossinmvvm

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Mapa::class], version = 1)
abstract class UmbraDatabase : RoomDatabase() {
    abstract fun mapaDao(): MapaDao
}