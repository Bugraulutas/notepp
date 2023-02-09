package com.bgr2.notepp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.CardDesignHolder> {
        private Context mContex;
        private List<Notes> notesList;

    public NotesAdapter(Context mContex, List<Notes> notesList) {
        this.mContex = mContex;
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public CardDesignHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_main,parent,false);

        return new CardDesignHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardDesignHolder holder, int position) {
        Notes note=notesList.get(position);

        holder.textview_main_title.setText(note.getNote_title());
        holder.textview_main_note.setText(note.getNote());

        holder.cardview_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContex,ArrangeActivity.class);
                intent.putExtra("object",note);
                mContex.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class CardDesignHolder extends RecyclerView.ViewHolder{
        private TextView textview_main_title;
        private TextView textview_main_note;
        private CardView cardview_main;

        public CardDesignHolder(@NonNull View itemView) {
            super(itemView);
            textview_main_title=itemView.findViewById(R.id.textview_main_title);
            textview_main_note=itemView.findViewById(R.id.textview_main_note);
            cardview_main=itemView.findViewById(R.id.cardview_main);
        }
    }
}
