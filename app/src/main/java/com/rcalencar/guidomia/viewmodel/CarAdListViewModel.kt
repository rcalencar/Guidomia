package com.rcalencar.guidomia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rcalencar.guidomia.GuidomiaApplication
import com.rcalencar.guidomia.model.CarAd

class CarAdListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as GuidomiaApplication).repository
    val liveData: LiveData<List<CarAd>> = repository.liveData

    private var make: String? = null
    private var model: String? = null

    private val mutableSelectedItem = MutableLiveData<CarAd>()
    val selectedItem: LiveData<CarAd> get() = mutableSelectedItem

    fun selectItem(item: CarAd) {
        mutableSelectedItem.value = item
    }

    fun filterMakes(selected: String?) {
        make = selected
        repository.filter(make, model)
    }

    fun filterModel(selected: String?) {
        model = selected
        repository.filter(make, model)
    }
}