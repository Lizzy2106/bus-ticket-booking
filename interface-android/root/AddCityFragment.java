package com.example.liaison.root;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.liaison.CompteUserActivity;
import com.example.liaison.R;
import com.example.liaison.agence.LogoutAgenceFragment;
import com.example.liaison.models.User;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCityFragment extends Fragment {

    public static AddCityFragment newInstance() {
        return new AddCityFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_add_city, container, false);

        EditText name_city = (EditText) result.findViewById(R.id.edit_city);
        Button register_city = (Button) result.findViewById(R.id.register_city_button);
        TextView display_response = result.findViewById(R.id.display_response);


        register_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = name_city.getText().toString();
                final String url = "https://liaison-api.herokuapp.com/api/city/";
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                            }
                        },
                        new Response.ErrorListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error.Response", String.valueOf(error.getMessage()));
                            }
                        }
                )
                {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email", "admin@liaison.com");
                        params.put("name", city);

                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(postRequest);
                display_response.setText("Ville enrégistrée avec succès");
            }
        });
        return result;
    }
}