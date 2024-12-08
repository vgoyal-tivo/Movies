package com.example.inshorts.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.inshorts.R;
import com.example.inshorts.data.room.MovieEntity;
import com.example.inshorts.ui.ViewModel.MovieViewModel;
import com.example.inshorts.ui.ViewModel.MovieViewModelFactory;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView moviePoster;
    private TextView movieTitle;
    private TextView movieOverview;
    private ImageButton bookmarkButton;
    private boolean isBookmarked = false;
    MovieEntity movie;
    private MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        movieViewModel = new ViewModelProvider(this, new MovieViewModelFactory(this)).get(MovieViewModel.class);

        moviePoster = findViewById(R.id.movie_poster);
        movieTitle = findViewById(R.id.movie_title);
        movieOverview = findViewById(R.id.movie_overview);
        bookmarkButton = findViewById(R.id.bookmarkBtn);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("movie")) {
            movie = (MovieEntity) intent.getSerializableExtra("movie");
            if (movie != null) {
                displayMovieDetails(movie);
            }
        }

        bookmarkButton.setOnClickListener(v -> {
            isBookmarked = !isBookmarked;
            updateBookmarkButton();
        });
    }

    private void updateBookmarkButton() {
        if (isBookmarked) {
            bookmarkButton.setImageResource(R.drawable.baseline_bookmark_24_fill);
        } else {

            bookmarkButton.setImageResource(R.drawable.ic_bookmark);
        }
        movie.setBookmarked(isBookmarked);
        movieViewModel.bookmarkMovie(movie);
    }

    private void displayMovieDetails(MovieEntity movie) {
        movieTitle.setText(movie.getTitle());
        movieOverview.setText(movie.getOverview());
        String posterUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
        Glide.with(this)
                .load(posterUrl)
                .into(moviePoster);
        isBookmarked = movie.isBookmarked();
        updateBookmarkButton();
    }
}