package com.openin.listed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.MyViewHolder> {

    private List<LinkModel> linkslist;
    Context mContext;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, num;
        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            num = view.findViewById(R.id.number);
            image = view.findViewById(R.id.img);
        }
    }

    public LinkAdapter(List<LinkModel> linkslist, Context mCon) {
        this.linkslist = linkslist;
        mContext = mCon;
    }

    @NonNull
    @Override
    public LinkAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.links_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LinkAdapter.MyViewHolder holder, int position) {
        LinkModel link = linkslist.get(position);
        String uri = "@drawable/source";
        String uri2 = "@drawable/location";

        int imageResource = mContext.getResources().getIdentifier(uri, null, mContext.getPackageName());
        int imageResource2 = mContext.getResources().getIdentifier(uri2, null, mContext.getPackageName());

        if(link.getTitle().equals("Top Location")){
            holder.image.setImageDrawable(mContext.getResources().getDrawable(imageResource2));
        }

        if(link.getTitle().equals("Top Source")){
            holder.image.setImageDrawable(mContext.getResources().getDrawable(imageResource));
        }
        if(link.getTitle().equals("Total Links")){
            holder.image.setImageDrawable(mContext.getResources().getDrawable(imageResource));
        }
        holder.title.setText(link.getTitle());
        holder.num.setText(link.getNum());
        //holder.image.setText(link.getYear());
    }

    @Override
    public int getItemCount() {
        return linkslist.size();
    }
}
