package com.bonusteam.gamenews.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bonusteam.gamenews.Entity.New;
import com.bonusteam.gamenews.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<New> newList;
    private Context context;
    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        ImageView imageNews;
        TextView title,fab,description,date,category;
        public NewsViewHolder(View itemView) {
            super(itemView);
            imageNews=itemView.findViewById(R.id.imageview_news);
            title = itemView.findViewById(R.id.text_title_news);
            fab = itemView.findViewById(R.id.text_fab);
            description = itemView.findViewById(R.id.text_description_news);
            date = itemView.findViewById(R.id.text_date_news);
            category = itemView.findViewById(R.id.text_category_news);
        }
    }
    public NewsAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
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
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Picasso.get().load(newList.get(position).getConverImage()).into(holder.imageNews);
        holder.category.setText(newList.get(position).getGame());
        holder.title.setText(newList.get(position).getDescription());
        holder.description.setText(newList.get(position).getDescription());
        holder.date.setText(newList.get(position).getCreateDate());
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
