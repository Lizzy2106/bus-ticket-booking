package com.example.liaison;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.liaison.agence.CompteAgenceActivity;
import com.example.liaison.data.DatabaseHandler;
import com.example.liaison.models.User;
import com.example.liaison.root.CompteRootActivity;

import java.util.HashMap;
import java.util.Map;

import com.example.liaison.utils.Error;

import org.json.JSONException;
import org.json.JSONObject;

public class ConnexionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        Button login = (Button) findViewById(R.id.login);

        EditText edit_mail = findViewById(R.id.edit_mail);
        EditText edit_mdp = findViewById(R.id.edit_mdp);
        Button redirect_register_button = findViewById(R.id.redirect_register_button);
        Button redirect_reset_button = findViewById(R.id.redirect_reset_button);
        TextView display_error = findViewById(R.id.display_error);

        redirect_register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        redirect_reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ResetActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String get_mail = edit_mail.getText().toString();
                String get_mdp = edit_mdp.getText().toString();

                if (get_mail.equals("")) {
                    edit_mail.setError("Ce champ ne peut pas rester vide");
                } else if (get_mdp.equals("")) {
                    edit_mail.setError("Ce champ ne peut pas rester vide");
                } else if (!get_mail.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
                    edit_mail.setError("Cette adresse n'est pas valide");
                } else if (get_mdp.length() < 6) {
                    edit_mdp.setError("Au moins 06 caractères");
                } else {
                    final String url = "https://liaison-api.herokuapp.com/api/auth/login";
                    JSONObject postData = new JSONObject();

                    try {
                        postData.put("email", get_mail);
                        postData.put("password", get_mdp);
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,postData,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    //Log.d("id", response);
                                    //User user = new User(get_name, get_mail, get_mdp, type_ag);
                                    //db.addUser(user);
                                    //Toast.makeText(getApplicationContext(), "amedee", Toast.LENGTH_LONG).show();
                                    String  new_username, email, type;
                                    try {
                                        new_username = response.getJSONObject("data").getJSONObject("user").getString("username");
                                        //Toast.makeText(getApplicationContext(), String.valueOf(new_username), Toast.LENGTH_LONG).show();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        type = response.getJSONObject("data").getJSONObject("user").getString("email");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        type = response.getJSONObject("data").getString("type");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    //if ()
                                    Intent intent = new Intent(getApplicationContext(), CompteAgenceActivity.class);
                                    startActivity(intent);
                                }
                            },
                            new Response.ErrorListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //Log.d("Error.Response", String.valueOf(error.getMessage()));
                                    //display_error.setText("Nous ne pouvons pas enrégistrer cet utilisateur");
                                    display_error.setText(String.valueOf(error.networkResponse.statusCode));
                                }
                            }
                        )
                    {
                    };
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(postRequest);

                }
                    Cursor cursor = (Cursor) db.searchUser(get_mail);
                    if (cursor != null && cursor.moveToFirst()) {
                        String type = cursor.getString(4);
                        if (String.valueOf(type).equals("PA")) {
                            Intent intent = new Intent(getApplicationContext(), CompteUserActivity.class);
                            startActivity(intent);
                        } else if (String.valueOf(type).equals("AG")) {
                            Intent intent = new Intent(getApplicationContext(), CompteAgenceActivity.class);
                            intent.putExtra("email", get_mail);
                            startActivity(intent);
                        }
                    } else if (get_mail.equals("admin@gmail.com") && get_mdp.equals("admin")) {
                        Intent intent = new Intent(getApplicationContext(), CompteRootActivity.class);
                        startActivity(intent);
                    } else {
                        display_error.setText("Cet utilisateur n'existe pas. Redirigez-vous vers la page d'enrégistrement");
                        Intent intent = new Intent(getApplicationContext(), CompteUserActivity.class);
                        startActivity(intent);
                    }
                }
            //}
        });
    }
}