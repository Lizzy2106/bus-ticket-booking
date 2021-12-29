package com.example.liaison.agence;

import android.annotation.SuppressLint;
import android.database.MatrixCursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.liaison.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BusAgenceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BusAgenceFragment extends Fragment {

    public static BusAgenceFragment newInstance(String email) {
        BusAgenceFragment retour = new BusAgenceFragment();
        Bundle args = new Bundle();
        args.putString("email", email);
        retour.setArguments(args);
        return retour;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_bus_agence, container, false);
        Bundle args = getArguments();
        String email = args.getString("email");

        //String email = "admin@liaison.com";
        String uri = String.format("https://liaison-api.herokuapp.com/api/bus/?email=" + email);

        JsonArrayRequest myReq = new JsonArrayRequest(Request.Method.GET,
                uri, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray Response) {
                        // get response
                        //List<String> maListe = new ArrayList<String>();
                        Log.d("bus", String.valueOf(Response));
                        try {
                            ArrayList<JSONObject> bus = new ArrayList<>();
                            for(int i=0; i<Response.length(); i++) {
                                JSONObject jsonObject = Response.getJSONObject(i);
                                bus.add(jsonObject);
                            }
                            /*for (JSONObject cty: city) {
                                citylist.add(cty.getString("name"));
                            }
                            Log.d("city", String.valueOf(citylist));*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Log.d("add", String.valueOf(citylist));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        e.printStackTrace();
                        Log.d("res", String.valueOf(e));
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(myReq);

        // NB : SimpleCursorAdapter a besoin obligatoirement d'un ID nommé "_id"
        String[] columns = new String[] { "_id", "col1", "col2" };

// Définition des données du tableau
// les lignes ci-dessous ont pour seul but de simuler
// un objet de type Cursor pour le passer au SimpleCursorAdapter.
// Si vos données sont issues d'une base SQLite,
// utilisez votre "cursor" au lieu du "matrixCursor"
        MatrixCursor matrixCursor= new MatrixCursor(columns);
        startManagingCursor(matrixCursor);
        matrixCursor.addRow(new Object[] { 1,"col1:ligne1","col2:ligne1" });
        matrixCursor.addRow(new Object[] { 2,"col1:ligne2","col2:ligne2" });
        matrixCursor.addRow(new Object[] { 3,"col1:ligne2","col2:ligne2" });

// on prendra les données des colonnes 1 et 2...
        String[] from = new String[] {"col1", "col2", "col1"};

// ...pour les placer dans les TextView définis dans "row_item.xml"
        int[] to = new int[] { R.id.textViewCol1, R.id.textViewCol2, R.id.textViewCol3};

// création de l'objet SimpleCursorAdapter...
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getContext(), R.layout.row_item_3, matrixCursor, from, to, 0);

// ...qui va remplir l'objet ListView
        ListView lv = (ListView) result.findViewById(R.id.lv);
        lv.setAdapter(adapter);

        // Gestion des clics sur les lignes
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
                // faites ici ce que vous voulez
                Log.d("mydebug","clic sur id:"+id);
            }
        };

// Utilisation avec notre listview
        lv.setOnItemClickListener(itemClickListener);

        return result;
    }

    private void startManagingCursor(MatrixCursor matrixCursor) {
    }
}