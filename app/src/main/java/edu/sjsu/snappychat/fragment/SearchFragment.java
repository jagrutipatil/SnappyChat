package edu.sjsu.snappychat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.sjsu.snappychat.R;
import edu.sjsu.snappychat.model.AdvancedSettings;
import edu.sjsu.snappychat.model.Mapping;
import edu.sjsu.snappychat.model.User;
import edu.sjsu.snappychat.model.UserFriend;
import edu.sjsu.snappychat.service.UserService;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.CustomSearchListAdapter;
import edu.sjsu.snappychat.util.Util;


/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private User loggedInUser;
    private DatabaseReference mDatabaseReference;

    private ListView list;
    private List<String> emailID;
    private SearchView searchView;
    private CustomSearchListAdapter adapter;
    private Mapping mapObject;
    private RadioButton searchByName;

    public SearchFragment() {
        // Get a list of all public visible users
        this.loggedInUser = UserService.getInstance().getUser();
        this.mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        emailID = new ArrayList<String>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchByName = (RadioButton) view.findViewById(R.id.by_email);

        searchView = (SearchView) view.findViewById(R.id.search_list);
        searchView.setQueryHint("Search...");

        mDatabaseReference.child(Constant.ADVANCED_SETTINGS).orderByChild("visibility").equalTo(Constant.PUBLIC_VISIBILITY).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AdvancedSettings settings = null;

                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    settings = snap.getValue(AdvancedSettings.class);
                    if (settings.getEmail_id().equals(loggedInUser.getEmail())) {
                        continue;
                    }
                    emailID.add(settings.getEmail_id());
                }

                mDatabaseReference.child(Constant.FRIENDS_NODE).child(Util.cleanEmailID(UserService.getInstance().getEmail())).child(Constant.FRIENDS_NODE).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //  UserFriend friends = null;

                        final ArrayList<String> friendList = (ArrayList<String>) dataSnapshot.getValue();

                        if (friendList != null) {
                            for (String emailIDString : friendList) {
                                if (friendList.contains(emailIDString))
                                    continue;
                                emailID.add(emailIDString);
                            }

                        }

                        mDatabaseReference.child(Constant.USER_NODE).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                long count = dataSnapshot.getChildrenCount();
                                final HashMap<String, String> emailToInterestMap = new HashMap<String, String>();

                                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                    User user = snap.getValue(User.class);
                                    if (emailID.contains(user.getEmail())) {
                                        emailToInterestMap.put(user.getEmail(), user.getInterests());
                                    }
                                }

                                mDatabaseReference.child(Constant.MAPPING).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        mapObject = dataSnapshot.getValue(Mapping.class);


                                        final ListView searchList = (ListView) view.findViewById(R.id.list);

                                        if (getActivity() != null) {
                                            adapter = new CustomSearchListAdapter(getActivity(), getContext(), emailID, friendList, emailToInterestMap, mapObject);
                                            searchList.setAdapter(adapter);

                                            searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                    //go to that user's profile
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                adapter.getFilter().filter(text);
                return false;
            }
        });
        return view;
    }
}