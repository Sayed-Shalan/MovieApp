package movieapp.shalan.net.sayed.movieapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import movieapp.shalan.net.sayed.movieapp.Fragments.DetailFragment;

public class DetailActivity extends AppCompatActivity {

    //deceleration  of needed views and variables
    Toolbar toolbar ;
    ShareActionProvider shareActionProvider;
    public static Activity detailActivityContext;
    private static final String MOVIE_SHARE_HASHTAG="#movieApp";
    Bundle bundle;
    DetailFragment detailFragment;

    //inflate detail menu for detail activity and set Action for shareActionProvider
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail,menu);
        MenuItem menuItem =menu.findItem(R.id.action_share);
        shareActionProvider=(ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (shareActionProvider!=null)
        {
            shareActionProvider.setShareIntent(createShareMovieIntent());
        }
        return super.onCreateOptionsMenu(menu);
    }

    //get menu items and set action for them
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home :
                this.finish();
                return true;
            default:
            return super.onOptionsItemSelected(item);

        }
    }

    //method return intent with data i want to share
    public Intent createShareMovieIntent(){
        Intent intent =new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,bundle.getString("original_title")+" ,vote average is :"+
                String.valueOf(bundle.getDouble("vote_average"))+"/10 ,overview :"+bundle.getString("overview")+" ,"
                +MOVIE_SHARE_HASHTAG);
        return intent;


    }
/////////////////////////////////////////////////////////////////////////////////////////////////

    //start onCreate for the detail activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //set actionbar logo and title
         toolbar = (Toolbar) findViewById(R.id.toolbar_detailactivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbtn);

        //get Activity Context
        detailActivityContext = this;

        //get data from mainFragment and pass it for detailFragment
         bundle = new Bundle();
        bundle = getIntent().getExtras();
        detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);

        //inflate detail fragment into detail activity
        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container,detailFragment).commit();

        
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
