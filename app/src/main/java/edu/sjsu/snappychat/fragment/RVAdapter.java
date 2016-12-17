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
import edu.sjsu.snappychat.datagenerate.Person;

/**
 * Created by I074841 on 12/13/2016.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{

    List<Person> persons;

    RVAdapter(List<Person> persons){
        this.persons = new ArrayList<>();
        this.persons.add(new Person("Emma Wilson", "23 years old", R.drawable.ic_audiotrack));
        this.persons.add(new Person("Lavery Maiss", "25 years old", R.drawable.ic_audiotrack_light));
        this.persons.add(new Person("Lillie Watts", "35 years old", R.drawable.ic_black_profession));
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView personName;
        TextView personAge;
        ImageView personPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personAge = (TextView)itemView.findViewById(R.id.person_age);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_timeline, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }
    @Override
    public int getItemCount() {
        return persons.size();
    }
    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.personName.setText(persons.get(i).name);
        personViewHolder.personAge.setText(persons.get(i).age);
        personViewHolder.personPhoto.setImageResource(persons.get(i).photoId);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
