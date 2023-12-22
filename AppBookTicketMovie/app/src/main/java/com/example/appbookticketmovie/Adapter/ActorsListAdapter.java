package com.example.appbookticketmovie.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbookticketmovie.Models.ActorItem;
import com.example.appbookticketmovie.R;

import java.util.List;

public class ActorsListAdapter extends RecyclerView.Adapter<ActorsListAdapter.ViewHolder> {
    List<ActorItem> listActors;
    Context context;

    public ActorsListAdapter(List<ActorItem> listActors) {
        this.listActors = listActors;
    }

    @NonNull
    @Override
    public ActorsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_actor,parent,false);
        return new ViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull ActorsListAdapter.ViewHolder holder, int position) {
        Glide.with(context)
                .load(listActors.get(position).getPhotoUrl())
                .into(holder.pic);
        holder.txtNameActor.setText(listActors.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return listActors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNameActor;
        ImageView pic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.itemImages);
            txtNameActor = itemView.findViewById(R.id.txtNameActor);
        }
    }
}
