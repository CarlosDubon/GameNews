package com.bonusteam.gamenews.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bonusteam.gamenews.R;

public class MainNewsFragment extends Fragment {
    View view;
    MainSetters tools;
    private RecyclerView recyclerView;
    private String game;

    public MainNewsFragment() {
        // Required empty public constructor
    }

    public static MainNewsFragment newInstance(String game) {
        MainNewsFragment fragment = new MainNewsFragment();
        fragment.setGame(game);
        return fragment;
    }
    public void setGame(String game){
        this.game = game;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_main_news, container, false);
        recyclerView = view.findViewById(R.id.recyclerview_news_general);
        GridLayoutManager glm = new GridLayoutManager(getActivity(),2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position%5==0){
                    return 2;
                }else {
                    return 1;
                }
            }
        });
        recyclerView.setLayoutManager(glm);
        tools.setAdapters(recyclerView);
        return view ;
    }


    public interface MainSetters {
        void setAdapters(RecyclerView rv);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MainSetters){
            tools = (MainSetters) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        tools =null;
    }

}
