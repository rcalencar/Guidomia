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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rcalencar.guidomia.R
import com.rcalencar.guidomia.databinding.FragmentCarAdListBinding
import com.rcalencar.guidomia.model.CarAd
import com.rcalencar.guidomia.model.DataSource
import com.rcalencar.guidomia.viewmodel.CarAdListViewModel
import com.rcalencar.guidomia.viewmodel.CarAdViewModel
import com.rcalencar.guidomia.viewmodel.ListViewModelFactory

class CarAdListFragment : Fragment() {

    private var _binding: FragmentCarAdListBinding? = null
    private val binding get() = _binding!!

    private val carAdViewModel: CarAdViewModel by activityViewModels()
    private val carAdListViewModel by viewModels<CarAdListViewModel> { ListViewModelFactory(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCarAdListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val carAdAdapter = CarAdAdapter {
                item -> carAdViewModel.selectItem(item)
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
            arrayOf(this.getString(R.string.any_make)) + DataSource.getDataSource(requireActivity().assets)
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
            arrayOf(this.getString(R.string.any_model)) + DataSource.getDataSource(requireActivity().assets)
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
                    if (selected == this@CarAdListFragment.getString(R.string.any_model)) carAdListViewModel.filterModel(
                        null
                    ) else carAdListViewModel.filterModel(selected)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}