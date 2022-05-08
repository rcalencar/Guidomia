package com.rcalencar.guidomia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rcalencar.guidomia.GuidomiaApplication
import com.rcalencar.guidomia.model.CarAd

class CarAdListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as GuidomiaApplication).repository
    val liveData: LiveData<List<CarAd>> = repository.carAds
    val makes: LiveData<List<String>> = repository.makes
    val models: LiveData<List<String>> = repository.models

    val selectedMake = MutableLiveData<String?>()
    val selectedModel = MutableLiveData<String?>()

    private val mutableSelectedItem = MutableLiveData<CarAd?>()
    val selectedItem: LiveData<CarAd?> get() = mutableSelectedItem
    fun selectItem(item: CarAd) {
        mutableSelectedItem.value = item
    }

    fun filterByMakes(selected: String?) {
        mutableSelectedItem.value = null
        selectedMake.value = selected
        selectedModel.value = null
        repository.filterModels(selected)
        repository.filterCarAds(selected)
    }

    fun filterByModel(selected: String?) {
        mutableSelectedItem.value = null
        selectedModel.value = selected
        repository.filterCarAds(selectedMake.value, selectedModel.value)
    }
}