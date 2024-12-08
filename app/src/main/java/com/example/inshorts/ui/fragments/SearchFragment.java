package com.example.inshorts.ui.fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inshorts.R;
import com.example.inshorts.data.retrofit.Movie;
import com.example.inshorts.data.room.MovieEntity;
import com.example.inshorts.ui.ViewModel.MovieViewModel;
import com.example.inshorts.ui.ViewModel.MovieViewModelFactory;
import com.example.inshorts.ui.adapters.MovieAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment {

    private EditText searchInput;
    private ImageView searchIcon;
    private RecyclerView searchResultsRecyclerView;
    private MovieAdapter searchAdapter;
    private MovieViewModel movieViewModel;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private boolean isBookmarked = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchInput = view.findViewById(R.id.searchInput);
        searchIcon = view.findViewById(R.id.search_icon);
        searchIcon.setOnClickListener(v -> {

            final InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);

        });
        searchResultsRecyclerView = view.findViewById(R.id.recyclerSearchResults);

        searchAdapter = new MovieAdapter();
        searchAdapter.setRemoveBookMark(true);
        searchResultsRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(),2));

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable);
                }
                searchRunnable = () -> movieViewModel.searchMovies(s.toString().trim());
                handler.postDelayed(searchRunnable, 500); // Debounce for 500ms
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        searchAdapter.setOnBookmarkClickListener(movie -> {
            bookmarkedMovies(movie);
        });
        return view;
    }

    private void setData() {
        searchResultsRecyclerView.setAdapter(searchAdapter);

        movieViewModel = new ViewModelProvider(this, new MovieViewModelFactory(getContext())).get(MovieViewModel.class);

        movieViewModel.getSearchResults().observe(getViewLifecycleOwner(), movies -> {
            List<MovieEntity> movieEntities = new ArrayList<>();
            for (Movie movie : movies) {
                movieEntities.add(new MovieEntity(movie.getId(), movie.getTitle(), movie.getOverview(), movie.getPosterPath(), "Now Playing"));
            }
            searchAdapter.setMovies(movieEntities);
        });
        movieViewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            searchAdapter.setErrorMessage(message);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    private void  bookmarkedMovies(MovieEntity movie) {
        var isValue = movie.isBookmarked();
        movie.setBookmarked(!isValue);
        movieViewModel.bookmarkMovie(movie);
    }
}
