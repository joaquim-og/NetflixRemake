package com.joaquim.replicanetflix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joaquim.replicanetflix.model.Category;
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

        //generating fake data
        ArrayList<Category> categories = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Category category = new Category();

            category.setName("cat " + i);

            List<Movie> movies = new ArrayList<>();
            for (int j = 0; j < 30; j++) {
                Movie movie = new Movie();
//                movie.setCoverUrl(R.drawable.movie);
                movies.add(movie);
            }

            category.setMovies(movies);
            categories.add(category);

        }

        //setting adapter and layout
        mainAdapter = new MainAdapter(categories);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerview.setAdapter(mainAdapter);

    }

    //setting Holders
    private static class MovieHolder extends RecyclerView.ViewHolder {

        private final ImageView imageViewCover;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCover = itemView.findViewById(R.id.image_view_cover);
        }
    }

    private static class CategoryHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        RecyclerView recyclerViewMovie;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            recyclerViewMovie = itemView.findViewById(R.id.recycler_view_movie);
        }
    }

    //Category adapter for its data
    private class MainAdapter extends RecyclerView.Adapter<CategoryHolder> {

        private final List<Category> categories;

        private MainAdapter(List<Category> categories) {
            this.categories = categories;
        }

        @NonNull
        @Override
        public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            CategoryHolder categoryHolder = new CategoryHolder(getLayoutInflater().inflate(R.layout.category_item, parent, false));
            return categoryHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
            Category category = categories.get(position);
            holder.textViewTitle.setText(category.getName());
            //creating movie adapter within each category
            holder.recyclerViewMovie.setAdapter(new MovieAdapter(category.getMovies()));
            //inflating new vertical layout for each category list with its movies
            holder.recyclerViewMovie.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.HORIZONTAL, false));
        }

        @Override
        public int getItemCount() {

            return categories.size();
        }
    }

    //Movie adapter for its data
    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {

        private final List<Movie> movies;

        private MovieAdapter(List<Movie> movies) {
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
//            holder.imageViewCover.setImageResource(movie.getCoverUrl());
        }

        @Override
        public int getItemCount() {

            return movies.size();
        }
    }

}
