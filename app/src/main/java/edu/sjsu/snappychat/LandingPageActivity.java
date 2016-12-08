package edu.sjsu.snappychat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import edu.sjsu.snappychat.fragment.chats.ChatFragment;
import edu.sjsu.snappychat.fragment.FriendsFragment;
import edu.sjsu.snappychat.fragment.HomeFragment;
import edu.sjsu.snappychat.fragment.SearchFragment;
import edu.sjsu.snappychat.model.User;
import edu.sjsu.snappychat.service.UserService;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.Util;

public class LandingPageActivity extends FragmentActivity implements FriendsFragment.OnFragmentInteractionListener {

    private BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        loadDataFromServer();
        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.noTopOffset();
        bottomBar.setItemsFromMenu(R.menu.menu_main, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottombaritemone) {
                    //Home
                    HomeFragment homeFragment = new HomeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, homeFragment).commit();

                } else if (menuItemId == R.id.bottombaritemtwo) {
                    //Friends
                    FriendsFragment friendsFragment = new FriendsFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, friendsFragment).commit();
                } else if (menuItemId == R.id.bottombaritemthree) {
                    //Search
                    SearchFragment searchFragment = new SearchFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, searchFragment).commit();
                } else if (menuItemId == R.id.bottombaritemfour) {
                    //chat
                    ChatFragment chatFragment = new ChatFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, chatFragment).commit();
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        });
    }

    private void loadDataFromServer() {
        if (UserService.getInstance().isDataLoaded() == false) {
            FirebaseDatabase.getInstance().getReference().child(Constant.USER_NODE).child(Util.cleanEmailID(UserService.getInstance().getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User currentUser = dataSnapshot.getValue(User.class);
                    UserService.getInstance().setNickName(currentUser.getNickName());
                    UserService.getInstance().setProfession(currentUser.getProfession());
                    UserService.getInstance().setLocation(currentUser.getLocation());
                    UserService.getInstance().setAboutMe(currentUser.getAboutMe());
                    UserService.getInstance().setInterests(currentUser.getInterests());
                    UserService.getInstance().setProfilePictureLocation(currentUser.getProfilePictureLocation());
                    UserService.getInstance().setDataLoaded(true);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("UserProfileActivity", "loadPost:onCancelled", databaseError.toException());
                }
            });
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}