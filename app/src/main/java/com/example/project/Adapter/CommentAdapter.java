package com.example.project.Adapter;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

public class CommentAdapter extends RecyclerView.ViewHolder {
public TextView Phone, Comment;
public RatingBar ratingBar;

    public CommentAdapter(@NonNull View itemView) {
        super(itemView);
        Comment =  (TextView)itemView.findViewById(R.id.txt_comment);
        Phone = (TextView)itemView.findViewById(R.id.txtName);
        ratingBar = (RatingBar)itemView.findViewById(R.id.ratingBar);

    }
}
