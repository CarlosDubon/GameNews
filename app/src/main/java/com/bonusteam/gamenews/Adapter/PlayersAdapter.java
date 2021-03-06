package com.bonusteam.gamenews.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonusteam.gamenews.Entity.Player;
import com.bonusteam.gamenews.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlayersAdapter  extends RecyclerView.Adapter<PlayersAdapter.PlayersViewHolder>{
    View view;
    private final LayoutInflater layoutInflater;
    private List<Player> playerList;
    private Context context;


    public static class PlayersViewHolder extends RecyclerView.ViewHolder{
        ImageView avatar;
        TextView name,ranking;
        LinearLayout container;
        public PlayersViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.imageview_player);
            name = itemView.findViewById(R.id.text_name_player);
            ranking = itemView.findViewById(R.id.text_ranking_player);
            container = itemView.findViewById(R.id.player_container);
        }
    }
    public PlayersAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
        this.context = context;

    }
    public void fillPlayers(List<Player> playerList){
        this.playerList = playerList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public PlayersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.player_top_item,parent,false);
        return new PlayersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayersViewHolder holder, final int position) {
        Picasso.get().load(playerList.get(position).getAvatar()).into(holder.avatar);
        holder.name.setText(playerList.get(position).getName());
        holder.ranking.setText("Ranking "+(position+1));
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog playerDialog = new Dialog(context);
                playerDialog.setContentView(R.layout.dialog_top_player_info);
                playerDialog.setTitle(playerList.get(position).getName());
                ImageView avatarDialog = playerDialog.findViewById(R.id.avatar_player_dialog);
                TextView nameDialog = playerDialog.findViewById(R.id.text_name_player_dialog);
                TextView bioDialog = playerDialog.findViewById(R.id.text_bio_player_dialog);
                Picasso.get().load(playerList.get(position).getAvatar()).into(avatarDialog);
                nameDialog.setText(playerList.get(position).getName());
                bioDialog.setText(playerList.get(position).getBiografia());
                playerDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(playerList!=null){
            return playerList.size();
        }else {
            return 0;
        }
    }

}
