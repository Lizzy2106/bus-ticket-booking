package com.example.liaison.agence;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.liaison.R;
import com.example.liaison.models.User;

import java.util.HashMap;
import java.util.Map;

public class AddBusAgenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus_agence);

        Button register = (Button) findViewById(R.id.register_button);
        EditText nom = findViewById(R.id.nom);
        EditText imma = findViewById(R.id.immatriculation);
        EditText place = findViewById(R.id.nbplace);
        TextView display_response = findViewById(R.id.display_response);
        String email = getIntent().getStringExtra("email");


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String get_name = nom.getText().toString();
                String get_imma = imma.getText().toString();
                String get_place = place.getText().toString();

                if(get_name.equals("")){
                    nom.setError("Ce champ ne peut pas rester vide");
                }
                else if(get_imma.equals("")){
                    imma.setError("Ce champ ne peut pas rester vide");
                }
                else if(get_place.equals("")){
                    place.setError("Ce champ ne peut pas rester vide");
                }
                else {
                    final String url = "https://liaison-api.herokuapp.com/api/bus/";
                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Intent intent = new Intent(getApplicationContext(), CompteAgenceActivity.class);
                                    startActivity(intent);
                                }
                            },
                            new Response.ErrorListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("Error.Response", String.valueOf(error.getMessage()));
                                    display_response.setText("Bus ajouté avec succès");
                                }
                            }
                    )
                    {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("email", email);
                            params.put("places", get_place);

                            return params;
                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(postRequest);
                    Intent intent = new Intent(getApplicationContext(), CompteAgenceActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}