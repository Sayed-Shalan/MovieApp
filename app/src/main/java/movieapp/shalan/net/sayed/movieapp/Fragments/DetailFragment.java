package movieapp.shalan.net.sayed.movieapp.Fragments;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

import movieapp.shalan.net.sayed.movieapp.Adapters.RecyclerViewOnItemClickListner;
import movieapp.shalan.net.sayed.movieapp.Adapters.ReviewsRecyclerView;
import movieapp.shalan.net.sayed.movieapp.Adapters.TrailerRecyclerView;
import movieapp.shalan.net.sayed.movieapp.Database.MovieDatabase;
import movieapp.shalan.net.sayed.movieapp.DetailActivity;
import movieapp.shalan.net.sayed.movieapp.Models.MoviesModel;
import movieapp.shalan.net.sayed.movieapp.Models.PeopleReviewsModel;
import movieapp.shalan.net.sayed.movieapp.Models.TrailerModel;
import movieapp.shalan.net.sayed.movieapp.R;

public class DetailFragment extends Fragment {

    //deceleration of needed views ,classes, and variables
    RecyclerView reviewsRecycler;
    ReviewsRecyclerView reviewsAdapter;
    RecyclerView trailerRecycler;
    TrailerRecyclerView trailerAdapter;
    Bundle bundle ;
    TextView movieTitle;
    ImageView posterPath;
    TextView releaseDate;
    TextView overview;
    RatingBar votingRate;
    TextView ratingTxt;
    ImageView favBtn;
    View view;

//////////////////////////////////////////////////////////////////////////////////////////////////
    //start on create for this fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view=inflater.inflate(R.layout.fragment_detail, container, false);

        //get Movie data in bundle
        bundle=new Bundle();
        bundle= getArguments();

        //call method for binding static data method
        bindMovieStaticData();

        //call method for on favourite button click
        onFavBtnListner();

        FetchTrailerTask fetchTrailerTask = new FetchTrailerTask();
        fetchTrailerTask.execute(bundle.getInt("id"));

        FetchReviewsTask fetchReviewsTask = new FetchReviewsTask();
        fetchReviewsTask.execute(bundle.getInt("id"));

        return view;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////
    public class FetchTrailerTask extends AsyncTask<Integer, Void, ArrayList<TrailerModel>>
    {
        private final String LOG_TAG = FetchTrailerTask.class.getSimpleName();

        @Override
        protected void onPostExecute(ArrayList<TrailerModel> trailerModels) {

            //check if this movie has trailers
            if(trailerModels.size()==0)
            {
                return ;
            }

            //define recyclerView of the trailer and its adapter
            trailerRecycler =(RecyclerView)view.findViewById(R.id.trailer_recycler_view);
            trailerAdapter = new TrailerRecyclerView(getActivity(),trailerModels);
            trailerRecycler.setAdapter(trailerAdapter);
            trailerRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            trailerAdapter.notifyDataSetChanged();

            //set trailer actionListner
            trailerOnitemListner();
        }

