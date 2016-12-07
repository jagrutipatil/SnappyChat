package edu.sjsu.snappychat.service;

import edu.sjsu.snappychat.model.AdvancedSettigs;
import edu.sjsu.snappychat.model.Friend;
import edu.sjsu.snappychat.model.Invitations;
import edu.sjsu.snappychat.model.User;
import edu.sjsu.snappychat.model.UserFriend;

/**
 * Created by jagruti on 12/4/16.
 */

public class UserService {
    private static User user = null;
    private static AdvancedSettigs settings = null;
    private static UserFriend friends = null;
    private static Invitations invitations = null;

    static private UserService instance = null;

    private boolean dataLoaded = false;

    private UserService() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
            user = new User();
            settings = new AdvancedSettigs("FriendsOnly", true);
            friends = new UserFriend();
            invitations = new Invitations();

        }
        return instance;
    }

    public static UserFriend getFriends() {
        return friends;
    }

    public static Invitations getInvitations() {
        return invitations;
    }


    public void setInterests(String interests){
        user.setInterests(interests);
    }

    public void setAboutMe(String aboutMe){
        user.setAboutMe(aboutMe);
    }

    public String getProfilePictureLocation() {
        return user.getProfilePictureLocation();
    }

    public void setProfilePictureLocation(String profilePictureLocation) {
        user.setProfilePictureLocation(profilePictureLocation);
    }

    public String getNickName() {
        return user.getNickName();
    }

    public void setNickName(String nickName) {
        user.setNickName(nickName);
    }

    public String getEmail() {
        return user.getEmail();
    }

    public void setEmail(String email) {
        user.setEmail(email);
        settings.setEmail_id(email);
        friends.setEmail(email);
    }

    public String getLocation() {
        return user.getLocation();
    }

    public void setLocation(String location) {
        user.setLocation(location);
    }

    public String getInterests() {
        return user.getInterests();
    }

    public String getAboutMe() {
        return user.getAboutMe();
    }

    public String getProfession() {
        return user.getProfession();
    }

    public void setProfession(String profession) {
        user.setProfession(profession);
    }

    public void setAdvancedSettings(AdvancedSettigs settings){
        settings.setVisibility(settings.getVisibility());
        settings.setEmail_notification(settings.isEmail_notification());
    }

    public User getUser(){
        return user;
    }

    public AdvancedSettigs getAdvancedSettings(){
        return settings;
    }

    public boolean isDataLoaded() {
        return dataLoaded;
    }

    public void setDataLoaded(boolean dataLoaded) {
        dataLoaded = dataLoaded;
    }
}
