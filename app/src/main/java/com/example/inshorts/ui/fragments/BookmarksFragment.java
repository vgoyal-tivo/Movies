package com.example.inshorts.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inshorts.R;
import com.example.inshorts.ui.ViewModel.MovieViewModel;
import com.example.inshorts.ui.ViewModel.MovieViewModelFactory;
import com.example.inshorts.ui.adapters.MovieAdapter;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;

public class BookmarksFragment extends Fragment {
    private RecyclerView bookmarkedMoviesRecyclerView;
    private MovieAdapter bookmarkedMoviesAdapter;
    private MovieViewModel movieViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        bookmarkedMoviesRecyclerView = view.findViewById(R.id.recyclerBookmarkedMovies);
        bookmarkedMoviesAdapter = new MovieAdapter();
        bookmarkedMoviesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        bookmarkedMoviesRecyclerView.setAdapter(bookmarkedMoviesAdapter);

        movieViewModel = new ViewModelProvider(this, new MovieViewModelFactory(getContext())).get(MovieViewModel.class);

        movieViewModel.getBookmarkedMovies().observe(getViewLifecycleOwner(), movies -> {
            bookmarkedMoviesAdapter.setMovies(movies);
        });

        return view;
    }
}
