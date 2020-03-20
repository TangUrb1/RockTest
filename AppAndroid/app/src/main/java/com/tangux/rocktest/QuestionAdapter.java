package com.tangux.rocktest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private List<Music> musics;

    //Constructor of the adapter
    public QuestionAdapter(List<Music> musicList) {
        musics = musicList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);

        return new ViewHolder(view);
    }

    //Display the information of the item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Music music = musics.get(position);

        holder.title.setText(music.getTitle());
        holder.singer.setText(music.getSinger());
    }

    //Set the max size of the recycler view according to the length of the list
    @Override
    public int getItemCount() {

        return musics.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        final TextView title;
        final TextView singer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleTextView);
            singer = itemView.findViewById(R.id.singerTextView);
        }
    }
}
