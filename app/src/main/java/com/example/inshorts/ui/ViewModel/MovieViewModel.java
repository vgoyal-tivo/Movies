package com.example.inshorts.ui.ViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inshorts.data.retrofit.Movie;
import com.example.inshorts.data.repository.MovieRepository;
import com.example.inshorts.data.room.MovieEntity;

import java.util.List;

public class MovieViewModel extends ViewModel {
    private final MovieRepository movieRepository;
    private LiveData<List<MovieEntity>> bookmarkedMovies;

    private final MutableLiveData<List<Movie>> trendingMovies = new MutableLiveData<List<Movie>>();

    private final MutableLiveData<List<Movie>> nowPlayingMovies = new MutableLiveData<List<Movie>>();

    private final MutableLiveData<List<Movie>> searchResults = new MutableLiveData<>();

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public MovieViewModel(Context context) {
        movieRepository = new MovieRepository(context);
        bookmarkedMovies = movieRepository.getBookmarkedMovies();
    }

    public void fetchTrendingMovies() {
        movieRepository.fetchTrendingMovies(new MovieRepository.MovieCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                trendingMovies.setValue(movies);
            }

            @Override
            public void onError(String error) {
                errorMessage.setValue(error);
            }
        });
    }

    public void fetchNowPlayingMovies() {
        movieRepository.fetchNowPlayingMovies(new MovieRepository.MovieCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                nowPlayingMovies.setValue(movies);
            }

            @Override
            public void onError(String error) {
                errorMessage.setValue(error);
            }
        });
    }

    public void searchMovies(String query) {
        movieRepository.searchMovies(query, new MovieRepository.MovieCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                searchResults.setValue(movies);
            }

            @Override
            public void onError(String error) {
                errorMessage.setValue(error);
            }
        });
    }

    public LiveData<List<MovieEntity>> getTrendingMovies() {
        return movieRepository.getTrendingMovies();
    }

    public LiveData<List<MovieEntity>> getNowPlayingMovies() {
        return movieRepository.getNowPlayingMovies();
    }

    public LiveData<List<Movie>> getSearchResults() {
        return searchResults;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<List<MovieEntity>> getBookmarkedMovies() {
        return movieRepository.getBookmarkedMovies();
    }

    public void bookmarkMovie(MovieEntity movie) {
        movieRepository.bookmarkMovie(movie);
    }

    public void removeBookmark(MovieEntity movie) {
        movieRepository.removeBookmark(movie);
    }
}

