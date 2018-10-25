package com.itparkbynipun.floodspredictor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Vector;

/**
 * Created by agarw on 10/25/2018.
 */

public class newsAdapter extends RecyclerView.Adapter<newsAdapter.MyViewHolder> {
    Context context;
    private Vector<getNews> newsVector;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView headingtxt, bodytxt;


        public MyViewHolder(View view) {
            super(view);
            headingtxt = (TextView) view.findViewById(R.id.headingTxtView);
            bodytxt = (TextView) view.findViewById(R.id.bodyTxtView);

        }

    }
    public newsAdapter(Vector<getNews> news, Context context) {
        this.newsVector = news;
        this.context = context;
    }
    @Override
    public newsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View currentOrderView = inflater.inflate(R.layout.newscardview, parent, false);
        return new newsAdapter.MyViewHolder(currentOrderView);

    }

    @Override
    public void onBindViewHolder(final newsAdapter.MyViewHolder holder, final int position) {

        holder.headingtxt.setText(newsVector.get(position).getTitle().toString());
        holder.bodytxt.setText(newsVector.get(position).getBody().toString());



    }
    @Override
    public int getItemCount() {
        return newsVector.size();
    }



}
