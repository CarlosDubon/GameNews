package com.bonusteam.gamenews.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bonusteam.gamenews.Adapter.PlayersAdapter;
import com.bonusteam.gamenews.R;


public class TopPlayerFragment extends Fragment {
    PlayersAdapter playersAdapter;
    RecyclerView rv;

    public TopPlayerFragment() {
        // Required empty public constructor
    }

    public static TopPlayerFragment newInstance(PlayersAdapter adapter) {
        TopPlayerFragment fragment = new TopPlayerFragment();

        return fragment;
    }
    public void setAdapter(PlayersAdapter adapter){
        playersAdapter= adapter;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_top_player, container, false);
        rv = view.findViewById(R.id.recyclerview_top_players);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(playersAdapter);
        return view;
    }


}
