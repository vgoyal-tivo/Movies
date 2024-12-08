package com.example.inshorts.data.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovies(List<MovieEntity> movieEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(MovieEntity movie);

    @Update
    void updateMovie(MovieEntity movie);

    // Retrieve all movies of a specific type (Trending or Now Playing)
    @Query("SELECT * FROM movies WHERE movieType = :movieType")
    LiveData<List<MovieEntity>> getMoviesByType(String movieType);

    // Retrieve all movies
    @Query("SELECT * FROM movies")
    LiveData<List<MovieEntity>> getAllMovies();

    // Optionally, if you want to check if a movie already exists in the database
    @Query("SELECT COUNT(*) FROM movies WHERE id = :id")
    int checkIfMovieExists(int id);

    @Query("SELECT * FROM movies WHERE isBookmarked = 1")
    LiveData<List<MovieEntity>> getAllBookmarkedMovies();

    @Delete
    void delete(MovieEntity movie);

}

