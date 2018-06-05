package com.bonusteam.gamenews.Fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bonusteam.gamenews.DB.GameNewsRoomDatabase;
import com.bonusteam.gamenews.Entity.New;
import com.bonusteam.gamenews.Interface.NewDao;
import com.bonusteam.gamenews.Model.GameNewsViewModel;
import com.bonusteam.gamenews.R;

import java.util.List;

public class NewsByGameFragment extends Fragment {

    View view;
    private GameNewsRoomDatabase db;
    private NewDao newDao;
    private LiveData<List<New>> newList;
    private String game;
    private GameNewsViewModel viewModel;
    public NewsByGameFragment() {
        // Required empty public constructor
    }

    public static NewsByGameFragment newInstance(String game) {
        NewsByGameFragment fragment = new NewsByGameFragment();
        fragment.setGameType(game);
        return fragment;
    }
    private void setGameType(String game){
        this.game = game;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FRAGMENTO: ",game);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_by_game, container, false);
    }

}
