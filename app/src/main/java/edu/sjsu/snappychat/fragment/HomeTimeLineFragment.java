package edu.sjsu.snappychat.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.sjsu.snappychat.R;
import edu.sjsu.snappychat.fragment.chats.ChatModel;
import edu.sjsu.snappychat.fragment.chats.ChatPage;
import edu.sjsu.snappychat.util.Util;

import static android.app.Activity.RESULT_OK;

/**
 * Created by I074841 on 12/13/2016.
 */

public class HomeTimeLineFragment extends Fragment {
    @Nullable

    private static final int PICK_IMAGE_REQUEST = 2;
    private static final int CAMERA_CODE = 1;
    Bitmap myBitmap;
    private int PICK_IMAGE_MULTIPLE = 1;
    String from_user, to_user, newmsg, LoggedInUser, ImageDecode, from_user_nick_name, to_user_nick_name;

    private ImageButton camera;
    private Button post;
    private List<String> imageArray;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*
        if (Intent.ACTION_SEND_MULTIPLE.equals(data.getAction())) {
            // retrieve a collection of selected images
            Intent intent = new Intent();
            ArrayList<Parcelable> list = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
            // iterate over these images
            if( list != null ) {
                for (Parcelable parcel : list) {
                    Uri uri = (Uri) parcel;
                    // TODO handle the images one by one here
                }
            }
        }*/

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

        /* else if(requestCode == CAMERA_CODE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ChatModel m = new ChatModel();
            Intent startingintent = getActivity().getIntent();
            LoggedInUser = startingintent.getStringExtra("LOG_IN_USER");
            m.setSender(LoggedInUser);
            String img = Util.encodeImage(imageBitmap);
            m.setImagemessage(img);
            ref_chatchildnode1.push().setValue(m);
            ref_chatchildnode2.push().setValue(m);
            checkUser();
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timeline_item, container, false);
        RecyclerView rv = (RecyclerView)view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        RVAdapter adapter = new RVAdapter(null);
        rv.setAdapter(adapter);
        imageArray = new ArrayList<String>();

        camera = (ImageButton) view.findViewById(R.id.camera);
        post = (Button) view.findViewById(R.id.post);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(String image:imageArray){
                    //do post/save in databse

                }
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

        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
