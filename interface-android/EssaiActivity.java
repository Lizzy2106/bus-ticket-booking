package com.example.liaison;

import android.os.Bundle;

import com.example.liaison.data.DatabaseHandler;
import com.example.liaison.models.User;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EssaiActivity extends AppCompatActivity {

    private ListView contact_list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> contactsToString = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_essai);

        DatabaseHandler db = new DatabaseHandler(EssaiActivity.this);
        contact_list = findViewById(R.id.list_view);

        List<User> users = db.getAllUsers();

        for (User cnt: users) {
            contactsToString.add(cnt.getName()+": "+cnt.getMail()+": "+cnt.getMdp()+": "+cnt.getType());
        }
        adapter = new ArrayAdapter<String>(
                EssaiActivity.this,
                android.R.layout.simple_list_item_1,
                contactsToString);
        contact_list.setAdapter(adapter);
        contact_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("VContact","onItemClick: " + contactsToString.get(position));
                db.getUser(position);
                //contact_list.remove(position);
                contactsToString.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
    }
}