package com.example.liaison;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationFragment extends Fragment {

    public static ReservationFragment newInstance() {
        return new ReservationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_reservation, container, false);
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