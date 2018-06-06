package com.bonusteam.gamenews.Fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import com.bonusteam.gamenews.Entity.User;
import com.bonusteam.gamenews.Model.GameNewsViewModel;
import com.bonusteam.gamenews.R;

import java.util.List;

public class ViewGameNewsFragment extends Fragment {

    private NewsAdapter newsAdapter;
    private PlayersAdapter playersAdapter;
    private GameNewsViewModel viewModel;
    private String categoryGame;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    public ViewGameNewsFragment() {
        // Required empty public constructor
    }

    public static ViewGameNewsFragment newInstance(String categoryGame) {
        ViewGameNewsFragment fragment = new ViewGameNewsFragment();
        fragment.setCategoryGame(categoryGame);
        return fragment;
    }


    public void setCategoryGame(String categoryGame){
        this.categoryGame = categoryGame;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPagerAdapter.clearFragments();
        newsAdapter = new NewsAdapter(getActivity());
        playersAdapter = new PlayersAdapter(getActivity());

        viewModel = ViewModelProviders.of(this).get(GameNewsViewModel.class);
        viewModel.getNewsByGame(categoryGame).observe(this, new Observer<List<New>>() {
            @Override
            public void onChanged(@Nullable List<New> newList) {
                newsAdapter.fillNews(newList);
            }
        });

        viewModel.getPlayersByGame(categoryGame).observe(this,new Observer<List<Player>>(){
            @Override
            public void onChanged(@Nullable List<Player> playerList) {
                playersAdapter.fillPlayers(playerList);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_game_news,container,false);


        viewPagerAdapter.addFragment(NewsByGameFragment.newInstance(newsAdapter),"News");
        viewPagerAdapter.addFragment(TopPlayerFragment.newInstance(playersAdapter),"Top Players");
        viewPagerAdapter.addFragment(new GalleryGameFragment(),"Gallery");

        tabLayout = view.findViewById(R.id.tablayout_news);
        viewPager = view.findViewById(R.id.viewpager_news);

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }



}
