package com.example.challenge_seven.presentation.detailmovie.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.challenge_seven.R
import com.example.challenge_seven.common.Constant.BASE_IMAGE_URL
import com.example.challenge_seven.common.Constant.TAG
import com.example.challenge_seven.databinding.FragmentDetailMovieBinding
import com.example.challenge_seven.domain.model.Detail
import com.example.challenge_seven.presentation.detailmovie.DetailMovieViewModel
import com.example.challenge_seven.utils.Extension.loadImage
import com.example.challenge_seven.utils.Extension.showLongToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailMovieFragment : Fragment() {
    private var _binding: FragmentDetailMovieBinding? = null
    private val binding get() = _binding!!

    private val args: DetailMovieFragmentArgs by navArgs()

    private val viewModel: DetailMovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailMovieBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "Movie id args -> ${args.movie.id}")
        val movieId = args.movie.id
        val userId = args.userId
        observeFavoriteMovie(userId, movieId)
        getMovieByID(movieId)
        moveToMovieList()
        addToFavoriteMovie()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeFavoriteMovie(userId: Int, movieId: Int) {
        viewModel.observeFavoriteMovie(userId, movieId)
    }

    private fun getMovieByID(movieId: Int) {
        viewModel.getMovie(movieId)
        observeQueryResult()
    }

    private fun observeQueryResult() {
        viewModel.state.observe(viewLifecycleOwner) { result ->
            showLoading(result.isLoading)
            showMovie(result.movie)
            showErrorLog(result.error)
            Log.d(TAG, "Detail Fragment -> ${result.movie}")
        }
    }

    private fun showErrorLog(message: String) {
        Log.d(TAG, "Detail Error -> $message")
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }

    private fun showMovie(movie: Detail?) {
        binding.apply {
            val ratingText = getString(R.string.get_rating, movie?.voteAverage.toString())
            val durationText = getString(R.string.get_duration, movie?.runtime.toString())
            titleTextView.text = movie?.title
            ratingTextView.text = ratingText
            movieDurationTextView.text = durationText
            movieLanguageTextView.text = movie?.originalLanguage
            movieReleaseDateTextView.text = movie?.releaseDate
            overviewTextView.text = movie?.overview
            requireContext().loadImage(BASE_IMAGE_URL + movie?.posterPath, binding.posterImageView)
        }
    }

    private fun addToFavoriteMovie() {
        viewModel.favoriteMovie.observe(viewLifecycleOwner) { result ->
            val isFavorite = result?.movieId == args.movie.id
            binding.toggleFavorite.setOnClickListener {
                if (result == null) {
                    args.movie.userId = args.userId
                    viewModel.addFavoriteMovie(args.movie)
                    requireContext().showLongToast("Berhasil menambahkan ke Favorite")
                } else {
                    viewModel.deleteFavoriteMovie(args.userId, result.id)
                    requireContext().showLongToast("Berhasil menghapus Favorite")
                }
            }
            binding.toggleFavorite.isChecked = isFavorite
        }
    }

    private fun moveToMovieList() {
        binding.toolbarId.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}