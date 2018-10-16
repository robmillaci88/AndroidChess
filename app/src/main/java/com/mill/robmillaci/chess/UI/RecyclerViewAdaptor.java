package com.mill.robmillaci.chess.UI;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mill.robmillaci.chess.R;

import java.util.ArrayList;

public class RecyclerViewAdaptor extends RecyclerView.Adapter<RecyclerViewAdaptor.MyViewHolder>  {
    ArrayList<Drawable> pieces;
    public RecyclerViewAdaptor(ArrayList<Drawable> pieces) {
        this.pieces = pieces;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewitem,parent,false);
       return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.takenPiece.setImageDrawable(pieces.get(i));
    }

    @Override
    public int getItemCount() {
        return pieces.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView takenPiece;
        private MyViewHolder(@NonNull View itemView) {
            super(itemView);
            takenPiece = itemView.findViewById(R.id.takenpiece);
        }
    }
}
