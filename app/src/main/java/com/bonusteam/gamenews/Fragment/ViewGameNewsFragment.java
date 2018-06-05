package com.bonusteam.gamenews.Fragment;

import android.app.ActionBar;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bonusteam.gamenews.Activity.MainActivity;
import com.bonusteam.gamenews.Adapter.NewsAdapter;
import com.bonusteam.gamenews.Adapter.ViewPagerAdapter;
import com.bonusteam.gamenews.Entity.New;
import com.bonusteam.gamenews.Model.GameNewsViewModel;
import com.bonusteam.gamenews.R;

import java.util.List;

public class ViewGameNewsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private NewsAdapter adapter;
    private GameNewsViewModel viewModel;
    private View view;
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
        Log.d("FRAGMENTO CON TOOLBAR",categoryGame);
        adapter = new NewsAdapter(getActivity());
        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPagerAdapter.addFragment(NewsByGameFragment.newInstance(adapter),"News");
        viewPagerAdapter.addFragment(new TopPlayerFragment(),"Top Players");
        viewPagerAdapter.addFragment(new GalleryGameFragment(),"Gallery");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_game_news,container,false);

        viewModel = ViewModelProviders.of(this).get(GameNewsViewModel.class);
        viewModel.getNewsByGame(categoryGame).observe(this, new Observer<List<New>>() {
            @Override
            public void onChanged(@Nullable List<New> newList) {
                adapter.fillNews(newList);
            }
        });

        tabLayout = view.findViewById(R.id.tablayout_news);
        viewPager = view.findViewById(R.id.viewpager_news);

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
