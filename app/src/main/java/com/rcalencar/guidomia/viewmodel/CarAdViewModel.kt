package com.rcalencar.guidomia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rcalencar.guidomia.model.CarAd

class CarAdViewModel() : ViewModel() {
    private val mutableSelectedItem = MutableLiveData<CarAd>()
    val selectedItem: LiveData<CarAd> get() = mutableSelectedItem

    fun selectItem(item: CarAd) {
        mutableSelectedItem.value = item
    }
}