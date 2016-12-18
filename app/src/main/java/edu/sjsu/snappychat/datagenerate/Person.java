package edu.sjsu.snappychat.datagenerate;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.snappychat.R;

/**
 * Created by I074841 on 12/13/2016.
 */

public class Person {
    public String name;
    public String age;
    public int photoId;
    public int uploadedImage1;
    private List<Person> persons;


    public Person(String name, String age, int photoId, int uploadedImage1) {
        this.name = name;
        this.age = age;
        this.photoId = photoId;
        this.uploadedImage1 = uploadedImage1;
    }

    // This method creates an ArrayList that has three Person objects
// Checkout the project associated with this tutorial on Github if
// you want to use the same images.
    public void initializeData() {
        persons = new ArrayList<>();
        persons.add(new Person("Emma Wilson", "23 years old", R.drawable.ic_audiotrack, R.drawable.ic_audiotrack));
        persons.add(new Person("Lavery Maiss", "25 years old", R.drawable.ic_audiotrack_light, R.drawable.ic_audiotrack));
        persons.add(new Person("Lillie Watts", "35 years old", R.drawable.ic_black_profession, R.drawable.ic_audiotrack));
    }
}