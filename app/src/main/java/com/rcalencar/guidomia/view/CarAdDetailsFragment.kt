package com.rcalencar.guidomia.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.rcalencar.guidomia.databinding.FragmentCarAdDetailsBinding
import com.rcalencar.guidomia.model.CarAd
import com.rcalencar.guidomia.viewmodel.CarAdListViewModel

class CarAdDetailsFragment : Fragment(), Observer<CarAd> {
    private val carAdListViewModel: CarAdListViewModel by activityViewModels()

    private var _binding: FragmentCarAdDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarAdDetailsBinding.inflate(inflater, container, false)

        carAdListViewModel.selectedItem.observe(requireActivity(), this)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        carAdListViewModel.selectedItem.removeObserver(this)
        _binding = null
    }

    override fun onChanged(item: CarAd?) {
        item?.let {
            binding.carAdImage.setImageAssets(requireContext(), item.image())
        }
    }
}