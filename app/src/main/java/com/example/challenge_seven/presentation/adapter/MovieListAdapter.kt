package com.example.challenge_seven.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.challenge_seven.common.Constant
import com.example.challenge_seven.databinding.ItemContainerBinding
import com.example.challenge_seven.domain.model.Movie
import com.example.challenge_seven.utils.Extension.loadImage

class MovieListAdapter(private val onClick: (Movie) -> Unit) :
    ListAdapter<Movie, MovieListAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.apply {
                titleTextView.text = movie.title
                ratingTextView.text = movie.voteAverage.toString()
                popularityTextView.text = movie.popularity.toString()
                itemView.context.loadImage(
                    Constant.BASE_IMAGE_URL + movie.posterPath,
                    binding.posterImageView
                )
                root.setOnClickListener { onClick(movie) }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListAdapter.ViewHolder {
        val binding =
            ItemContainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieListAdapter.ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int = currentList.size

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }
}