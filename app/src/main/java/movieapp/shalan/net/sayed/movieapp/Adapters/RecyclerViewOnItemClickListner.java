package movieapp.shalan.net.sayed.movieapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerViewOnItemClickListner implements RecyclerView.OnItemTouchListener{

    //deceleration of needed classes and variables
    private GestureDetector mGestureDetector ;
    private  recyclerViewTouchListner mRecyclerViewTouchListner ;

    //constructor get information of recyclerView and context
    public RecyclerViewOnItemClickListner(Context context , final RecyclerView recyclerView , final recyclerViewTouchListner mRecyclerViewTouchListner) {
        this.mRecyclerViewTouchListner = mRecyclerViewTouchListner;
        mGestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View  child = recyclerView.findChildViewUnder(e.getX(),e.getY());
                if (child != null){
                    mRecyclerViewTouchListner.onclick(child,recyclerView.getChildAdapterPosition(child));

                }

                return true ;
            }

            @Override
            public void onLongPress(MotionEvent e) {

                View  child = recyclerView.findChildViewUnder(e.getX(),e.getY());
                if (child != null){
                    mRecyclerViewTouchListner.onLongClick(child,recyclerView.getChildAdapterPosition(child));

                }

            }
        });

    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
    public  interface  recyclerViewTouchListner {
         void  onclick(View child, int postion);
         void  onLongClick(View child, int postion);

    }

}