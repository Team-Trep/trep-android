package com.jiwondev.trep.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jiwondev.trep.databinding.FragmentFeedBinding

class MapFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // TODO : BaseFragment에서 ViewModel, ViewBinding 상속시킬 것.
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        return binding.root
    }

}