package com.rcalencar.guidomia.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CarAdDao {
    @Query("SELECT * FROM CarAd")
    fun getAll(): Flow<List<CarAd>>

    @Query("SELECT * FROM CarAd WHERE make like :make AND model like :model")
    fun getByMakeAndModel(make: String, model: String): Flow<List<CarAd>>

    @Query("SELECT * FROM CarAd WHERE make like :make")
    fun getByMake(make: String): Flow<List<CarAd>>

    @Query("SELECT * FROM CarAd WHERE model like :model")
    fun getByModel(model: String): Flow<List<CarAd>>

    @Query("SELECT DISTINCT make FROM CarAd")
    suspend fun getMakes(): List<String>

    @Query("SELECT DISTINCT model FROM CarAd")
    suspend fun getModels(): List<String>

    @Insert
    suspend fun insert(word: CarAd)
}