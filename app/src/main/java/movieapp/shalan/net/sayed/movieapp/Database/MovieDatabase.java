package movieapp.shalan.net.sayed.movieapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


import movieapp.shalan.net.sayed.movieapp.Models.MoviesModel;

public class MovieDatabase extends SQLiteOpenHelper{

    //Define database name and version
    private static String DB_NAME="movies";
    private static int DB_VERSION=1;

    //Define table name and take object from SQLiteDatabase
    private SQLiteDatabase dp;
    private String TABLE_NAME="FAVOURITES";

    //Define Favourite columns
    private String KEY_FAV_ID="ID";
    private String KEY_FAV_TITLE="TITLE";
    private String KEY_FAV_POSTER_PATH="POSTER_PATH";
    private String KEY_FAV_RELEASE_DATE="RELEASE_DATE";
    private String KEY_FAV_VOTE_AVERAGE="VOTE_AVERAGE";
    private String KEY_FAV_OVERVIEW="OVERVIEW";

    public MovieDatabase(Context context)
    {
        super(context, DB_NAME,null,DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " ( "+KEY_FAV_ID+" INTEGER , "+KEY_FAV_TITLE+" TEXT , "+
                KEY_FAV_OVERVIEW+" TEXT , "+KEY_FAV_POSTER_PATH + " TEXT , "+KEY_FAV_RELEASE_DATE+" TEXT , "+KEY_FAV_VOTE_AVERAGE +" NUMERIC );"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME+" ;");
        onCreate(sqLiteDatabase);
    }

    //MAKE METHOD FOR INSERT A MOVIE
    public void insertMovie(MoviesModel moviesModel)
    {
        dp=this.getWritableDatabase();
        ContentValues cv=new ContentValues();


        cv.put(KEY_FAV_ID,moviesModel.getId());
        cv.put(KEY_FAV_OVERVIEW,moviesModel.getOverView());
        cv.put(KEY_FAV_POSTER_PATH,moviesModel.getPoster_path());
        cv.put(KEY_FAV_RELEASE_DATE,moviesModel.getRelease_date());
        cv.put(KEY_FAV_TITLE,moviesModel.getOriginal_title());
        cv.put(KEY_FAV_VOTE_AVERAGE,moviesModel.getVote_average());

        dp.insert(TABLE_NAME,null,cv);
        dp.close();
    }


    //MAKE METHOD FOR DELETE A MOVIE FROM A FAVOURITE
    public void deleteMovie(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME,KEY_FAV_ID + " = ? ",

                new String[]{String.valueOf(id)});
    }

    //MAKE A METHOD FOR SELECT ALL MOVIES IDs
    public ArrayList<MoviesModel> getMovies()
    {
        ArrayList<MoviesModel> ints = new ArrayList<>();
        dp=getReadableDatabase();
        String columns[] = new String[]{KEY_FAV_ID, KEY_FAV_TITLE,KEY_FAV_OVERVIEW,KEY_FAV_OVERVIEW,KEY_FAV_POSTER_PATH,KEY_FAV_RELEASE_DATE,KEY_FAV_VOTE_AVERAGE};
        Cursor c = dp.query(TABLE_NAME,columns,null,null,null,null,null);

        int idIndex = c.getColumnIndex(KEY_FAV_ID);
        int titleIndex=c.getColumnIndex(KEY_FAV_TITLE);
        int overviewIndex=c.getColumnIndex(KEY_FAV_OVERVIEW);
        int releaseDateIndex=c.getColumnIndex(KEY_FAV_RELEASE_DATE);
        int voteAverageIndex=c.getColumnIndex(KEY_FAV_VOTE_AVERAGE);
        int posterPathIndex=c.getColumnIndex(KEY_FAV_POSTER_PATH);

        if (c != null && c.moveToFirst()) {
            do {
                MoviesModel model = new MoviesModel();
                model.setId(c.getInt(idIndex));
                model.setVote_average(c.getDouble(voteAverageIndex));
                model.setPoster_path(c.getString(posterPathIndex));
                model.setOriginal_title(c.getString(titleIndex));
                model.setRelease_date(c.getString(releaseDateIndex));
                model.setOverView(c.getString(overviewIndex));
                model.setInfavourtie(true);
                ints.add(model);
            }while (c.moveToNext());
            c.close();
            dp.close();
            return ints;
        }
        c.close();
        dp.close();
        return ints;
    }

}
