package movieapp.shalan.net.sayed.movieapp.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import movieapp.shalan.net.sayed.movieapp.Models.PeopleReviewsModel;

import movieapp.shalan.net.sayed.movieapp.R;

public class ReviewsRecyclerView extends RecyclerView.Adapter<ReviewsRecyclerView.ReviewsViewHolder>{

    //deceleration of needed variables
    private Context context ;
    private ArrayList<PeopleReviewsModel> data ;
    private LayoutInflater layoutInflater ;

    //create class constructor
    public ReviewsRecyclerView(Context context,ArrayList<PeopleReviewsModel> data)
    {
        this.context=context;
        this.data=data;
        layoutInflater = LayoutInflater.from(context);
    }

    //inflating single item of review to the coming context
    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.people_reviews_single_item,parent,false);
        ReviewsViewHolder trailerHolder = new ReviewsViewHolder(view);
        return trailerHolder;
    }

    //binding coming data to single item review views
    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {

        if (data.size()==0)
        {

        }
        else {
            PeopleReviewsModel currentElement = data.get(position);
            holder.authorTxt.setText(currentElement.getAuthor()+": ");
            holder.contentTxt.setText(currentElement.getContent());
        }
    }

    //return number of items
    @Override
    public int getItemCount() {
        return data.size();
    }

    //class that define single item review views
    public class ReviewsViewHolder extends RecyclerView.ViewHolder{
        TextView authorTxt;
        TextView contentTxt;
        public ReviewsViewHolder(View itemView) {
            super(itemView);
            authorTxt=(TextView) itemView.findViewById(R.id.txtAuthor);
            contentTxt=(TextView) itemView.findViewById(R.id.txtContent);
        }
    }
}
