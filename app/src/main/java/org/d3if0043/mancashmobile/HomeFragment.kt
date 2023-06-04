package org.d3if0043.mancashmobile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import org.d3if0043.mancashmobile.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        val sort = resources.getStringArray(R.array.sorting)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, sort)
        binding.autoCompleteTextView5.setAdapter(arrayAdapter)
        return binding.root
    }
}