package com.jiwondev.trep.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.jiwondev.trep.R

abstract class BaseFragment<T: ViewBinding>(
    private val bindingFactory: (LayoutInflater, ViewGroup?, Boolean) -> T
) : Fragment() {
    private var _binding: T? = null
    val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = bindingFactory(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}