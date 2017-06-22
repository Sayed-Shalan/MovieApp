package movieapp.shalan.net.sayed.movieapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import movieapp.shalan.net.sayed.movieapp.Database.MovieDatabase;
import movieapp.shalan.net.sayed.movieapp.Models.MoviesModel;
import movieapp.shalan.net.sayed.movieapp.R;


public class GridViewAdapter extends BaseAdapter{

    //deceleration of needed variables
    Context context;
    ArrayList<MoviesModel> items;
    String posterImageUrl="http://image.tmdb.org/t/p/w185/";

    //gridViewAdapter constructor
    public GridViewAdapter(Context context ,ArrayList<MoviesModel> items) {
        this.context=context;
        this.items=items;
    }

    //return number of items
    @Override
    public int getCount() {
        return items.size();
    }

    //return an item depend on its position
    @Override
    public MoviesModel getItem(int i) {
        return items.get(i);
    }

    //return item id
    @Override
    public long getItemId(int i) {
        return 0;
    }

    //inflating single item poster layout and binding data to its views
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view ==null)
        {
            LayoutInflater layoutInflater;
            layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=layoutInflater.inflate(R.layout.poster_single_item,null);
        }

        //add poster
        ImageView posterImg=(ImageView) view.findViewById(R.id.poster_image);
        Picasso.with(context).load(posterImageUrl+items.get(i).getPoster_path()).into(posterImg);

        //check favourite button
        final ImageView favBtn=(ImageView) view.findViewById(R.id.poster_favourite_button);
        if (items.get(i).isInfavourtie())
        {
            favBtn.setColorFilter(Color.YELLOW);
        }
        else
        {
            favBtn.setColorFilter(Color.GRAY);
        }


        //add on clickListner for FavBtn
        favBtn.setTag(String.valueOf(i));
       favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=Integer.parseInt(view.getTag().toString());
                if(items.get(pos).isInfavourtie())
                {
                    items.get(pos).setInfavourtie(false);
                    favBtn.setColorFilter(Color.GRAY);
                    new MovieDatabase(context).deleteMovie(items.get(pos).getId());


                }
                else {
                    items.get(pos).setInfavourtie(true);
                    favBtn.setColorFilter(Color.YELLOW);
                    new MovieDatabase(context).insertMovie(items.get(pos));

                }


            }
        });
        return view;
    }


}
