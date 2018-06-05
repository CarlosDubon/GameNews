package com.bonusteam.gamenews.Fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bonusteam.gamenews.Adapter.NewsAdapter;
import com.bonusteam.gamenews.DB.GameNewsRoomDatabase;
import com.bonusteam.gamenews.Entity.New;
import com.bonusteam.gamenews.Interface.NewDao;
import com.bonusteam.gamenews.Model.GameNewsViewModel;
import com.bonusteam.gamenews.R;

import java.util.List;

public class NewsByGameFragment extends Fragment {

    View view;
    private String game="";
    private GameNewsViewModel viewModel;
    private NewsAdapter adapter;
    private RecyclerView rv;
    public NewsByGameFragment() {
        // Required empty public constructor
    }

    public static NewsByGameFragment newInstance(NewsAdapter adapter) {
        NewsByGameFragment fragment = new NewsByGameFragment();
        fragment.setAdapter(adapter);
        return fragment;
    }
    private void setAdapter(NewsAdapter adapter){
        this.adapter = adapter;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_by_game, container, false);
        rv = view.findViewById(R.id.recyclerview_news_by_games);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);
        return view;
    }

}
