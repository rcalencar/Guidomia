package com.rcalencar.guidomia.data

import android.content.res.AssetManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class DataSource(assets: AssetManager) {
    private val initialList = carAdList(assets)
    private val liveData = MutableLiveData(initialList)

    fun getList(): LiveData<List<CarAd>> {
        return liveData
    }

    companion object {
        private var INSTANCE: DataSource? = null

        fun getDataSource(assets: AssetManager): DataSource {
            return synchronized(DataSource::class) {
                val newInstance = INSTANCE ?: DataSource(assets)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}

private val json = Json { ignoreUnknownKeys = true }

fun carAdList(assets: AssetManager): List<CarAd> {
    val fileInString: String = assets.open("car_list.json").bufferedReader().use { it.readText() }
    val data = json.decodeFromString<Array<CarAd>>(fileInString)
    return data.toList()
}