package com.joaquim.replicanetflix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joaquim.replicanetflix.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerview = findViewById(R.id.recycler_view_main);

        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Movie movie = new Movie();
            movie.setCoverUrl("abc " + i);
            movies.add(movie);
        }

        mainAdapter = new MainAdapter(movies);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerview.setAdapter(mainAdapter);

    }

    private static class MovieHolder extends RecyclerView.ViewHolder {

        private final TextView textViewUrl;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            textViewUrl = itemView.findViewById(R.id.text_view_url);
        }
    }

    private class MainAdapter extends RecyclerView.Adapter<MovieHolder> {

        private final List<Movie> movies;

        private MainAdapter(List<Movie> movies) {
            this.movies = movies;
        }

        @NonNull
        @Override
        public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            MovieHolder movieHolder = new MovieHolder(getLayoutInflater().inflate(R.layout.movie_item, parent, false));
            return movieHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
            Movie movie = movies.get(position);
            holder.textViewUrl.setText(movie.getCoverUrl());
        }

        @Override
        public int getItemCount() {

            return movies.size();
        }
    }

}
