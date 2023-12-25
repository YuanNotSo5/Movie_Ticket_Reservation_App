package com.example.appbookticketmovie.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbookticketmovie.Models.CommentItem;
import com.example.appbookticketmovie.R;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    ArrayList<CommentItem> listComment;
    Context context;

    public CommentAdapter(ArrayList<CommentItem> listComment) {
        this.listComment = listComment;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_comment_item,parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameCmtTxt.setText(listComment.get(position).getUserName());
        Glide.with(context)
                .load(listComment.get(position).getPhoto())
                .into(holder.cmtPhoto);
        holder.dateCmtTxt.setText(listComment.get(position).getDate());
        holder.commentTxt.setText(listComment.get(position).getCommnet());
        holder.ratingBar.setRating(Float.parseFloat(listComment.get(position).getRating()));
    }

    @Override
    public int getItemCount() {
        return listComment.size();
    }

    // Add a method to update the comment list
    public void updateCommentList(ArrayList<CommentItem> newCommentList) {
        listComment.clear();
        listComment.addAll(newCommentList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameCmtTxt, dateCmtTxt, commentTxt;
        RatingBar ratingBar;
        ImageView cmtPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameCmtTxt = itemView.findViewById(R.id.nameCmtTxt);
            dateCmtTxt = itemView.findViewById(R.id.dateCmtTxt);
            commentTxt = itemView.findViewById(R.id.commentTxt);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            cmtPhoto = itemView.findViewById(R.id.cmtPhoto);


        }
    }
}
