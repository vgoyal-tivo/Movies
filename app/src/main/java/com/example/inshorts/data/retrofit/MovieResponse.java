package com.example.inshorts.data.retrofit;

import java.util.List;

public class MovieResponse {
    private int page;
    private List<Movie> results;

    public int getPage() {
        return page;
    }

    public List<Movie> getResults() {
        return results;
    }
}
