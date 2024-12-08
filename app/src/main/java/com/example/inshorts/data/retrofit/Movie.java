package com.example.inshorts.data.retrofit;

public class Movie {
    private int id;
    private String title;
    private String overview;
    private String poster_path;
    private String release_date;

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public int getId() {
        return id;
    }
}
