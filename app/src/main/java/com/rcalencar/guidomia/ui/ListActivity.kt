package com.rcalencar.guidomia.ui

import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rcalencar.guidomia.R
import com.rcalencar.guidomia.data.CarAd
import com.rcalencar.guidomia.databinding.ActivityMainBinding

class ListActivity : AppCompatActivity() {
    private val listViewModel by viewModels<ListViewModel> { ListViewModelFactory(this) }
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val adapter = CarAdAdapter { item -> listViewModel.expand(item) }

        binding.recyclerView.adapter = adapter
        val decorator = DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)
        ContextCompat.getDrawable(applicationContext, R.drawable.divider)?.let {
            decorator.setDrawable(
                it
            )
        }
        binding.recyclerView.addItemDecoration(decorator)
        binding.recyclerView.addItemDecoration(MarginItemDecoration(resources.getDimension(R.dimen.default_padding).toInt()))

        listViewModel.liveData.observe(this, {
            it?.let {
                adapter.submitList(it as MutableList<CarAd>)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}