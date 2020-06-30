package com.joaquim.replicanetflix.kotlin

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.Target
import com.joaquim.replicanetflix.R
import com.joaquim.replicanetflix.model.Movie
import com.joaquim.replicanetflix.util.ImageDownloaderTask
import com.joaquim.replicanetflix.util.MovieDetailTask
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.movie_item.view.*

class MovieActivity : AppCompatActivity() {

    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        intent.extras?.let {
            val id = it.getInt("id")
            val task = MovieDetailTask(this)
            task.setMovieDetailLoader {movieDetail ->
                text_view_title.text = movieDetail.movie.title
                text_view_desc.text = movieDetail.movie.desc
                text_view_cast.text = getString(R.string.cast ,movieDetail.movie.cast)

                Glide.with(this)
                        .load(movieDetail.movie.coverUrl)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                return true
                            }

                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                val drawable: LayerDrawable? = ContextCompat.getDrawable(baseContext, R.drawable.shadows) as LayerDrawable?
                                drawable?.let {
                                    drawable.setDrawableByLayerId(R.id.cover_drawable, resource)
                                    (target as DrawableImageViewTarget).view.setImageDrawable(drawable)
                                }
                                return true
                            }

                        }).into(image_view_cover)
                
                movieAdapter.movies.clear()
                movieAdapter.movies.addAll(movieDetail.moviesSimiliar)
                movieAdapter.notifyDataSetChanged()

            }
            task.execute("https://tiagoaguiar.co/api/netflix/$id")

            setSupportActionBar(toolbar)
            supportActionBar?.let { toolbar ->
                toolbar.setDisplayHomeAsUpEnabled(true)
                toolbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
                toolbar.title = null
            }

            val movies = arrayListOf<Movie>()
            movieAdapter = MovieAdapter(movies)
            recycler_view_similar.adapter = movieAdapter
            recycler_view_similar.layoutManager = GridLayoutManager(this, 3)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private inner class MovieAdapter( val movies: MutableList<Movie>) : RecyclerView.Adapter<MovieHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder = MovieHolder(layoutInflater.inflate(R.layout.movie_item_similar, parent, false))

        override fun getItemCount(): Int {
            val size = movies.size
            return size
        }

        override fun onBindViewHolder(holder: MovieHolder, position: Int) = holder.bind(movies[position])
    }


    private class MovieHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            with(itemView) {
                Glide.with(context)
                        .load(movie.coverUrl)
                        .placeholder(R.drawable.placheholder_bg)
                        .into(image_view_cover)
            }
        }
    }

}