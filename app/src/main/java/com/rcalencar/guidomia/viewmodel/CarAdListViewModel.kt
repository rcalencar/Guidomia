package com.rcalencar.guidomia.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rcalencar.guidomia.model.DataSource

class CarAdListViewModel(val dataSource: DataSource) : ViewModel() {
    val liveData = dataSource.getList()
    private var make: String? = null
    private var model: String? = null

    fun filterMakes(selected: String?) {
        make = selected
        dataSource.filter(make, model)
    }

    fun filterModel(selected: String?) {
        model = selected
        dataSource.filter(make, model)
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