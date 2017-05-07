package com.sheatouk.selmy.componentsdonationasu.DialogFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sheatouk.selmy.componentsdonationasu.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private int Flag ;
    public SearchFragment() {
    }

    public SearchFragment(int installed) {
        Flag = installed;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String[] strings = {getString(R.string.installed), getString(R.string.all)};
        List<String> list = new ArrayList<>(30);
        Random random = new Random();
        while (list.size() < 30) {
            list.add(strings[random.nextInt(strings.length)]);
        }

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        return recyclerView;
    }

}
