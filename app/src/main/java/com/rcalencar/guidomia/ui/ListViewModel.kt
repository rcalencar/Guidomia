package com.rcalencar.guidomia.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rcalencar.guidomia.data.DataSource

class ListViewModel(val dataSource: DataSource) : ViewModel() {
    val liveData = dataSource.getList()
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