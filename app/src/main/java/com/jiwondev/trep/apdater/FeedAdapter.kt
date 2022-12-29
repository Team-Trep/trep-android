package com.jiwondev.trep.apdater

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.jiwondev.trep.R
import com.jiwondev.trep.databinding.FeedItemBinding
import com.jiwondev.trep.fragments.FeedFragment

// TODO : Pager 라이브러리 사용

class FeedAdapter(
    private val feedList: ArrayList<String>,
    private val fragment: FeedFragment
    ) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder =
        FeedViewHolder(FeedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bind((feedList[position]))
    }

    override fun getItemCount() = feedList.size

    inner class FeedViewHolder(private val binding: FeedItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val exoplayer = binding.exoPlayer

        fun bind(url: String) {
            val player = SimpleExoPlayer.Builder(fragment.requireContext()).build()
            val mediaItem = MediaItem.fromUri(Uri.parse(url))

            player.apply {
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true
            }

            exoplayer.player = player
        }
    }
}
