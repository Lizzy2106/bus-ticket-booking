package com.example.liaison;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RechercherUserFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public static RechercherUserFragment newInstance() {
        return new RechercherUserFragment();
    }

    String[] country = { "India", "USA", "China", "Japan", "Other"};
    String[] country1 = { "India", "USA", "China", "Japan", "Other"};

    private EditText lieu_depart;
    private EditText lieu_arrivee;
    private EditText date_depart;
    private EditText heure_depart;
    private int nbre_place = 2;
    private int nbre_place_restant = 5;

    private ArrayAdapter<String> adapter;

    private ArrayList<String> citylist = new ArrayList<String>();

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.fragment_user_rechercher, container, false);

        ArrayList<String> cars = new ArrayList<String>();
        cars.add("Volvo");
        cars.add("BMW");
        cars.add("Ford");
        cars.add("Mazda");

        Spinner lieu_depart = (Spinner) result.findViewById(R.id.lieu_depart_spinner);
        Spinner lieu_arrivee = (Spinner) result.findViewById(R.id.lieu_arrive_spinner);
        lieu_depart.setOnItemSelectedListener(this);
        lieu_arrivee.setOnItemSelectedListener(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, citylist);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lieu_depart.setAdapter(arrayAdapter);

        adapter = new ArrayAdapter<>(
                this.getActivity(),
                android.R.layout.simple_spinner_item,
                cars);
        //Log.d("list", String.valueOf(citylist));

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        //lieu_depart.setAdapter(adapter);
        lieu_arrivee.setAdapter(adapter);
        lieu_depart.setOnItemSelectedListener(this);
        lieu_depart.setOnItemSelectedListener(this);

        date_depart = (EditText) result.findViewById(R.id.date_depart);
        heure_depart = (EditText) result.findViewById(R.id.heure_date);
        Button search_button = (Button) result.findViewById(R.id.search_reservation);
        ImageButton more_place = (ImageButton) result.findViewById(R.id.more_place);
        ImageButton minus_place = (ImageButton) result.findViewById(R.id.minus_place);
        TextView display_nbplace = (TextView) result.findViewById(R.id.view_nbplace);

        more_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nbre_place >= nbre_place_restant){
                    display_nbplace.setText(String.valueOf(nbre_place));
                    Toast.makeText(getContext(), "Le nombre de place disponible est atteint", Toast.LENGTH_LONG).show();
                }
                else {
                    nbre_place++;
                    display_nbplace.setText(String.valueOf(nbre_place));
                }
            }
        });
        minus_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nbre_place<=1){
                    display_nbplace.setText(String.valueOf(nbre_place));
                    Toast.makeText(getContext(), "Le nombre de places doit être supérieur à 0", Toast.LENGTH_LONG).show();
                }
                else {
                    nbre_place--;
                    display_nbplace.setText(String.valueOf(nbre_place));
                }
            }
        });

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Log.d("DEBUG", String.valueOf(lieu_depart.getText()));
                String get_lieu_depart = lieu_depart.getText().toString();
                String get_lieu_arrive = lieu_arrivee.getText().toString();*/

                String get_date_depart = date_depart.getText().toString();
                String get_heure_depart = heure_depart.getText().toString();

                if(get_heure_depart.equals("")){
                    heure_depart.setError("Ce champ ne peut pas rester vide");
                }
                else if(get_date_depart.equals("")){
                    date_depart.setError("Ce champ ne peut pas rester vide");
                }

                else if(!get_date_depart.matches("(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$")){
                    date_depart.setError("Format à respecter dd/mm/yyyy ou dd-mm-yyyy ou dd.mm.yyyy");
                }
                else if(!get_heure_depart.matches("([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")){
                    heure_depart.setError("Format à respecter hh:mm");
                }
                else {
                    Intent intent = new Intent(RechercherUserFragment.this.getActivity(),
                            ResultatActivity.class);
                    startActivity(intent);
                }
            }
        });
        return result;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int position, long id) {
        //Toast.makeText (getContext(), cars.get(position), Toast.LENGTH_LONG) .show ();
        Log.v("item", (String) parent.getItemAtPosition(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getActivity(), "Vous n'avez rien sélectionner", Toast.LENGTH_LONG).show();
    }
}