package edu.sjsu.snappychat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import edu.sjsu.snappychat.util.CustomSearchListAdapter;

public class search extends AppCompatActivity {

    private ListView list;
    private String[] emailID = {
            "mayuri.sapre@gmail.com",
            "jagruti.patil@gmail.com",
            "madhusapre@gmail.com",
            "akki@gmail.com",
            "kdChauhan@gmail.com",
            "sagar.bhoite@gmail.com",
            "raavi.patil@gmail.com ",
            "atitha@gmail.com",
            "nagesh@gmail.com",
            "purva.legacy@gmail.com"
    };
    private String[] nickName = {
            "mayu",
            "jagruti",
            "madhu",
            "akki",
            "kd",
            "sagar",
            "raavi",
            "atitha",
            "nagya",
            "purva"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        CustomSearchListAdapter adapter=new CustomSearchListAdapter(this, emailID, nickName);
        list=(ListView)findViewById(R.id.search_listview);
        list.setAdapter(adapter);

        /*
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem= itemname[+position];
                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });*/
    }
}
