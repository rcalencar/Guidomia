package com.rcalencar.guidomia.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rcalencar.guidomia.data.CarAd
import com.rcalencar.guidomia.data.DataSource

class ListViewModel(val dataSource: DataSource) : ViewModel() {
    val liveData = dataSource.getList()
    var expandedItem: MutableLiveData<CarAd> = MutableLiveData(null)
    var make: String? = null
    var model: String? = null

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
            expandedItem.value = it
        }
    }
}

class ListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListViewModel(
                dataSource = DataSource.getDataSource(context.assets)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}