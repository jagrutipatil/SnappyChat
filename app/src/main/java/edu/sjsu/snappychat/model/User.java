package edu.sjsu.snappychat.model;

/**
 * Created by Kamlendra Chauhan on 12/4/2016.
 */

public class User {
    public String nickName;
    public String email;
    public String profilePictureLocation;
    public String location;
    public String interests;
    public String aboutMe;
    public String profession;
    //public AdvancedSettings advancedSetting;

    public User(){
        //this.advancedSetting = new AdvancedSettings("Friends Only", true);
    }

    public User(String email){
        this.email = email;
        //this.advancedSetting = new AdvancedSettings("Friends Only", true);
    }

    public User(String email, String nickName, String Location, String profession){
        this.email = email;
        this.nickName  = nickName;
        this.location = Location;
        this.profession = profession;
    }

    public User(String profession, String nickName, String email, String profilePictureLocation, String location, String interests, String aboutMe) {
        this.profession = profession;
        this.nickName = nickName;
        this.email = email;
        this.profilePictureLocation = profilePictureLocation;
        this.location = location;
        this.interests = interests;
        this.aboutMe = aboutMe;
    }

    public void setInterests(String interests){
        this.interests = interests;
    }

    public void setAboutMe(String aboutMe){
        this.aboutMe = aboutMe;
    }

    public String getProfilePictureLocation() {
        return profilePictureLocation;
    }

    public void setProfilePictureLocation(String profilePictureLocation) {
        this.profilePictureLocation = profilePictureLocation;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInterests() {
        return interests;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    /*
    public void setAdvancedSettings(AdvancedSettings settings){
        this.advancedSetting = settings;
    }

    public AdvancedSettings getAdvancedSettings(){
        return this.advancedSetting;
    }*/

}
