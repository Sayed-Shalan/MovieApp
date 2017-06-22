package movieapp.shalan.net.sayed.movieapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import movieapp.shalan.net.sayed.movieapp.Models.TrailerModel;
import movieapp.shalan.net.sayed.movieapp.R;

public class TrailerRecyclerView extends RecyclerView.Adapter<TrailerRecyclerView.TrailerHolder>{

    //deceleration of needed variables
    private Context context ;
    private ArrayList<TrailerModel> data ;
    private LayoutInflater layoutInflater ;

    //create class constructor
    public TrailerRecyclerView(Context context,ArrayList<TrailerModel> data)
    {
        this.context=context;
        this.data=data;
        layoutInflater = LayoutInflater.from(context);
    }

    //inflating single item of trailer to the coming context
    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.trailer_single_item,parent,false);
        TrailerHolder trailerHolder = new TrailerHolder(view);
        return trailerHolder;

    }

    //binding coming data to single item trailer views
    @Override
    public void onBindViewHolder(TrailerHolder holder, int position) {

        if (data.size()==0)
        {

        }
        else {
            TrailerModel currentElement = data.get(position);
            holder.trailerName.setText(currentElement.getTrailer_name());
        }
    }

    //return number of items
    @Override
    public int getItemCount() {
        return data.size();
    }

    //return item depend on its position
    public TrailerModel getItem(int postion){
        return  data.get(postion);
    }

    //class that define single trailer review views
    public class TrailerHolder extends RecyclerView.ViewHolder
    {
        private TextView trailerName;
        public TrailerHolder(View itemView) {
            super(itemView);

            trailerName=(TextView)itemView.findViewById(R.id.single_item_trailer_name);
        }
    }
}
