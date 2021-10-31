package com.rcalencar.guidomia.data

import kotlinx.serialization.Serializable

@Serializable
data class CarAd(val make: String, val model: String, val rating: Int, val marketPrice: Double, val customerPrice: Double, val prosList: List<String>, val consList: List<String>, var expanded: Boolean = false) {
    val id = "${make.lowercase().replace(" ", "_")}_${model.lowercase().replace(" ", "_")}"
    fun formattedCustomerPrice() : String {
        return if (customerPrice > 1000) {
            String.format("%.0fk", customerPrice / 1000)
        } else {
            String.format("%.2f", customerPrice)
        }
    }
}