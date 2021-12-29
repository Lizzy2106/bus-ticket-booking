package com.example.liaison.agence;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.liaison.R;

public class AccueilAgenceFragment extends Fragment {

    public static AccueilAgenceFragment newInstance() {
        return new AccueilAgenceFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_accueil_agence, container, false);

        Button add_bus = (Button) result.findViewById(R.id.add_bus);
        Button add_voyage = (Button) result.findViewById(R.id.add_voyage);
        //Button search = (Button) result.findViewById(R.id.rechercher_button);
        //Button send_mail = (Button) result.findViewById(R.id.send_mail_button);

        /*add_bus.setOnClickListener(add_busListener);
        send_mail.setOnClickListener(send_mailListener);
        add_voyage.setOnClickListener(add_voyageListener);*/

        add_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccueilAgenceFragment.this.getActivity(),
                        AddBusAgenceActivity.class);
                startActivity(intent);
            }
        });

        add_voyage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccueilAgenceFragment.this.getActivity(),
                        AddVoyageActivity.class);
                startActivity(intent);
            }
        });
        return result;
    }
}