package com.example.inshorts.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inshorts.R;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inshorts.data.room.MovieEntity;
import com.example.inshorts.ui.ViewModel.MovieViewModel;
import com.example.inshorts.ui.ViewModel.MovieViewModelFactory;
import com.example.inshorts.ui.adapters.MovieAdapter;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewTrending;
    private RecyclerView recyclerViewNowPlaying;
    private TextView errorMessageTextView;
    private MovieViewModel movieViewModel;

    private MovieAdapter trendingAdapter = new MovieAdapter();
    private MovieAdapter nowPlayingAdapter = new MovieAdapter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerViewTrending = view.findViewById(R.id.recyclerTrendingMovies);
        recyclerViewNowPlaying = view.findViewById(R.id.recyclerNowPlayingMovies);
        errorMessageTextView = view.findViewById(R.id.textErrorMessage);

        movieViewModel = new ViewModelProvider(this, new MovieViewModelFactory(getContext())).get(MovieViewModel.class);


        recyclerViewTrending.setLayoutManager(new LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL, false));
        recyclerViewTrending.setAdapter(trendingAdapter);

        recyclerViewNowPlaying.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        recyclerViewNowPlaying.setAdapter(nowPlayingAdapter);

        trendingAdapter.setOnBookmarkClickListener(this::bookmarkedMovies);
        nowPlayingAdapter.setOnBookmarkClickListener(this::bookmarkedMovies);
        return view;
    }

    private void  setData() {
        if (movieViewModel.getTrendingMovies().getValue() == null) {
            movieViewModel.fetchTrendingMovies();
        }
        if (movieViewModel.getNowPlayingMovies().getValue() == null) {
            movieViewModel.fetchNowPlayingMovies();
        }
        movieViewModel.getTrendingMovies().observe(getViewLifecycleOwner(), movies -> {
            trendingAdapter.setMovies(movies);
        });

        movieViewModel.getNowPlayingMovies().observe(getViewLifecycleOwner(), movies -> {
            nowPlayingAdapter.setMovies(movies);
        });

        movieViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            errorMessageTextView.setVisibility(View.VISIBLE);
            errorMessageTextView.setText(error);
        });
    }

    private void  bookmarkedMovies(MovieEntity movie) {
        var isValue = movie.isBookmarked();
        movie.setBookmarked(!isValue);
        movieViewModel.bookmarkMovie(movie);
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }
}
