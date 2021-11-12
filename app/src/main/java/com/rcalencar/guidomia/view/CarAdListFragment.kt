package com.rcalencar.guidomia.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.rcalencar.guidomia.GuidomiaApplication
import com.rcalencar.guidomia.R
import com.rcalencar.guidomia.databinding.FragmentCarAdListBinding
import com.rcalencar.guidomia.model.CarAd
import com.rcalencar.guidomia.viewmodel.CarAdListViewModel
import kotlinx.coroutines.launch

class CarAdListFragment : Fragment() {
    private var _binding: FragmentCarAdListBinding? = null
    private val binding get() = _binding!!

    private val carAdListViewModel: CarAdListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarAdListBinding.inflate(inflater, container, false)

        val repository = (requireActivity().application as GuidomiaApplication).repository

        Glide.with(this)
            .load(getString(R.string.ad_url))
            .placeholder(R.drawable.ic_launcher_foreground)
            .fitCenter()
            .into(binding.topAdBanner.bannerAdImage);

        val carAdAdapter = CarAdAdapter {
                item -> carAdListViewModel.selectItem(item)
            findNavController().navigate(R.id.action_List_to_Detail)
        }
        binding.recyclerView.adapter = carAdAdapter
        val decorator = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        ContextCompat.getDrawable(requireActivity(), R.drawable.divider)?.let {
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

        carAdListViewModel.liveData.observe(requireActivity(), {
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
            requireActivity(),
            R.layout.spinner_item_simple,
            mutableListOf(this.getString(R.string.any_make))
        )

        lifecycleScope.launch {
            val queryResult = repository.getMakes()
            makesAdapter.addAll(queryResult)
        }

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
                    if (selected == this@CarAdListFragment.getString(R.string.any_make)) carAdListViewModel.filterMakes(
                        null
                    ) else carAdListViewModel.filterMakes(selected)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        val modelsAdapter = ArrayAdapter(
            requireActivity(),
            R.layout.spinner_item_simple,
            mutableListOf(this.getString(R.string.any_model))
        )

        lifecycleScope.launch {
            val queryResult = repository.getModels()
            modelsAdapter.addAll(queryResult)
        }

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
                    if (selected == this@CarAdListFragment.getString(R.string.any_model)) carAdListViewModel.filterModel(
                        null
                    ) else carAdListViewModel.filterModel(selected)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        return binding.root
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}