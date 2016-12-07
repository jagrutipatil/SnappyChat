package edu.sjsu.snappychat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.snappychat.R;
import edu.sjsu.snappychat.model.AdvancedSettigs;
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
    private List<String> nickName;
    /*
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
    };*/

    public SearchFragment() {
        // Get a list of all public visible users
        this.loggedInUser = UserService.getInstance().getUser();
        this.mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        emailID = new ArrayList<String>();
        nickName = new ArrayList<String>();

        //Populate emailId list

       /* //Populate nickName ArrayList
        for (int i = 0; i < emailID.size(); i++) {
            mDatabaseReference.child(Constant.USER_NODE).child(Util.cleanEmailID(emailID.get(i))).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    nickName.add(user.getNickName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        mDatabaseReference.child(Constant.ADVANCED_SETTINGS).orderByChild("visibility").equalTo(Constant.PUBLIC_VISIBILITY).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AdvancedSettigs settings = null;

                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    settings = snap.getValue(AdvancedSettigs.class);
                    if (settings.getEmail_id().equals(loggedInUser.getEmail())) {
                        continue;
                    }
                    emailID.add(settings.getEmail_id());
                    //nickName.add(settings.getEmail_id());//change to nickname
                }

                mDatabaseReference.child(Constant.FRIENDS_NODE).child(Util.cleanEmailID(loggedInUser.getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserFriend friends = null;

                        for (DataSnapshot snap : dataSnapshot.getChildren()) {

                            friends = snap.getValue(UserFriend.class);
                            if (friends != null) {
                                emailID.addAll(friends.getFriends());
                                //nickName.addAll(friends.getFriends());//change to nickname
                            }
                        }

                        mDatabaseReference.child(Constant.MAPPING).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Mapping mapping = dataSnapshot.getValue(Mapping.class);

                                int count = emailID.size();
                                for(int i=0;i<count;i++){
                                    nickName.add(i, mapping.getNickName(Util.cleanEmailID(emailID.get(i))));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        final ListView searchList = (ListView) view.findViewById(R.id.list);

                        CustomSearchListAdapter adapter = new CustomSearchListAdapter(getContext(), emailID, nickName, friends);
                        searchList.setAdapter(adapter);

                        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                //go to that user's profile
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


        return view;
    }
}
