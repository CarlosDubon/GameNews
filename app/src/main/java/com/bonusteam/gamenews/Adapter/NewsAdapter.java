package com.bonusteam.gamenews.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bonusteam.gamenews.Activity.MainActivity;
import com.bonusteam.gamenews.Activity.SingleNewActivity;
import com.bonusteam.gamenews.Entity.New;
import com.bonusteam.gamenews.Interface.NewTools;
import com.bonusteam.gamenews.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private NewTools tools;
    private final LayoutInflater layoutInflater;
    private List<New> newList;
    private Context context;
    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        ImageView imageNews;
        TextView title,fab,description,date,category;
        CardView container;
        public NewsViewHolder(View itemView) {
            super(itemView);
            imageNews=itemView.findViewById(R.id.imageview_news);
            title = itemView.findViewById(R.id.text_title_news);
            fab = itemView.findViewById(R.id.text_fab);
            date = itemView.findViewById(R.id.text_date_news);
            category = itemView.findViewById(R.id.text_category_news);
            container = itemView.findViewById(R.id.new_container);
        }
    }
    public NewsAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
        tools = (MainActivity) context;
        this.context = context;
    }

    public void fillNews(List<New> newList){
        this.newList = newList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.news_item,parent,false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, final int position) {
        Picasso.get().load(newList.get(position).getCoverImage()).into(holder.imageNews);
        holder.category.setText(newList.get(position).getGame());
        holder.title.setText(newList.get(position).getTitle());
        holder.date.setText(newList.get(position).getCreated_date());
        if(newList.get(position).getFavorite()==1){
            holder.fab.setBackgroundResource(R.drawable.ic_favorite_active);
        }else{
            holder.fab.setBackgroundResource(R.drawable.ic_favorite_white);
        }
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context.getApplicationContext(), SingleNewActivity.class);
                i.putExtra("ID_NEW",newList.get(position).get_id());
                context.startActivity(i);
            }
        });
        holder.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newList.get(position).getFavorite()!=1) {
                    Snackbar.make(v, "Noticia añadida a favoritos", Snackbar.LENGTH_SHORT).show();
                    tools.addFavorites(newList.get(position).get_id());
                }else{
                    Snackbar.make(v, "Noticia removida de favoritos", Snackbar.LENGTH_SHORT).show();
                    tools.removeFavorites(newList.get(position).get_id());
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        if(newList!=null){
            return newList.size();
        }else {
            return 0;
        }
    }

}
