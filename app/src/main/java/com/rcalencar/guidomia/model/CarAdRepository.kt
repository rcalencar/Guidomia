package com.rcalencar.guidomia.model

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData

class CarAdRepository(private val carAdDao: CarAdDao) {
    val liveData = carAdDao.getAll().asLiveData()

    fun filter(make: String? = null, model: String? = null) {
//        if (make != null && model != null) {
//            liveData.addSource(carAdDao.getByMakeAndModel(make, model).asLiveData()) { value ->
//                liveData.setValue(value)
//            }
//        } else if (make != null) {
//            liveData.addSource(carAdDao.getByMake(make).asLiveData()) { value ->
//                liveData.setValue(value)
//            }
//        } else if (model != null) {
//            liveData.addSource(carAdDao.getByModel(model).asLiveData()) { value ->
//                liveData.setValue(value)
//            }
//        } else {
//            liveData.addSource(carAdDao.getAll().asLiveData()) { value ->
//                liveData.setValue(value)
//            }
//        }
    }

    suspend fun getMakes(): List<String> {
        return carAdDao.getMakes()
    }

    suspend fun getModels(): List<String> {
        return carAdDao.getModels()
    }
}