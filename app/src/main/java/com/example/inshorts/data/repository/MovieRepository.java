package com.example.inshorts.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.inshorts.data.retrofit.Movie;
import com.example.inshorts.data.retrofit.MovieResponse;
import com.example.inshorts.data.retrofit.RetrofitClient;
import com.example.inshorts.data.retrofit.TmdbApiService;
import com.example.inshorts.data.room.MovieDatabase;
import com.example.inshorts.data.room.MovieEntity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private static final String API_KEY = "b8a9285be14f32f20cf08678cec0c0d8";
    private final TmdbApiService apiService;
    private final String TAG = "MovieRepository";
    private final MovieDatabase movieDatabase;

    public MovieRepository(Context context) {
        apiService = RetrofitClient.getRetrofitInstance().create(TmdbApiService.class);
        movieDatabase = Room.databaseBuilder(context, MovieDatabase.class, "movie_database")
                .addMigrations(MovieDatabase.MIGRATION_1_2)
                .build();
    }

    public interface MovieCallback {
        void onSuccess(List<Movie> movies);
        void onError(String error);
    }

    public void fetchTrendingMovies(MovieCallback movieCallback) {
        LiveData<List<MovieEntity>> list = movieDatabase.movieDao().getMoviesByType("Trending");
        if(list!=null) {
            return;
        }
        Call<MovieResponse> call = apiService.getTrendingMovies(API_KEY);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getResults();
                    movieCallback.onSuccess(movies);

                    List<MovieEntity> movieEntities = new ArrayList<>();
                    for (Movie movie : movies) {
                        movieEntities.add(new MovieEntity(movie.getId(), movie.getTitle(), movie.getOverview(), movie.getPosterPath(), "Trending"));
                    }

                    new Thread(() -> movieDatabase.movieDao().insertMovies(movieEntities)).start();
                } else {
                    movieCallback.onError("Failed to fetch trending movies. Error: " + response.errorBody());
                    Log.e(TAG, "Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                movieCallback.onError(t.getMessage());
                Log.e(TAG, "API call failed: " + t.getMessage());
            }
        });
    }

    public void fetchNowPlayingMovies(MovieCallback movieCallback) {
        LiveData<List<MovieEntity>> list = movieDatabase.movieDao().getMoviesByType("Now Playing");
        if(list!=null) {
            return;
        }
        Call<MovieResponse> call = apiService.getNowPlayingMovies(API_KEY);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getResults();
                    movieCallback.onSuccess(movies);

                    List<MovieEntity> movieEntities = new ArrayList<>();
                    for (Movie movie : movies) {
                        movieEntities.add(new MovieEntity(movie.getId(), movie.getTitle(), movie.getOverview(), movie.getPosterPath(), "Now Playing"));
                    }

                    new Thread(() -> movieDatabase.movieDao().insertMovies(movieEntities)).start();
                } else {
                    movieCallback.onError("Failed to fetch now playing movies. Error: " + response.errorBody());
                    Log.e(TAG, "Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                movieCallback.onError(t.getMessage());
                Log.e(TAG, "API call failed: " + t.getMessage());
            }
        });
    }

    public void searchMovies(String query, MovieCallback movieCallback) {
        Call<MovieResponse> call = apiService.searchMovies(API_KEY, query);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getResults();
                    movieCallback.onSuccess(movies);

                    for (Movie movie : movies) {

                    }


                    if (movies.isEmpty()) {
                        Log.d(TAG, "No movies found for query: " + query);
                    } else {
                        for (Movie movie : movies) {
                            Log.d(TAG,"Search Result: " + movie.getTitle());
                        }
                    }
                } else {
                    movieCallback.onError("Failed to search movies. Error: " + response.errorBody());
                    Log.e(TAG,"Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                movieCallback.onError(t.getMessage());
                Log.e(TAG,"API call failed: " + t.getMessage());
            }
        });
    }

    public LiveData<List<MovieEntity>> getTrendingMovies() {
        return movieDatabase.movieDao().getMoviesByType("Trending");
    }

    public LiveData<List<MovieEntity>> getBookmarkedMovies() {
        return movieDatabase.movieDao().getAllBookmarkedMovies();
    }

    public LiveData<List<MovieEntity>> getNowPlayingMovies() {
        return movieDatabase.movieDao().getMoviesByType("Now Playing");
    }



    public void bookmarkMovie(MovieEntity movie) {
        new Thread(() -> movieDatabase.movieDao().updateMovie(movie)).start();
    }

    public void removeBookmark(MovieEntity movie) {
        new Thread(() -> movieDatabase.movieDao().delete(movie)).start();
    }
}

