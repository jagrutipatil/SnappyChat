package edu.sjsu.snappychat.fragment.chats;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.FirebaseListAdapter;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import edu.sjsu.snappychat.BaseAppCompatActivity;
import edu.sjsu.snappychat.R;
import edu.sjsu.snappychat.model.UserChatList;
import edu.sjsu.snappychat.model.UserFriend;
import edu.sjsu.snappychat.service.UserService;
import edu.sjsu.snappychat.util.Constant;
import edu.sjsu.snappychat.util.Util;

public class ChatPage extends BaseAppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 2;
    private static final int CAMERA_CODE = 1;

    EditText editText;
    ImageButton sendbutton, imagebutton;

    ArrayList<ChatModel> chatmsgsList = new ArrayList<ChatModel>();
    ChatAdapter adapter;
    private ListView chatList;
    Firebase firebase_chatnode ;
    Firebase ref_chatchildnode1 = null;
    Firebase ref_chatchildnode2 = null;
    String from_user, to_user, newmsg, LoggedInUser, ImageDecode, from_user_nick_name, to_user_nick_name;
    Bitmap myBitmap;;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri URI = data.getData();
            String[] FILE = { MediaStore.Images.Media.DATA };


            Cursor cursor = getContentResolver().query(URI,
                    FILE, null, null, null);

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(FILE[0]);
            ImageDecode = cursor.getString(columnIndex);
            cursor.close();

            File imgFile = new File(ImageDecode);
            if(imgFile.exists()) {
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            }

            //imageViewLoad.setImageBitmap(BitmapFactory.decodeFile(ImageDecode));

            /*Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");*/
            ChatModel m = new ChatModel();
            Intent startingintent = getIntent();
            LoggedInUser = startingintent.getStringExtra("LOG_IN_USER");
            m.setSender(LoggedInUser);
            String img = Util.encodeImage(myBitmap);
            m.setImagemessage(img);
            ref_chatchildnode1.push().setValue(m);
            ref_chatchildnode2.push().setValue(m);
            checkUser();

        } else if(requestCode == CAMERA_CODE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ChatModel m = new ChatModel();
            Intent startingintent = getIntent();
            LoggedInUser = startingintent.getStringExtra("LOG_IN_USER");
            m.setSender(LoggedInUser);
            String img = Util.encodeImage(imageBitmap);
            m.setImagemessage(img);
            ref_chatchildnode1.push().setValue(m);
            ref_chatchildnode2.push().setValue(m);
            checkUser();
        }
    }

    private void checkUser() {
        mDatabaseReference.child(Constant.CHAT_LIST).child(from_user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                UserChatList currentUser = dataSnapshot.getValue(UserChatList.class);
                if (currentUser != null) {
                    ArrayList<String> chats = currentUser.getChats();
                    if(!chats.contains(to_user)) {
                        chats.add(to_user);
                        currentUser.setChats(chats);
                        mDatabaseReference.child(Constant.CHAT_LIST).child(from_user).setValue(currentUser);
                    }
                } else {
                    ArrayList<String> chatsList = new ArrayList<String>();
                    chatsList.add(to_user);
                    UserChatList userFriend = new UserChatList(chatsList);
                    mDatabaseReference.child(Constant.CHAT_LIST).child(Util.cleanEmailID(from_user)).setValue(userFriend);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("chat add", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        Firebase.setAndroidContext(this);


        firebase_chatnode = new Firebase("https://snappychat-25a5a.firebaseio.com/chats");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        editText = (EditText) findViewById(R.id.editText);

        sendbutton = (ImageButton) findViewById(R.id.sendButton);
        imagebutton = (ImageButton) findViewById(R.id.imageButton) ;

        Intent startingintent = getIntent();

        from_user = startingintent.getStringExtra("FROM_USER");
        to_user = startingintent.getStringExtra("TO_USER");
        LoggedInUser = startingintent.getStringExtra("LOG_IN_USER");
        from_user_nick_name = startingintent.getStringExtra("FROM_USER_NICK_NAME");
        to_user_nick_name = startingintent.getStringExtra("TO_USER_NICK_NAME");

        chatmsgsList = new ArrayList<ChatModel>();
        chatList = (ListView) findViewById(R.id.recycler_view);

        adapter = new ChatAdapter();
        chatList.setAdapter(adapter);
        chatList.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        chatList.setStackFromBottom(true);


        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(to_user_nick_name);
        actionbar.setDefaultDisplayHomeAsUpEnabled(true);



        Log.v("NODE CREATED:", from_user + " " + to_user);


        ref_chatchildnode1 = firebase_chatnode.child(from_user + " " + to_user);

        ref_chatchildnode2 = firebase_chatnode.child(to_user + " " + from_user);





        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newmsg = editText.getText().toString().trim();
                ChatModel m = new ChatModel();
                m.setSender(from_user_nick_name);
                m.setReceiver(to_user_nick_name);
                m.setMessage(newmsg);

                ref_chatchildnode1.push().setValue(m);
                ref_chatchildnode2.push().setValue(m);
                checkUser();
                editText.setText("");


            }
        });

        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ChatPage.this);
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
                                startActivityForResult(intent,PICK_IMAGE_REQUEST);
                            } else if (items[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                }
        });

        ref_chatchildnode1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatModel chatmsg = dataSnapshot.getValue(ChatModel.class);
                    chatmsgsList.add(chatmsg);
                    adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });




    }



    private class ChatAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return chatmsgsList.size();
        }

        @Override
        public ChatModel getItem(int arg0) {
            return chatmsgsList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int pos, View v, ViewGroup arg2) {
            ChatModel c = getItem(pos);
            String currentUser = Util.cleanEmailID(UserService.getInstance().getEmail());
            if (c.getSender().matches(from_user_nick_name) ) {
                if(c.getImagemessage() != null){
                    v = getLayoutInflater().inflate(R.layout.chat_item_sent_img,null);
                }else {
                    v = getLayoutInflater().inflate(R.layout.chat_item_sent, null);
                }
            }else if(c.getSender() != currentUser) {
                if(c.getImagemessage() != null){
                    v = getLayoutInflater().inflate(R.layout.chat_item_rcv_img,null);
                }else {
                    v = getLayoutInflater().inflate(R.layout.chat_item_rcv, null);
                }
            }
            TextView lbl = (TextView) v.findViewById(R.id.lbl1);
            ImageView img = (ImageView) v.findViewById(R.id.lb4);
            lbl.setText(c.getSender());

            if(c.getMessage() != null) {
                lbl = (TextView) v.findViewById(R.id.lbl2);
                lbl.setText(c.getMessage());
            }

            if(c.getImagemessage()!=null) {
                img = (ImageView) v.findViewById(R.id.lb4);
                Bitmap imgmsg = Util.decodeImage(c.getImagemessage());
                img.setImageBitmap(imgmsg);
            }

            return v;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}



