package edu.sjsu.snappychat;

import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import edu.sjsu.snappychat.fragment.FriendsFragment;
import edu.sjsu.snappychat.fragment.HomeFragment;

public class LandingPageActivity extends AppCompatActivity implements FriendsFragment.OnFragmentInteractionListener{

    private BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        bottomBar = BottomBar.attach(this,savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.menu_main, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if(menuItemId == R.id.bottombaritemone){
                    //Home
                    HomeFragment homeFragment = new HomeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame,homeFragment).commit();
                }else if(menuItemId == R.id.bottombaritemtwo){
                    //Friends
                    FriendsFragment friendsFragment = new FriendsFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame,friendsFragment).commit();
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
}
