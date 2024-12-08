package com.example.inshorts.ui.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inshorts.R;
import com.example.inshorts.data.room.MovieEntity;
import com.example.inshorts.ui.MovieDetailsActivity;
import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<MovieEntity> movies = new ArrayList<>();
    private OnBookmarkClickListener onBookmarkClickListener;
    private boolean removeBookMark = false;

    public void setOnBookmarkClickListener(OnBookmarkClickListener listener) {
        this.onBookmarkClickListener = listener;
    }

    public void setRemoveBookMark(boolean removeBookMark) {
        this.removeBookMark = removeBookMark;
    }

    public interface OnBookmarkClickListener {
        void onBookmarkClick(MovieEntity movie);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieEntity movieEntity = movies.get(position);
        holder.titleTextView.setText(movieEntity.getTitle());
        if (removeBookMark) {
            holder.bookmarkButton.setVisibility(View.GONE);
        }
        if (movieEntity.isBookmarked()) {
            holder.bookmarkButton.setImageResource(R.drawable.baseline_bookmark_24_fill);
        } else {
            holder.bookmarkButton.setImageResource(R.drawable.ic_bookmark);
        }

        holder.bookmarkButton.setOnClickListener(v -> {
            if (onBookmarkClickListener != null) {
                Log.d("HomeFragment", "Bookmark clicked for movie 123: " + movieEntity.getTitle());

                onBookmarkClickListener.onBookmarkClick(movieEntity);
            }
        });
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MovieDetailsActivity.class);
            intent.putExtra("movie", movieEntity);
            v.getContext().startActivity(intent);
        });
        Glide.with(holder.posterImageView.getContext())
                .load("https://image.tmdb.org/t/p/w500" + movieEntity.getPosterPath())
                .into(holder.posterImageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setMovies(List<MovieEntity> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public void setErrorMessage(String message) {
        this.movies.clear();
        notifyDataSetChanged();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView posterImageView;
        ImageButton bookmarkButton;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textViewTitle);
            posterImageView = itemView.findViewById(R.id.imageViewPoster);
            bookmarkButton = itemView.findViewById(R.id.bookmarkButton);
        }
    }
}

