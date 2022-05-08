package com.rcalencar.guidomia.model

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData

class CarAdRepository(private val carAdDao: CarAdDao) {
    val carAds = MediatorLiveData<List<CarAd>>()
    val makes = carAdDao.getMakes().asLiveData()
    val models = MediatorLiveData<List<String>>()

    fun filterModels(make: String?) {
        make?.let {
            models.addSource(carAdDao.getModels(make).asLiveData()) { value ->
                models.setValue(value)
            }
        } ?: run {
            models.setValue(emptyList())
        }
    }

    init {
        filterCarAds()
    }

    fun filterCarAds(make: String? = null, model: String? = null) {
        if (make != null && model != null) {
            carAds.addSource(carAdDao.getByMakeAndModel(make, model).asLiveData()) { value ->
                carAds.setValue(value)
            }
        } else if (make != null) {
            carAds.addSource(carAdDao.getByMake(make).asLiveData()) { value ->
                carAds.setValue(value)
            }
        } else if (model != null) {
            carAds.addSource(carAdDao.getByModel(model).asLiveData()) { value ->
                carAds.setValue(value)
            }
        } else {
            carAds.addSource(carAdDao.getAll().asLiveData()) { value ->
                carAds.setValue(value)
            }
        }
    }
}