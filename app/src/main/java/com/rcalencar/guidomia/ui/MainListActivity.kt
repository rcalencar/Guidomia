package com.rcalencar.guidomia.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rcalencar.guidomia.R
import com.rcalencar.guidomia.data.CarAd
import com.rcalencar.guidomia.data.DataSource
import com.rcalencar.guidomia.databinding.ActivityMainBinding
import com.rcalencar.guidomia.model.CarAdListViewModel
import com.rcalencar.guidomia.model.ListViewModelFactory

class MainListActivity : AppCompatActivity() {
    private val listViewModel by viewModels<CarAdListViewModel> { ListViewModelFactory(this) }
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val carAdAdapter = CarAdAdapter()
        binding.recyclerView.adapter = carAdAdapter
        val decorator = DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)
        ContextCompat.getDrawable(applicationContext, R.drawable.divider)?.let {
            decorator.setDrawable(
                it
            )
        }
        binding.recyclerView.addItemDecoration(decorator)
        binding.recyclerView.addItemDecoration(
            MarginItemDecoration(
                resources.getDimension(R.dimen.default_padding).toInt()
            )
        )

        listViewModel.liveData.observe(this, {
            it?.let {
                carAdAdapter.expand(null)
                carAdAdapter.submitList(it as MutableList<CarAd>) {
                    if (it.isNotEmpty()) {
                        carAdAdapter.expand(Pair(0, it.first()))
                    }
                    binding.recyclerView.scrollToPosition(0)
                }
            }
        })

        val makesAdapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item,
            arrayOf(this.getString(R.string.any_make)) + DataSource.getDataSource(this.assets)
                .getMakes()
        )
        makesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.filter.filterMake.adapter = makesAdapter
        binding.filter.filterMake.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val selected: String = parent?.getItemAtPosition(pos) as String
                    if (selected == this@MainListActivity.getString(R.string.any_make)) listViewModel.filterMakes(
                        null
                    ) else listViewModel.filterMakes(selected)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        val modelsAdapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item,
            arrayOf(this.getString(R.string.any_model)) + DataSource.getDataSource(this.assets)
                .getModels()
        )
        modelsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.filter.filterModel.adapter = modelsAdapter
        binding.filter.filterModel.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val selected: String = parent?.getItemAtPosition(pos) as String
                    if (selected == this@MainListActivity.getString(R.string.any_model)) listViewModel.filterModel(
                        null
                    ) else listViewModel.filterModel(selected)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        binding.fab.setOnClickListener {
            binding.filter.root.isGone = !binding.filter.root.isGone
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}