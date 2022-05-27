package com.example.challenge_seven.presentation.favorite.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.challenge_seven.common.Constant
import com.example.challenge_seven.common.Constant.TAG
import com.example.challenge_seven.databinding.FragmentFavoriteBinding
import com.example.challenge_seven.domain.model.Movie
import com.example.challenge_seven.presentation.adapter.MovieListAdapter
import com.example.challenge_seven.presentation.favorite.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val adapter: MovieListAdapter by lazy { MovieListAdapter(::onClicked) }

    private val viewModel: FavoriteViewModel by viewModels()

    private val args: FavoriteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeQueryResult()
        moveToMovieList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeQueryResult() {
        viewModel.getMovie(args.userId)
        Log.d(TAG, "Favorite UserId -> ${args.userId}")
        viewModel.state.observe(viewLifecycleOwner) { result ->
            showLoading(result.isLoading)
            showMovieList(result.movies ?: emptyList())
            Log.d(Constant.TAG, "Fragment -> ${result.movies}")
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }

    private fun showMovieList(movies: List<Movie>) {
        adapter.submitList(movies)
        binding.movieRecyclerView.adapter = adapter
        binding.movieRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun onClicked(movie: Movie) {
        val direction =
            FavoriteFragmentDirections.actionFavoriteFragmentToDetailMovieFragment(
                movie,
                args.userId
            )
        findNavController().navigate(direction)

        Log.d(Constant.TAG, "userId -> ${args.userId}")

    }

    private fun moveToMovieList() {
        binding.toolbarId.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}