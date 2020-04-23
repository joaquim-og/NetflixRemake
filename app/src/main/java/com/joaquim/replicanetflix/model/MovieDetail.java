package com.joaquim.replicanetflix.model;

import java.util.List;

public class MovieDetail {

    private final Movie movie;
    private final List<Movie> moviesSimiliar;

    public MovieDetail(Movie movie, List<Movie> moviesSimiliar) {
        this.movie = movie;
        this.moviesSimiliar = moviesSimiliar;
    }

    public Movie getMovie() {
        return movie;
    }

    public List<Movie> getMoviesSimiliar() {
        return moviesSimiliar;
    }
}
