package edu.sjsu.snappychat.fragment.friends;

import android.os.Bundle;
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
import edu.sjsu.snappychat.model.Invitations;
import edu.sjsu.snappychat.model.Mapping;
import edu.sjsu.snappychat.model.User;
import edu.sjsu.snappychat.service.UserService;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.FriendInvitationAdapter;
import edu.sjsu.snappychat.util.Util;


public class RequestSentFragment extends Fragment {
    private List<String> emailIds;
    private List<String> nickNames;
    private DatabaseReference mDatabaseReference;

    public RequestSentFragment() {
        this.mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        this.emailIds = new ArrayList<String>();
        this.nickNames = new ArrayList<String>();



    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       final View view = inflater.inflate(R.layout.fragment_reqeust_sent, container, false);

        mDatabaseReference.child(Constant.INVITATIONS_NODE).child(Util.cleanEmailID(UserService.getInstance().getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Invitations invitations = dataSnapshot.getValue(Invitations.class);
                if (invitations != null && invitations.getInvitationSent()!=null) {
                    emailIds = invitations.getInvitationSent();
                    //nickNames = invitations.getInvitationSent();

                    mDatabaseReference.child(Constant.MAPPING).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Mapping mapObject = dataSnapshot.getValue(Mapping.class);

                            for(String email:emailIds){
                                nickNames.add(mapObject.getNickName(Util.cleanEmailID(email)));
                            }

                            ListView searchList = (ListView) view.findViewById(R.id.sentrequestlist);

                            FriendInvitationAdapter adapter = new FriendInvitationAdapter(getContext(), emailIds, nickNames, false);
                            searchList.setAdapter(adapter);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }


}
