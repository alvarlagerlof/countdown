package com.alvarlagerlof.countdown.Main;

/**
 * Created by alvar on 2016-07-02.
 */

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alvarlagerlof.countdown.AddAndEdit.AddAndEdit;
import com.alvarlagerlof.countdown.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ClockAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ClockObject> dataset;

    public ClockAdapter(ArrayList<ClockObject> dataset) {
        this.dataset = dataset;
    }

    public static class ViewHolderItem extends RecyclerView.ViewHolder {
        public final TextView title;
        public final TextView left;
        public final TextView until;


        public ViewHolderItem(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            left        = (TextView) itemView.findViewById(R.id.left);
            until        = (TextView) itemView.findViewById(R.id.until);
        }

    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolderItem(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.clock_item, viewGroup, false));
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        String left;
        int until = dataset.get(position).until;

        // Fix left
        Long tsLong = System.currentTimeMillis()/1000;
        int current_timestamp = Integer.parseInt(tsLong.toString());

        int difference = until - current_timestamp;


        int weeks = difference / (60 * 60 * 24 * 7);
        difference -= weeks * 7 * 24 * 60 * 60;

        int days = difference / (24 * 60 * 60);
        difference -= days * 24 * 60 * 60;


        int hours = (difference / (60 * 60)) % 60;
        int minutes = (difference / (60)) % 60;
        int seconds = (difference) % 60;

        left = String.valueOf(weeks + "w " +
                days + "d " +
                hours + "h " +
                minutes + "m " +
                seconds + "s");


        // Fix until
        Date date = new Date(until * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy, HH:mm");


        // Set text
        ((ViewHolderItem) holder).title.setText(dataset.get(position).title);
        ((ViewHolderItem) holder).left.setText(left);
        ((ViewHolderItem) holder).until.setText(sdf.format(date));


        ((ViewHolderItem) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(((ViewHolderItem) holder).title.getContext(), AddAndEdit.class);
                intent.putExtra("id", dataset.get(position).id);
                intent.putExtra("title", dataset.get(position).title);
                intent.putExtra("until", dataset.get(position).until);
                ((ViewHolderItem) holder).title.getContext().startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


}
