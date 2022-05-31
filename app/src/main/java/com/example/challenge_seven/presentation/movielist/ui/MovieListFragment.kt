package com.example.challenge_seven.presentation.movielist.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.challenge_seven.R
import com.example.challenge_seven.common.Constant.TAG
import com.example.challenge_seven.databinding.FragmentMovieListBinding
import com.example.challenge_seven.domain.model.Movie
import com.example.challenge_seven.domain.model.User
import com.example.challenge_seven.presentation.adapter.MovieListAdapter
import com.example.challenge_seven.presentation.movielist.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListFragment : Fragment() {
    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieListViewModel by viewModels()

    private val adapter: MovieListAdapter by lazy { MovieListAdapter(::onClicked) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMovieListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeQueryResult()
        getUserEmail()
        moveToProfile()
        moveToFavorite()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeQueryResult() {
        viewModel.state.observe(viewLifecycleOwner) { result ->
            showLoading(result.isLoading)
            showMovieList(result.movies)
            Log.d(TAG, "Fragment -> ${result.movies}")
        }
    }

    private fun getUserEmail() {
        viewModel.email.observe(viewLifecycleOwner) { email ->
            viewModel.getUser(email)
        }
        getUserData()
    }

    private fun getUserData() {
        viewModel.userData.observe(viewLifecycleOwner) { result ->
            showUserData(result.user)
        }
    }

    private fun showUserData(user: User?) {
        binding.usernameTextView.text =
            getString(R.string.get_username, user?.username ?: "null")
        binding.profileButton.load(user?.profilePhoto)
    }

    private fun onClicked(movie: Movie) {
        viewModel.userData.observe(viewLifecycleOwner) { result ->
            val direction =
                MovieListFragmentDirections.actionMovieListFragmentToDetailMovieFragment(
                    movie,
                    result.user?.id ?: 0
                )
            findNavController().navigate(direction)

            Log.d(TAG, "userId -> $result.user?.id")
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

    private fun moveToProfile() {
        binding.profileButton.setOnClickListener {
            findNavController().navigate(R.id.action_movieListFragment_to_profileFragment)
        }
    }

    private fun moveToFavorite() {
        viewModel.userData.observe(viewLifecycleOwner) { result ->
            binding.favoriteButton.setOnClickListener {
                val direction =
                    MovieListFragmentDirections.actionMovieListFragmentToFavoriteFragment(
                        result.user?.id ?: 0
                    )
                findNavController().navigate(direction)
            }
        }
    }
}