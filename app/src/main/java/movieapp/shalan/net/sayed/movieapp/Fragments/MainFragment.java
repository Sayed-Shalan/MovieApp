package movieapp.shalan.net.sayed.movieapp.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import movieapp.shalan.net.sayed.movieapp.Adapters.GridViewAdapter;
import movieapp.shalan.net.sayed.movieapp.Database.MovieDatabase;
import movieapp.shalan.net.sayed.movieapp.DetailActivity;
import movieapp.shalan.net.sayed.movieapp.MainActivity;
import movieapp.shalan.net.sayed.movieapp.Models.MoviesModel;
import movieapp.shalan.net.sayed.movieapp.R;

public class MainFragment extends Fragment {

    //deceleration for needed views ,classes, and variables
    boolean twobane = false;
    View view;
    GridView gridView;
    GridViewAdapter gridViewAdapter;
    ArrayList<MoviesModel> items = new ArrayList<>();
    String sortType;


    //method for check if the device is connected to internet or no
    public boolean isOnline()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //start on create for this fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main, container, false);

        //check internet connection
        if(!isOnline())
        {
          Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }else {


            //  //check if the device is tablet
            if (isTablet(getActivity())) {
                twobane = true;

            }


            //get Sort type from Default SharedPreference
            sortType = PreferenceManager.getDefaultSharedPreferences(getActivity())
                    .getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default_value));
            //if sort type not favourite
            if (!sortType.equals(getResources().getString(R.string.pref_sort_favourite_value))) {
                FetchMovieTask fetchMovieTask = new FetchMovieTask();
                fetchMovieTask.execute(sortType);
            }

            //if sort type is favourite
            else {
                ArrayList<MoviesModel> favList = new ArrayList<>();

                //get favourite movies from internal database and assign it to favList
                favList = new MovieDatabase(MainActivity.fa).getMovies();

                //Define GridView And set its Adapter
                gridView = (GridView) view.findViewById(R.id.grid_view);
                gridViewAdapter = new GridViewAdapter(MainActivity.fa, favList);
                gridView.setAdapter(gridViewAdapter);

                //add on item Listner for GridView
                setOnItemClickListner();
            }

        }
        return view;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //AsyncTask to upload movie data in background
    public class FetchMovieTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected void onPostExecute(Void aVoid) {


            //Define GridView And set its Adapter
            gridView = (GridView) view.findViewById(R.id.grid_view);
            gridViewAdapter = new GridViewAdapter(MainActivity.fa, items);
            gridView.setAdapter(gridViewAdapter);

            //add on item Listner for GridView
            setOnItemClickListner();


        }

        @Override
        protected Void doInBackground(String... voids) {

            //if there is no sort type , there is no things to look up .
            if (voids.length == 0) {
                return null;
            }

            //connect to api services
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;
            final String urlString = "http://api.themoviedb.org/3/movie/" + voids[0] + "?api_key=f470b65a6a640a7cafd4b4a02dd90807";
            try {

                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // read data from stream
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                /////////////////////////////////////////////
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "/n");
                }
                if (buffer.length() <= 0) {
                    Log.v("Response", "Error to connect");
                    return null;

                }
                movieJsonStr = buffer.toString();
                Log.v(LOG_TAG + "Response", movieJsonStr);

            } catch (MalformedURLException e) {
                Log.v(LOG_TAG + "Response", "Error to connect");


            } catch (IOException e) {
                e.printStackTrace();
                Log.v("Response", "Error to connect second");

            } finally {
                //  Log.e("Response",movieJsonStr);
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    movieDataFormate(movieJsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;

        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //movie data formate function
    private void movieDataFormate(String moviesData) throws JSONException {

        String JSON_ARRAY_NAME = "results";

        String POSTER_PATH = "poster_path";
        String ADULT = "adult";
        String OVER_VIEW = "overview";
        String RELEASED_DATE = "release_date";
        String ID = "id";
        String ORIGINAL_TITLE = "original_title";
        String ORIGINAL_LANGUAGE = "original_language";
        String BACKDROP_PATH = "backdrop_path";
        String POPULARITY = "popularity";
        String VOTE_COUNT = "vote_count";
        String VIDEO = "video";
        String VOTE_AVERAGE = "vote_average";

        JSONObject moviesJsonObject = new JSONObject(moviesData);
        JSONArray allMoviesArray = moviesJsonObject.getJSONArray(JSON_ARRAY_NAME);

        for (int i = 0; i < allMoviesArray.length(); i++) {
            JSONObject singleMovieObject = allMoviesArray.getJSONObject(i);
            MoviesModel model = new MoviesModel();

            model.setId(singleMovieObject.getInt(ID));
            model.setPoster_path(singleMovieObject.getString(POSTER_PATH));
            model.setAdult(singleMovieObject.getBoolean(ADULT));
            model.setOverView(singleMovieObject.getString(OVER_VIEW));
            model.setRelease_date(singleMovieObject.getString(RELEASED_DATE));
            model.setOriginal_title(singleMovieObject.getString(ORIGINAL_TITLE));
            model.setOriginal_language(singleMovieObject.getString(ORIGINAL_LANGUAGE));
            model.setBackdrop_path(singleMovieObject.getString(BACKDROP_PATH));
            model.setPopularity(singleMovieObject.getDouble(POPULARITY));
            model.setVote_count(singleMovieObject.getInt(VOTE_COUNT));
            model.setVideo(singleMovieObject.getBoolean(VIDEO));
            model.setVote_average(singleMovieObject.getDouble(VOTE_AVERAGE));
            model.setInfavourtie(checkIfMovieInFavourite(singleMovieObject.getInt(ID)));
            items.add(model);
        }

    }

    //Define On Item Click Listner Method for GridView
    public void setOnItemClickListner() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MoviesModel data ;
                data = gridViewAdapter.getItem(i);

                //if device not a tablet
                if (!twobane) {

                    Intent intent = new Intent(getActivity(), DetailActivity.class);

                    intent.putExtra("id", data.getId());
                    intent.putExtra("poster_path", data.getPoster_path());
                    intent.putExtra("overview", data.getOverView());
                    intent.putExtra("original_title", data.getOriginal_title());
                    intent.putExtra("vote_average", data.getVote_average());
                    intent.putExtra("release_date", data.getRelease_date());
                    intent.putExtra("is_fav", data.isInfavourtie());
                    startActivity(intent);
                }

                //if device is a tablet
                else {
                    Bundle bundle = new Bundle();
                    DetailFragment detailFragment = new DetailFragment();
                    bundle.putInt("id", data.getId());
                    bundle.putDouble("vote_average", data.getVote_average());
                    bundle.putString("poster_path", data.getPoster_path());
                    bundle.putString("overview", data.getOverView());
                    bundle.putString("original_title", data.getOriginal_title());
                    bundle.putString("release_date", data.getRelease_date());
                    bundle.putBoolean("is_fav", data.isInfavourtie());
                    detailFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().
                            replace(R.id.detail_fragment_frame_tablet, detailFragment).
                            commit();
                }

            }
        });
    }

    //check if movie in favourite or no
    public boolean checkIfMovieInFavourite(int movieId) {

        ArrayList<MoviesModel> listModels;
        listModels = new MovieDatabase(MainActivity.fa).getMovies();
        for (int i = 0; i < listModels.size(); i++) {
            MoviesModel model2;
            model2 = listModels.get(i);
            if (model2.getId() == movieId) {
                return true;
            }
        }

        return false;
    }

    //check for tablet
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
