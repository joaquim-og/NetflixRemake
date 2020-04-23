package com.joaquim.replicanetflix.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.joaquim.replicanetflix.R;
import com.joaquim.replicanetflix.model.Category;
import com.joaquim.replicanetflix.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class CategoryTask extends AsyncTask<String, Void, List<Category>> {

    private final WeakReference<Context> context;
    private ProgressDialog dialog;
    private CategoryLoader categoryLoader;

    public CategoryTask(Context context) {
        this.context = new WeakReference<>(context);
    }

    public void setCategoryLoader(CategoryLoader categoryLoader) {
        this.categoryLoader = categoryLoader;
    }

    // thread - main
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Context context = this.context.get();

        if (context != null)
            dialog = ProgressDialog.show(context, "Carregando", "", true);
    }

    // thread - background
    @Override
    protected List<Category> doInBackground(String... params) {
        // catching parameters from "MainActivity"  - CategoryTask
        String url = params[0];

        try {
            URL requestUrl = new URL(url);

            //opening connection
            HttpsURLConnection urlConnection = (HttpsURLConnection) requestUrl.openConnection();

            //setting max time of each action
            urlConnection.setReadTimeout(20000);
            urlConnection.setConnectTimeout(20000);

            //catching server response
            int responseCode = urlConnection.getResponseCode();

            //manipulating server response. Greater than 400 raise server error
            if (responseCode > 400) {
                throw new IOException();
            }

            //if server ok, read and manipulate all data
            InputStream inputStream = urlConnection.getInputStream();

            //allocate memory for received data
            BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());

            //converting data(JSON WEB) in string
            String jsonAsString = toString(in);

            List<Category> categories = getCategories(new JSONObject(jsonAsString));
            in.close();
            return categories;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    // thread - main

    @Override
    protected void onPostExecute(List<Category> categories) {
        super.onPostExecute(categories);
        dialog.dismiss();

        //listener
        if (categoryLoader != null)
            categoryLoader.onResult(categories);

    }

    //converting all characters in strings
    private String toString(BufferedInputStream is) throws IOException {
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int lidos;

        while ((lidos = is.read(bytes)) > 0) {
            baos.write(bytes, 0 , lidos);
        }
        return new String(baos.toByteArray());
    }


    private List<Category> getCategories(JSONObject jsonObject) throws JSONException {

        //finally, catching data and putting this to List

        //categories
        List<Category> categories = new ArrayList<>();
        JSONArray categoryArray = jsonObject.getJSONArray("category");
        for (int i = 0; i < categoryArray.length() ; i++) {
            JSONObject category= categoryArray.getJSONObject(i);
            String title = category.getString("title");

            //movies
            List<Movie> movies = new ArrayList<>();
            JSONArray movieArray = category.getJSONArray("movie");
            for (int j = 0; j < movieArray.length() ; j++) {
                JSONObject movie = movieArray.getJSONObject(j);

               String coverUrl =  movie.getString("cover_url");
                Movie movieObj = new Movie();
                movieObj.setCoverUrl(coverUrl);

                movies.add(movieObj);
            }
            Category categoryObj = new Category();
            categoryObj.setName(title);
            categoryObj.setMovies(movies);

            categories.add(categoryObj);
        }

        return categories;

    }

    public interface CategoryLoader {
        void onResult(List<Category> categories);
    }
}
