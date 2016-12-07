package edu.sjsu.snappychat;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import edu.sjsu.snappychat.datagenerate.DataGenerator;
import edu.sjsu.snappychat.fragment.FriendsFragment;
import edu.sjsu.snappychat.fragment.HomeFragment;
import edu.sjsu.snappychat.fragment.SearchFragment;
import edu.sjsu.snappychat.fragment.chats.ChatFragment;

public class LandingPageActivity extends FragmentActivity implements FriendsFragment.OnFragmentInteractionListener {

    private BottomBar bottomBar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

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


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void generateData() {
        DataGenerator.write();
    }
}