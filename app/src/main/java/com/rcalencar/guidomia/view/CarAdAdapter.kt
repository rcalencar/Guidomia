package com.rcalencar.guidomia.view

import android.content.Context
import android.graphics.BitmapFactory
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.BulletSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rcalencar.guidomia.R
import com.rcalencar.guidomia.databinding.AdapterItemCarAdBinding
import com.rcalencar.guidomia.model.CarAd
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import java.io.IOException

const val BULLET_GAP_WIDTH = 22
const val BULLET_RADIUS = 10

class CarAdAdapter(private val onClick: (CarAd) -> Unit) : ListAdapter<CarAd, CarAdAdapter.ViewHolder>(DiffCallback) {
    private var expandedListItem: Pair<Int, CarAd>? = null

    class ViewHolder(
        itemView: AdapterItemCarAdBinding,
        val expand: (Pair<Int, CarAd>) -> Unit,
        val expanded: () -> Pair<Int, CarAd>?,
        val onClick: (CarAd) -> Unit
    ) : RecyclerView.ViewHolder(itemView.root) {
        private val textView: TextView = itemView.carAdDescription
        private val priceView: TextView = itemView.carAdPrice
        private val imageView: ImageView = itemView.carAdImage
        private val ratingView: MaterialRatingBar = itemView.carAdRating
        private val expandable: Group = itemView.carAdExpandable
        private val pros: TextView = itemView.carAdPros
        private val cons: TextView = itemView.carAdCons
        private val more: TextView = itemView.more

        private var currentItem: Pair<Int, CarAd>? = null

        init {
            itemView.root.setOnClickListener {
                currentItem?.let {
                    expand(it)
                }
            }
        }

        fun bind(index: Int, carAd: CarAd) {
            currentItem = Pair(index, carAd)

            textView.text =
                itemView.context.getString(R.string.car_ad_description, carAd.make, carAd.model)
            priceView.text =
                itemView.context.getString(R.string.car_ad_price, carAd.formattedCustomerPrice())
            imageView.setImageAssets(itemView.context, carAd.image())
            ratingView.rating = carAd.rating.toFloat()

            expandable.isGone = true
            bulletList(carAd.prosList, pros)
            bulletList(carAd.consList, cons)

            expandable.visibility =
                if (carAd.uid == expanded()?.second?.uid) View.VISIBLE else View.GONE

            more.setOnClickListener {
                onClick(carAd)
            }
        }

        private fun bulletList(list: List<String>, container: TextView) {
            val items = if (list.isEmpty()) listOf(container.context.getString(R.string.empty_list)) else list.filter { it.isNotEmpty() }
            container.text = items.joinTo(SpannableStringBuilder(), System.lineSeparator()) {
                SpannableString(it).apply {
                    setSpan(BulletSpan(BULLET_GAP_WIDTH, container.context.getColor(R.color.primaryColor), BULLET_RADIUS), 0, this.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterItemCarAdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, expand, { expandedListItem }, onClick)
    }

    val expand: (Pair<Int, CarAd>?) -> Unit = {
        expandedListItem?.let {
            notifyItemChanged(expandedListItem!!.first)
        }
        it?.let {
            notifyItemChanged(it.first)
        }
        expandedListItem = it
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(position, item)
    }
}

object DiffCallback : DiffUtil.ItemCallback<CarAd>() {
    override fun areItemsTheSame(oldItem: CarAd, newItem: CarAd): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CarAd, newItem: CarAd): Boolean {
        return oldItem.uid == newItem.uid
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