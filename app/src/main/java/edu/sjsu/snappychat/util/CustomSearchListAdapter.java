package edu.sjsu.snappychat.util;

/**
 * Created by mayur on 12/6/2016.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import edu.sjsu.snappychat.R;
import edu.sjsu.snappychat.model.User;
import edu.sjsu.snappychat.service.UserService;

public class CustomSearchListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] emailID;
    private final String[] nickName;
    private User loggedInUser;

    public CustomSearchListAdapter(Activity context, String[] email, String[] nickName) {
        super(context, R.layout.search_listview, email);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.emailID = email;
        this.nickName = nickName;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.search_listview, null,true);

        EditText email = (EditText) rowView.findViewById(R.id.email);
        EditText nickName = (EditText) rowView.findViewById(R.id.nickname);
        EditText friendTag = (EditText) rowView.findViewById(R.id.friend_tag);
        ImageButton goToProfile = (ImageButton) rowView.findViewById(R.id.go_profile);
        ImageButton addFriend = (ImageButton) rowView.findViewById(R.id.add_friend);

        email.setText(emailID[position]);
        nickName.setText(emailID[position]);
        loggedInUser = UserService.getInstance().getUser();

        /*
        if(loggedInUser.isFriend(emailID[position])){
            friendTag.setVisibility(rowView.VISIBLE);
            addFriend.setVisibility(rowView.INVISIBLE);
        }else{
            friendTag.setVisibility(rowView.INVISIBLE);
            addFriend.setVisibility(rowView.VISIBLE);
        }*/

        return rowView;

    };
}
