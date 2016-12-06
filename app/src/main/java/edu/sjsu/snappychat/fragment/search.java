package edu.sjsu.snappychat.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import edu.sjsu.snappychat.R;
import edu.sjsu.snappychat.util.CustomSearchListAdapter;


/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link search.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class search extends ListFragment {

    private ListView list;
    private String[] emailID = {
            "mayuri.sapre@gmail.com",
            "jagruti.patil@gmail.com",
            "madhusapre@gmail.com",
            "akki@gmail.com",
            "kdChauhan@gmail.com",
            "sagar.bhoite@gmail.com",
            "raavi.patil@gmail.com ",
            "atitha@gmail.com",
            "nagesh@gmail.com",
            "purva.legacy@gmail.com"
    };
    private String[] nickName = {
            "mayu",
            "jagruti",
            "madhu",
            "akki",
            "kd",
            "sagar",
            "raavi",
            "atitha",
            "nagya",
            "purva"
    };

    public search() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        final ListView searchList = (ListView) getView().findViewById(R.id.search_list);

        CustomSearchListAdapter adapter=new CustomSearchListAdapter(getContext(), emailID, nickName);
        searchList.setAdapter(adapter);

        return view;
    }
}
