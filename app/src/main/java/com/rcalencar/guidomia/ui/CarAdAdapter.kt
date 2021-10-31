package com.rcalencar.guidomia.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rcalencar.guidomia.R
import com.rcalencar.guidomia.data.CarAd
import com.rcalencar.guidomia.databinding.BulletPointBinding
import com.rcalencar.guidomia.databinding.CarAdItemBinding
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import java.io.IOException

class CarAdAdapter(private val onClick: (CarAd) -> Unit) :
    ListAdapter<CarAd, CarAdAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(itemView: CarAdItemBinding, val onClick: (CarAd) -> Unit) :
        RecyclerView.ViewHolder(itemView.root) {
        private val textView: TextView = itemView.carAdDescription
        private val priceView: TextView = itemView.carAdPrice
        private val imageView: ImageView = itemView.carAdImage
        private val ratingView: MaterialRatingBar = itemView.carAdRating
        private val expandable: LinearLayout = itemView.carAdExpandable
        private val pros: LinearLayout = itemView.carAdPros
        private val cons: LinearLayout = itemView.carAdCons

        private var currentItem: CarAd? = null

        init {
            itemView.root.setOnClickListener {
                currentItem?.let {
                    onClick(it)
                    bindingAdapter?.notifyDataSetChanged()
                }
            }
        }

        fun bind(carAd: CarAd) {
            currentItem = carAd

            textView.text = itemView.context.getString(R.string.car_ad_description, carAd.make, carAd.model)
            priceView.text = itemView.context.getString(R.string.car_ad_price, carAd.formattedCustomerPrice())
            imageView.setImageAssets(itemView.context, "${carAd.id}.jpg")
            ratingView.rating = carAd.rating.toFloat()

            expandable.visibility = View.GONE
            bulletList(carAd.prosList, pros)
            bulletList(carAd.consList, cons)

            expandable.visibility = if (carAd.expanded) View.VISIBLE else View.GONE
        }

        private fun bulletList(list: List<String>, container: LinearLayout) {
            container.removeAllViews()
            val items = if (list.isEmpty()) listOf(container.context.getString(R.string.empty_list)) else list

            for (item in items) {
                if (item.isEmpty()) continue
                val binding = BulletPointBinding.inflate(LayoutInflater.from(itemView.context))
                binding.bulletPointText.text = item
                container.addView(binding.root)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CarAdItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

object DiffCallback : DiffUtil.ItemCallback<CarAd>() {
    override fun areItemsTheSame(oldItem: CarAd, newItem: CarAd): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CarAd, newItem: CarAd): Boolean {
        return oldItem.id == newItem.id
    }
}

fun ImageView.setImageAssets(context: Context, fileName: String) {
    try {
        with(context.assets.open(fileName)) {
            setImageBitmap(BitmapFactory.decodeStream(this))
        }
    } catch (e: IOException) {
        setImageResource(R.drawable.ic_launcher_foreground)
    }
}