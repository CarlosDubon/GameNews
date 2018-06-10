package com.bonusteam.gamenews.Fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    private List<Favorite> favoritesNewsID;
    private GameNewsViewModel model;
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    List<New> newList = new ArrayList<>();


    public FavoriteNewFragment() {
    }

    public static FavoriteNewFragment newInstance(List<Favorite> favoritesNewsID){
        FavoriteNewFragment fragment = new FavoriteNewFragment();
        fragment.setFavoritesNewsID(favoritesNewsID);
        return fragment;
    }

    public void setFavoritesNewsID(List<Favorite> favoritesNewsID){
        this.favoritesNewsID = favoritesNewsID;
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

        adapter = new NewsAdapter(getActivity());
        model = ViewModelProviders.of(this).get(GameNewsViewModel.class);
        for(int i=0;i<favoritesNewsID.size();i++){
            model.getNew(favoritesNewsID.get(i).get_id()).observe(this, new Observer<New>() {
                @Override
                public void onChanged(@Nullable New aNew) {
                    if(newList.indexOf(aNew)<0) {
                        newList.add(aNew);
                    }else{
                        newList.set(newList.indexOf(aNew),aNew);
                    }
                    adapter.fillNews(newList);
                    recyclerView.setAdapter(adapter);
                }
            });
        }
        return view;
    }
}
