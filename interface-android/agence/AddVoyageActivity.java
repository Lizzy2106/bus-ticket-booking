package com.example.liaison.agence;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.liaison.R;

public class AddVoyageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_voyage_agence);

        Button register = (Button) findViewById(R.id.register);
        EditText date = findViewById(R.id.date_depart);
        EditText heure = findViewById(R.id.heure_depart);
        Spinner bus = findViewById(R.id.bus);
        Spinner depart = findViewById(R.id.lieu_depart_spinner);
        Spinner arrive = findViewById(R.id.lieu_arrive_spinner);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String get_date = date.getText().toString();
                String get_heure = heure.getText().toString();
                //String depart = .getText().toString();

                if(get_date.equals("")){
                    date.setError("Ce champ ne peut pas rester vide");
                }
                else if(get_heure.equals("")){
                    heure.setError("Ce champ ne peut pas rester vide");
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), CompteAgenceActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}