package edu.sjsu.snappychat.service;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import edu.sjsu.snappychat.MainActivity;

/**
 * Created by i856547 on 12/4/16.
 */

public class UploadImage extends AsyncTask<Void, Object, Void> {

    private StorageReference mStorageRef;
    private ImageView profilePic;
    private Uri downloadUri;
    private Context context;
    private Uri uri;

    public UploadImage(Context context, ImageView profilePic,Uri uri) {
        this.context = context;
        this.profilePic = profilePic;
        this.uri = uri;
    }

    private Boolean isSuccess;

    @Override
    protected Void doInBackground(Void...params) {

        //HashMap<String,String> map = maps[0];
        //Uri uri = Uri.parse( map.get("uri") );
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference filePath = mStorageRef.child("images").child(uri.getLastPathSegment());

        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                downloadUri = taskSnapshot.getDownloadUrl();



                isSuccess = true;
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Picasso.with(context).load(uri).fit().centerCrop().into(profilePic);
    }
}
