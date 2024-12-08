package com.example.inshorts.data.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "movies")
public class MovieEntity implements Serializable {
    @PrimaryKey
    private int id;
    private String title;
    private String overview;
    private String posterPath;

    private boolean isBookmarked = false;
    private String movieType;

    public MovieEntity(int id, String title, String overview, String posterPath, String movieType) {
        this(id, title, overview, posterPath, movieType, false); // Calls the other constructor
    }

    public MovieEntity(int id, String title, String overview, String posterPath, String movieType, boolean bookmark) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.movieType = movieType;
        this.isBookmarked = bookmark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getMovieType() {
        return movieType;
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }
}

