package com.joaquim.replicanetflix.kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.joaquim.replicanetflix.R
import com.joaquim.replicanetflix.model.Category
import com.joaquim.replicanetflix.model.Movie
import com.joaquim.replicanetflix.util.CategoryTask
import com.joaquim.replicanetflix.util.ImageDownloaderTask
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.category_item.view.*
import kotlinx.android.synthetic.main.movie_item.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val categories = arrayListOf<Category>()
        mainAdapter = MainAdapter(categories)
        recycler_view_main.adapter = mainAdapter
        recycler_view_main.layoutManager = LinearLayoutManager(this)

        val categoryTask = CategoryTask(this)
        categoryTask.setCategoryLoader {
            mainAdapter.categories.clear()
            mainAdapter.categories.addAll(it)
            mainAdapter.notifyDataSetChanged()
        }

        categoryTask.execute("https://tiagoaguiar.co/api/netflix/home")
    }

    private inner class MainAdapter(val categories: MutableList<Category>) : RecyclerView.Adapter<CategoryHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
            val categoryHolder = CategoryHolder(
                    layoutInflater.inflate(R.layout.category_item, parent, false)
            )

            return categoryHolder
        }

        override fun getItemCount(): Int {
            return categories.size
        }

        override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
            val category = categories[position]
            holder.bind(category)
        }
    }


    private inner class MovieAdapter(val movies: MutableList<Movie>, private val listener: ((Movie) -> Unit)?) : RecyclerView.Adapter<MovieHolder>() {

        val onClick: ((Int) -> Unit)? = { position ->

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {

            val movieHolder = MovieHolder(
                    layoutInflater.inflate(R.layout.movie_item, parent, false), listener
            )

            return movieHolder
        }

        override fun getItemCount(): Int {
            return movies.size
        }

        override fun onBindViewHolder(holder: MovieHolder, position: Int) {
            val movie = movies[position]
            holder.bind(movie)
        }

    }

    private inner class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(category: Category) = with(itemView) {
            text_view_title.text = category.name
            recycler_view_movie.adapter = MovieAdapter(category.movies) { movie ->
                if (movie.id > 3) {
                    Toast.makeText(this@MainActivity, "não foi implementada essa funcionalidade", Toast.LENGTH_LONG).show()
                } else {
                    val intent = Intent(this@MainActivity, MovieActivity::class.java)

                    intent.putExtra("id", movie.id)
                    startActivity(intent)
                }
            }
            recycler_view_movie.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
        }
    }

    private class MovieHolder(itemView: View, val onClick: ((Movie) -> Unit)?) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) = with(itemView) {
            Glide.with(context)
                    .load(movie.coverUrl)
                    .placeholder(R.drawable.placheholder_bg)
                    .into(image_view_cover)

            image_view_cover.setOnClickListener {
                onClick?.invoke(movie)
            }
        }
    }

}
