package com.openin.listed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class VerticalLinksAdapter extends RecyclerView.Adapter<VerticalLinksAdapter.MyViewHolder>{

    private List<VerticalLinksModel> vlinkslist;


    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, link, date, number;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title_v);
            link = view.findViewById(R.id.vlink);
            date = view.findViewById(R.id.vdate);
            number = view.findViewById(R.id.vnum);
        }
    }

    public VerticalLinksAdapter(List<VerticalLinksModel> linkslist) {
        this.vlinkslist = linkslist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vertical_links_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        VerticalLinksModel data = vlinkslist.get(position);

        String date = data.getDate();
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        Date parsedDate = null;
        try {
            parsedDate = inputFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = outputFormat.format(Objects.requireNonNull(parsedDate));

        holder.title.setText(data.getTitle());
        holder.link.setText(data.getLink());
        holder.date.setText(formattedDate);
        holder.number.setText(data.getNumber());



    }

    @Override
    public int getItemCount() {
        return vlinkslist.size();
    }
}
