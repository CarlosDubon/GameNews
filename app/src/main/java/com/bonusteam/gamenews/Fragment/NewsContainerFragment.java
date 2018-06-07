package com.bonusteam.gamenews.Fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bonusteam.gamenews.Adapter.NewsAdapter;
import com.bonusteam.gamenews.Adapter.PlayersAdapter;
import com.bonusteam.gamenews.Adapter.ViewPagerAdapter;
import com.bonusteam.gamenews.Entity.New;
import com.bonusteam.gamenews.Entity.Player;
import com.bonusteam.gamenews.Model.GameNewsViewModel;
import com.bonusteam.gamenews.R;

import java.util.List;

public class NewsContainerFragment extends Fragment {

    private String game;
    private GameNewsViewModel model;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private NewsAdapter newsAdapter;
    private PlayersAdapter playersAdapter;


    public NewsContainerFragment() {
    }

    public static NewsContainerFragment newInstance(String game){
        NewsContainerFragment fragment = new NewsContainerFragment();
        fragment.setGame(game);
        return fragment;
    }

    public void setGame(String game){
        this.game = game;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        newsAdapter = new NewsAdapter(getActivity());
        playersAdapter = new PlayersAdapter(getActivity());

        model = ViewModelProviders.of(this).get(GameNewsViewModel.class);
        model.getNewsByGame(game).observe(this, new Observer<List<New>>() {
            @Override
            public void onChanged(@Nullable List<New> newList) {
                newsAdapter.fillNews(newList);
            }
        });
        model.getPlayersByGame(game).observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(@Nullable List<Player> playerList) {
                playersAdapter.fillPlayers(playerList);
            }
        });

        viewPagerAdapter.addFragment(NewsByGameFragment.newInstance(newsAdapter),"News");
        viewPagerAdapter.addFragment(TopPlayerFragment.newInstance(playersAdapter),"TOP PLAYERS");


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_container_fragment,container,false);
        tabLayout = view.findViewById(R.id.tablayout_fragment_container);
        viewPager = view.findViewById(R.id.viewpager_fragment_container);

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}
