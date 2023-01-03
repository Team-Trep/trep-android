package com.jiwondev.trep.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jiwondev.trep.apdater.FeedAdapter
import com.jiwondev.trep.databinding.FragmentFeedBinding

class FeedFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // TODO : BaseFragment에서 ViewModel, ViewBinding 상속시킬 것.
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        val testList: ArrayList<String> = arrayListOf(
            "352e8c5d-6382-4977-8daa-368c02643337.mp4",
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
        )

        binding.feedViewPager.adapter = FeedAdapter(feedList = testList, fragment = this)
        return binding.root
    }

}