package movieapp.shalan.net.sayed.movieapp;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import movieapp.shalan.net.sayed.movieapp.Fragments.MainFragment;

public class MainActivity extends AppCompatActivity {

    //deceleration of needed views and variables
    Toolbar toolbar ;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    MainFragment mainFragment;
    public static Activity fa;

    //inflate main menu for mainActivity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //get menu items and set action fro each icon
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_refresh:
                addFragment();
                break;
            case R.id.action_setting:
                goToSettingActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
//////////////////////////////////////////////////////////////////////////////////////////////

    //start onCreate method for MainActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get Context of this Activity
        fa=this;

        //set actionbar logo and title
        toolbar = (Toolbar)findViewById(R.id.toolbar_mainctivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);


        //call method to add fragment
        addFragment();

    }
/////////////////////////////////////////////////////////////////////////////////////////////

    //method to add MainFragment To MainActivity
    public  void addFragment()
    {
        mainFragment=new MainFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragemnt_frame,mainFragment);
        fragmentTransaction.commit();
    }

    //method for setting item to go to setting activity
    public void goToSettingActivity()
    {
        Intent intent = new Intent(MainActivity.this,Setting_Activity.class);
        startActivity(intent);
    }

    //start onStart for this Activity
    @Override
    protected void onStart() {
        super.onStart();

        //when back to this activity refresh the fragment
       addFragment();
    }



}
