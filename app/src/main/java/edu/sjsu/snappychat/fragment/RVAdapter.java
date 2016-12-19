package edu.sjsu.snappychat.fragment;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.snappychat.R;
import edu.sjsu.snappychat.model.TimeLineCard;
import edu.sjsu.snappychat.util.Util;

/**
 * Created by I074841 on 12/13/2016.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {

    List<TimeLineCard> listOfTimeLineCard;

    RVAdapter(List<TimeLineCard> listOfTimeLineCard) {
        this.listOfTimeLineCard = listOfTimeLineCard == null ? new ArrayList<TimeLineCard>() : listOfTimeLineCard;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_timeline, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public int getItemCount() {
        return listOfTimeLineCard.size();
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.nickNameHeader.setText(listOfTimeLineCard.get(i).getNickName());
        personViewHolder.userText.setText(listOfTimeLineCard.get(i).getUserUpdatedText());
        //personViewHolder.personAge.setText(persons.get(i).age);
        if (listOfTimeLineCard.get(i).getProfilePicture() != null)
            personViewHolder.personPhoto.setImageBitmap(Util.decodeImage(listOfTimeLineCard.get(i).getProfilePicture()));
        List<String> listOfUploadedImageLocation = listOfTimeLineCard.get(i).getListOfUploadedImage();
        for (int j = 0; j < listOfUploadedImageLocation.size(); j++) {
            if (listOfUploadedImageLocation.get(j) != null)
                personViewHolder.uploadImages.get(j).setImageBitmap(Util.decodeImage(listOfUploadedImageLocation.get(j)));
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView nickNameHeader;
        TextView userText;
        // TextView personAge;
        ImageView personPhoto;
        List<ImageView> uploadImages = new ArrayList<>();

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            nickNameHeader = (TextView) itemView.findViewById(R.id.nickNameHeader);
            userText = (TextView) itemView.findViewById(R.id.usertext);
            //personAge = (TextView)itemView.findViewById(R.id.person_age);
            personPhoto = (ImageView) itemView.findViewById(R.id.person_photo);
            uploadImages.add((ImageView) itemView.findViewById(R.id.uploadedImage1));
            uploadImages.add((ImageView) itemView.findViewById(R.id.uploadedImage2));
            uploadImages.add((ImageView) itemView.findViewById(R.id.uploadedImage3));
            uploadImages.add((ImageView) itemView.findViewById(R.id.uploadedImage4));
            uploadImages.add((ImageView) itemView.findViewById(R.id.uploadedImage5));
            uploadImages.add((ImageView) itemView.findViewById(R.id.uploadedImage6));
            uploadImages.add((ImageView) itemView.findViewById(R.id.uploadedImage7));
            uploadImages.add((ImageView) itemView.findViewById(R.id.uploadedImage8));
            uploadImages.add((ImageView) itemView.findViewById(R.id.uploadedImage9));
            uploadImages.add((ImageView) itemView.findViewById(R.id.uploadedImage10));

        }
    }


}
