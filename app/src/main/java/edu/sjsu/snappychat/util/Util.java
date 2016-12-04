package edu.sjsu.snappychat.util;

/**
 * Created by I074841 on 12/4/2016.
 */

public class Util {

    public static String cleanEmailID(String email) {
        String regex = "[^A-Za-z0-9]";
        return email.replaceAll(regex, "");
    }
}
