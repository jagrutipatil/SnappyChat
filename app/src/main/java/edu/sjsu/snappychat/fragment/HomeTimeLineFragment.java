package edu.sjsu.snappychat.fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.snappychat.LoginActivity;
import edu.sjsu.snappychat.R;
import edu.sjsu.snappychat.fragment.chats.ChatModel;
import edu.sjsu.snappychat.fragment.chats.ChatPage;
import edu.sjsu.snappychat.util.Util;

import static android.app.Activity.RESULT_OK;
import edu.sjsu.snappychat.UserProfileActivity;
import edu.sjsu.snappychat.datagenerate.DataGenerator;
import edu.sjsu.snappychat.model.TimeLineCard;
import edu.sjsu.snappychat.model.User;
import edu.sjsu.snappychat.service.DatabaseService;
import edu.sjsu.snappychat.service.UserService;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.Util;
/**
 * Created by I074841 on 12/13/2016.
 */

public class HomeTimeLineFragment extends Fragment {
    private DatabaseReference mDatabaseReference;
    private ImageView profilePic;
    private FloatingActionButton profile;

    @Nullable

    private static final int PICK_IMAGE_REQUEST = 2;
    private static final int CAMERA_CODE = 1;
    Bitmap myBitmap;
    private int PICK_IMAGE_MULTIPLE = 1;
    String from_user, to_user, newmsg, LoggedInUser, ImageDecode, from_user_nick_name, to_user_nick_name;

    private ImageButton camera;
    private Button post;
    private EditText postText;
    private List<String> imageArray;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri URI = data.getData();
            String[] FILE = { MediaStore.Images.Media.DATA };


            Cursor cursor = getActivity().getContentResolver().query(URI,
                    FILE, null, null, null);

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(FILE[0]);
            ImageDecode = cursor.getString(columnIndex);
            cursor.close();

            File imgFile = new File(ImageDecode);
            if(imgFile.exists()) {
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                String img = Util.encodeImage(myBitmap);
                imageArray.add(img);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timeline_item, container, false);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        setHasOptionsMenu(true);

        final RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        RVAdapter adapter = new RVAdapter(null);
        rv.setAdapter(adapter);
        imageArray = new ArrayList<String>();

        camera = (ImageButton) view.findViewById(R.id.camera);
        post = (Button) view.findViewById(R.id.post);
        postText = (EditText) view.findViewById(R.id.post_text);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseReference.child(Constant.USER_NODE).child(Util.cleanEmailID(UserService.getInstance().getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        TimeLineCard card = new TimeLineCard();

                        User user = dataSnapshot.getValue(User.class);
                        card.setNickName(user.getNickName());
                        card.setProfilePicture(user.getProfilePictureLocation());
                        card.setUserUpdatedText(postText.getText().toString());
                        card.setEmailAddress(UserService.getInstance().getEmail());
                        List<String> imageArray1 = card.getListOfUploadedImage();
                        imageArray1.addAll(imageArray);
                        card.setListOfUploadedImage(imageArray1);

                        long timeStamp = System.currentTimeMillis()/1000;
                        String time = Long.toString(timeStamp);
                        imageArray.clear();

                        //Write in database
                        mDatabaseReference.child(Constant.TIMELINE_NODE).child(Util.cleanEmailID(UserService.getInstance().getEmail())).child(time).setValue(card);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                builder.setTitle("Add Photo!");
                builder.setIcon(R.drawable.faceicon);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Take Photo")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, CAMERA_CODE);
                        } else if (items[item].equals("Choose from Library")) {
                            Intent intent = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                            //intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        //Pass timeline object here as a list. Add an event where on child update set the adapter.
        mDatabaseReference.child(Constant.TIMELINE_NODE).child(Util.cleanEmailID(UserService.getInstance().getEmail())).orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<TimeLineCard> listOfTimeLineCard = new ArrayList<>();
                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                for (DataSnapshot snapshot : iterable) {
                    listOfTimeLineCard.add(snapshot.getValue(TimeLineCard.class));
                }
                //setFields();
                RVAdapter adapter = new RVAdapter(listOfTimeLineCard);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

/*        mDatabaseReference.child(Constant.TIMELINE_NODE).child(Util.cleanEmailID(UserService.getInstance().getEmail())).orderByKey().addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
              List<TimeLineCard> listOfTimeLineCard = new ArrayList<TimeLineCard>();
                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                for (DataSnapshot snapshot : iterable) {
                    listOfTimeLineCard.add(snapshot.getValue(TimeLineCard.class));
                }
                //setFields();
                RVAdapter adapter = new RVAdapter(listOfTimeLineCard);
                rv.setAdapter(adapter);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                List<TimeLineCard> listOfTimeLineCard = new ArrayList<>();
                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                for (DataSnapshot snapshot : iterable) {
                    listOfTimeLineCard.add(snapshot.getValue(TimeLineCard.class));
                }
                //setFields();
                RVAdapter adapter = new RVAdapter(listOfTimeLineCard);
                rv.setAdapter(adapter);
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("HomeFragment", "loadPost:onCancelled", databaseError.toException());
            }
        });
*/
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!UserService.getInstance().isDataLoaded()) {
            loadDataFromServer();

        }
    }

    private void loadDataFromServer() {
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
                //setFields();
                //DataGenerator.writeDummyTimeLineData();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("HomeFragment", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent edit_page = new Intent(getContext(), UserProfileActivity.class);
                startActivity(edit_page);
                return true;
            case R.id.action_logout:
                DatabaseService.setAvailabilityStatus(UserService.getInstance().getEmail(), Constant.AVAILABILITY_STATUS_OFFLINE);
                Intent loginActivity = new Intent(getActivity(), LoginActivity.class);
                startActivity(loginActivity);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
