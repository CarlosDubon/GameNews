package com.bonusteam.gamenews.Fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bonusteam.gamenews.Adapter.NewsAdapter;
import com.bonusteam.gamenews.Entity.Favorite;
import com.bonusteam.gamenews.Entity.New;
import com.bonusteam.gamenews.Model.GameNewsViewModel;
import com.bonusteam.gamenews.R;

import java.util.ArrayList;
import java.util.List;

public class FavoriteNewFragment extends Fragment {

    private FavoriteNewsTools tools;
    private List<New> favoritesNews;
    private GameNewsViewModel model;
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private SwipeRefreshLayout refreshContent;


    public FavoriteNewFragment() {
    }

    public static FavoriteNewFragment newInstance(List<New> favoritesNews){
        FavoriteNewFragment fragment = new FavoriteNewFragment();
        fragment.setFavoritesNews(favoritesNews);
        return fragment;
    }

    public void setFavoritesNews(List<New> favoritesNews){
        this.favoritesNews = favoritesNews;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_by_game,container,false);
        recyclerView = view.findViewById(R.id.recyclerview_news_by_games);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshContent = view.findViewById(R.id.refreshContent);
        refreshContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tools.refreshFavorites();
                refreshContent.setRefreshing(false);
            }
        });
        adapter = new NewsAdapter(getActivity());
        adapter.fillNews(favoritesNews);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public interface FavoriteNewsTools{
        void refreshFavorites();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof FavoriteNewsTools){
            tools = (FavoriteNewsTools) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        tools=null;
    }
}
