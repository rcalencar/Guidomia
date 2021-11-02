package com.rcalencar.guidomia.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rcalencar.guidomia.data.CarAd
import com.rcalencar.guidomia.data.DataSource

class CarAdListViewModel(val dataSource: DataSource) : ViewModel() {
    val liveData = dataSource.getList()
    private val _expandedItem: MutableLiveData<CarAd> = MutableLiveData(null)
    val expandedItem: LiveData<CarAd>
        get() {
            return _expandedItem
        }
    private var make: String? = null
    private var model: String? = null

    fun expand(carAd: CarAd) {
        _expandedItem.value = carAd
    }

    fun filterMakes(selected: String?) {
        make = selected
        dataSource.filter(make, model)
        expandFirst()
    }

    fun filterModel(selected: String?) {
        model = selected
        dataSource.filter(make, model)
        expandFirst()
    }

    init {
        expandFirst()
    }

    private fun expandFirst() {
        liveData.value?.getOrNull(0)?.let {
            _expandedItem.value = it
        }
    }
}

class ListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarAdListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CarAdListViewModel(
                dataSource = DataSource.getDataSource(context.assets)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}