        @Override
        protected ArrayList<TrailerModel> doInBackground(Integer... integers) {
            if (integers.length==0){
                return null;
            }

            //connect to api services
            HttpURLConnection urlConnection=null;
            BufferedReader reader=null;
            String trailerJsonStr=null;
            final String urlString ="http://api.themoviedb.org/3/movie/"+integers[0]+"/videos?api_key=f470b65a6a640a7cafd4b4a02dd90807";
            try {
                URL url = new URL(urlString);
                urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // read data from stream
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream==null) {
                    return null;
                }
                reader=new BufferedReader(new InputStreamReader(inputStream));
                //Read from bufferReader
                String line;
                while((line=reader.readLine()) != null)
                {
                    buffer.append(line +"/n");
                }
                if(buffer.length()<=0) {
                    Log.v("Response","Error to connect");
                    return null;

                }
                trailerJsonStr=buffer.toString();
                Log.v(LOG_TAG+"Response",trailerJsonStr);
            } catch (MalformedURLException e) {
                Log.v(LOG_TAG+" Error :","URl url = new .....");
            }  catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(urlConnection !=null) {
                    urlConnection.disconnect();
                }
                if(reader !=null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    return trailerDataFormate(trailerJsonStr);
                } catch (JSONException e) {
                    return null;
                }
            }

        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //trailer json formate
    public ArrayList<TrailerModel> trailerDataFormate(String trailerJsonStr) throws JSONException {

        ArrayList<TrailerModel> data=new ArrayList<>();

        String ARRAY_OF_ALL_TRAILERS_KEY ="results";
        String TRAILER_ID_KEY="id";
        String TRAILER_KEY_KEY="key";

        JSONObject trailersObject = new JSONObject(trailerJsonStr);
        JSONArray trailer_array = trailersObject.getJSONArray(ARRAY_OF_ALL_TRAILERS_KEY);

        for (int i =0;i<trailer_array.length();i++)
        {
            JSONObject single_trailer_data=trailer_array.getJSONObject(i);
            TrailerModel trailerModel = new TrailerModel();
            trailerModel.setTrailer_id(single_trailer_data.getString(TRAILER_ID_KEY));
            trailerModel.setTrailer_key(single_trailer_data.getString(TRAILER_KEY_KEY));
            trailerModel.setTrailer_name("Trailer "+String.valueOf(i+1));
            data.add(trailerModel);
        }

        return data;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //binding static data
    public void bindMovieStaticData()
    {


        //get xml views
        movieTitle=(TextView)view.findViewById(R.id.detail_titleTxt);
        posterPath=(ImageView) view.findViewById(R.id.detail_poster);
        releaseDate=(TextView)view.findViewById(R.id.detail_release_date);
        overview=(TextView)view.findViewById(R.id.detail_overview);
        votingRate=(RatingBar)view.findViewById(R.id.detail_rating_bar);
        ratingTxt=(TextView)view.findViewById(R.id.detail_rating_txt);
        favBtn=(ImageView)view.findViewById(R.id.detail_favourite_button);

        //binding data from bundle to views
        movieTitle.setText(bundle.getString("original_title"));
        releaseDate.setText(bundle.getString("release_date"));
        ratingTxt.setText(String.valueOf(bundle.getDouble("vote_average"))+"/10");
        overview.setText(bundle.getString("overview"));
        votingRate.setRating((float) bundle.getDouble("vote_average"));
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/"+bundle.getString("poster_path")).into(posterPath);

        if (bundle.getBoolean("is_fav")){
            favBtn.setColorFilter(Color.YELLOW);
        }
        else {
            favBtn.setColorFilter(Color.GRAY);
        }

    }

    //on trailer action listner open the trailer via youtube
    public void trailerOnitemListner() {

        trailerRecycler.addOnItemTouchListener(new RecyclerViewOnItemClickListner(getActivity(), trailerRecycler, new RecyclerViewOnItemClickListner.recyclerViewTouchListner() {
            @Override
            public void onclick(View child, int postion) {
                TrailerModel model = new TrailerModel();
                model=trailerAdapter.getItem(postion);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/watch?v="+model.getTrailer_key()));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View child, int postion) {

            }
        }));
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //get reviews for this movie
public class FetchReviewsTask extends AsyncTask<Integer , Void ,ArrayList<PeopleReviewsModel>>
    {
        private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();

        @Override
        protected void onPostExecute(ArrayList<PeopleReviewsModel> peopleReviewsModels) {
            super.onPostExecute(peopleReviewsModels);

            //check if exist reviews for the movie
            if(peopleReviewsModels.size()==0)
            {
                return ;
            }

            //define reclerView for the reviews and its adapter
            reviewsRecycler =(RecyclerView)view.findViewById(R.id.people_recycler_view);
            reviewsAdapter = new ReviewsRecyclerView(getActivity(),peopleReviewsModels);
            reviewsRecycler.setAdapter(reviewsAdapter);
            reviewsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            reviewsAdapter.notifyDataSetChanged();


        }

        @Override
        protected ArrayList<PeopleReviewsModel> doInBackground(Integer... integers) {
            if (integers.length==0){
                return null;
            }
            //connect to api services
            HttpURLConnection urlConnection=null;
            BufferedReader reader=null;
            String reviewsJsonStr=null;
            final String urlString ="http://api.themoviedb.org/3/movie/"+integers[0]+"/reviews?api_key=f470b65a6a640a7cafd4b4a02dd90807";
            URL url = null;
            try {
                url = new URL(urlString);
                urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // read data from stream
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream==null) {
                    return null;
                }
                reader=new BufferedReader(new InputStreamReader(inputStream));
                //Read from bufferReader
                String line;
                while((line=reader.readLine()) != null)
                {
                    buffer.append(line +"/n");
                }
                if(buffer.length()<=0) {
                    Log.v("Response","Error to connect");
                    return null;

                }
                reviewsJsonStr=buffer.toString();
                Log.v(LOG_TAG+"Response",reviewsJsonStr);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(urlConnection !=null) {
                    urlConnection.disconnect();
                }
                if(reader !=null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    return reviewsDataFormate(reviewsJsonStr);
                } catch (JSONException e) {
                    return null;
                }
            }

        }
        }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //formate reviews json
    private ArrayList<PeopleReviewsModel> reviewsDataFormate(String reviewsJsonStr) throws JSONException {

        ArrayList<PeopleReviewsModel> data=new ArrayList<>();

        String ARRAY_OF_ALL_TRAILERS_KEY ="results";
        String REVIEW_KEY="content";
        String AUTHOR_KEY="author";

        JSONObject reviewsObject = new JSONObject(reviewsJsonStr);
        JSONArray reviews_array = reviewsObject.getJSONArray(ARRAY_OF_ALL_TRAILERS_KEY);
        for (int i =0;i<reviews_array.length();i++)
        {
            PeopleReviewsModel peopleReviewsModel = new PeopleReviewsModel();
            JSONObject singleReviewJsonObject = reviews_array.getJSONObject(i);
            peopleReviewsModel.setAuthor(singleReviewJsonObject.getString(AUTHOR_KEY));
            peopleReviewsModel.setContent(singleReviewJsonObject.getString(REVIEW_KEY));
            data.add(peopleReviewsModel);
        }

        return data;
    }

    //method for fav button clickListner
    private void onFavBtnListner(){
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bundle.getBoolean("is_fav"))
                {
                    bundle.putBoolean("is_fav",false);
                    favBtn.setColorFilter(Color.GRAY);
                    new MovieDatabase(getActivity()).deleteMovie(bundle.getInt("id"));
                }
                else{
                    bundle.putBoolean("is_fav",true);
                    favBtn.setColorFilter(Color.YELLOW);
                    MoviesModel model = new MoviesModel();
                    model.setId(bundle.getInt("id"));
                    model.setOriginal_title(bundle.getString("original_title"));
                    model.setOverView(bundle.getString("overview"));
                    model.setRelease_date(bundle.getString("release_date"));
                    model.setPoster_path(bundle.getString("poster_path"));
                    model.setVote_average(bundle.getDouble("vote_average"));
                    new MovieDatabase(getActivity()).insertMovie(model);
                }
            }
        });
    }


}
