package com.rcalencar.guidomia.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class CarAd(
    @PrimaryKey var uid: Int? = null,
    val make: String,
    val model: String,
    val rating: Int,
    val marketPrice: Double,
    val customerPrice: Double,
    val prosList: List<String>,
    val consList: List<String>
) {
    fun image() = "${make.lowercase().replace(" ", "_")}_${model.lowercase().replace(" ", "_")}.jpg"

    fun formattedCustomerPrice(): String {
        return if (customerPrice > 1000) {
            String.format("%.0fk", customerPrice / 1000)
        } else {
            String.format("%.2f", customerPrice)
        }
    }
}