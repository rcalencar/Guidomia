package com.rcalencar.guidomia

import android.app.Application
import com.rcalencar.guidomia.model.CarAdRepository
import com.rcalencar.guidomia.model.CarAdRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class GuidomiaApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { CarAdRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { CarAdRepository(database.carAdDao()) }
}
