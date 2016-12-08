package edu.sjsu.snappychat.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.storage.StorageReference;

/**
 * Created by Akshatha Anantharmu on 12/4/16.
 */

public class UploadImage extends AsyncTask<Void, Object, Void> {

    private StorageReference mStorageRef;
    private ImageView profilePic;
    private Uri downloadUri;
    private Context context;
    private Bundle extras;

    public UploadImage(Context context, ImageView profilePic, Bundle extras) {
        this.context = context;
        this.profilePic = profilePic;
        this.extras = extras;
    }

    private Boolean isSuccess;

    @Override
    protected Void doInBackground(Void...params) {
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        encodeBitmapAndSaveToFirebase(imageBitmap);


//        mStorageRef = FirebaseStorage.getInstance().getReference();
//        StorageReference filePath = mStorageRef.child("profile").child(uri.getLastPathSegment());
//
//        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                downloadUri = taskSnapshot.getDownloadUrl();
//                isSuccess = true;
//            }
//        });

        return null;
    }

    private void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {


    }


    @Override
    protected void onPostExecute(Void aVoid) {
    }
}
