package edu.sjsu.snappychat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import edu.sjsu.snappychat.service.DatabaseService;
import edu.sjsu.snappychat.service.UserService;
import edu.sjsu.snappychat.util.Constant;

/**
 * Created by Kamlendra on 12/7/2016.
 */

public class BaseAppCompatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(getClass().getName(), "OnCreate Status changed to " + Constant.AVAILABILITY_STATUS_ONLINE);
        setUserStatus(Constant.AVAILABILITY_STATUS_ONLINE);
    }
    
    protected void setUserStatus(String status) {
        DatabaseService.setAvailabilityStatus(UserService.getInstance().getEmail(), status);
    }
}
