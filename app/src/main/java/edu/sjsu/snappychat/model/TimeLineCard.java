package edu.sjsu.snappychat.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by I074841 on 12/17/2016.
 */

public class TimeLineCard {
    private String nickName;
    private String profilePicture;
    private String userUpdatedText;
    private List<String> listOfUploadedImage = new ArrayList<>();
    public TimeLineCard(){}
    public TimeLineCard(String nickName, String profilePicture) {
        this.nickName = nickName;
        this.profilePicture = profilePicture;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUserUpdatedText() {
        return userUpdatedText;
    }

    public void setUserUpdatedText(String userUpdatedText) {
        this.userUpdatedText = userUpdatedText;
    }

    public void addToUploadImageList(String uploadImageContent) {
        if (listOfUploadedImage != null)
            listOfUploadedImage.add(uploadImageContent);
    }

    public List<String> getListOfUploadedImage() {
        return listOfUploadedImage;
    }
}
