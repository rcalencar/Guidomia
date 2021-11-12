package com.rcalencar.guidomia.model

import android.content.Context
import android.content.res.AssetManager
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Database(entities = [CarAd::class], version = 1)
@TypeConverters(Converters::class)
abstract class CarAdRoomDatabase : RoomDatabase() {

    abstract fun carAdDao(): CarAdDao

    companion object {
        @Volatile
        private var INSTANCE: CarAdRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): CarAdRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CarAdRoomDatabase::class.java,
                    "car_ad_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(WordDatabaseCallback(context, scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class WordDatabaseCallback(
            private val context: Context,
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(context, database.carAdDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(context: Context, carAdDao: CarAdDao) {
            carAdList(context.assets).forEach { carAdDao.insert(it) }
        }
    }
}


private val json = Json { ignoreUnknownKeys = true }

fun carAdList(assets: AssetManager): List<CarAd> {
    val fileInString = assets.open("car_list.json").bufferedReader().use { it.readText() }
    val data = json.decodeFromString<Array<CarAd>>(fileInString)
    data.forEachIndexed { index, carAd -> carAd.uid = index }
    return data.toList()
}