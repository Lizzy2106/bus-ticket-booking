package com.example.liaison;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    String type_ag = "AG";
    String type_pa = "PA";

    Object self = this;
    private EditText username;
    EditText mail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        DatabaseHandler db = new DatabaseHandler(RegisterActivity.this);

        this.username = findViewById(R.id.edit_name);
        EditText mail = findViewById(R.id.edit_mail);
        EditText mdp = findViewById(R.id.edit_mdp);
        EditText name_agency = findViewById(R.id.edit_name_agency);
        EditText description_agency =findViewById(R.id.edit_description);
        TextView display_error = findViewById(R.id.display_error);

        Button redirect_login_button = findViewById(R.id.redirect_register_button);
        Button submit_register_button = findViewById(R.id.submit_register_button);
        CheckBox as_agency = findViewById(R.id.agency_button);

        as_agency.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                name_agency.setVisibility( isChecked ? View.VISIBLE : View.INVISIBLE);
                description_agency.setVisibility( isChecked ? View.VISIBLE : View.INVISIBLE);
            }
        });

        submit_register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String get_name = username.getText().toString();
                String get_mail = mail.getText().toString();
                String get_mdp = mdp.getText().toString();
                String get_agence_name = name_agency.getText().toString();
                String get_agence_description = description_agency.getText().toString();

                if (get_name.equals("")){
                    username.setError("Ce champ ne peut pas rester vide");
                }
                else if(get_mail.equals("")){
                    mail.setError("Ce champ ne peut pas rester vide");
                }
                else if(get_mdp.equals("")){
                    mail.setError("Ce champ ne peut pas rester vide");
                }
                else if(!get_name.matches("[A-Za-z0-9]+")){
                    username.setError("Uniquement des caractères alphanumériques");
                }
                else if(!get_mail.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")){
                    mail.setError("Cette adresse n'est pas valide");
                }
                else if(get_name.length()<3){
                    username.setError("Au moins 03 caractères");
                }
                else if(get_mdp.length()<5){
                    mdp.setError("Au moins 05 caractères");
                }
                else{
                    if (as_agency.isChecked()){
                        if(get_agence_name.equals("")){
                            mail.setError("Ce champ ne peut pas rester vide");
                        }
                        else if(get_agence_description.equals("")){
                            description_agency.setError("Ce champ ne peut pas rester vide");
                        }
                        else if(!get_agence_name.matches("[A-Za-z0-9]+")){
                            name_agency.setError("Uniquement des caractères alphanumériques");
                        }
                        else if(get_agence_description.length()<10){
                            username.setError("Au moins 10 caractères");
                        }
                    }

                        //Toast.makeText(getApplicationContext(), "amedee", Toast.LENGTH_LONG).show();

                    final String url = "https://liaison-api.herokuapp.com/api/auth/register";
                    JSONObject postData = new JSONObject();

                    try {
                        postData.put("email", get_mail);
                        postData.put("username", get_name);
                        postData.put("type", type_ag);
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
                                        email = response.getJSONObject("data").getJSONObject("user").getString("email");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        type = response.getJSONObject("data").getString("type");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
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
                        /*@Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("email", get_mail);
                            params.put("username", get_name);
                            params.put("password", get_mdp);
                            params.put("type", type_ag);

                            return params;
                        }*/
                    };
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(postRequest);

                }
            }
        });

        redirect_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, ConnexionActivity.class);
                startActivity(intent);
            }
        });
    }
}