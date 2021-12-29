package com.example.liaison.agence;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.liaison.R;
import com.example.liaison.ReservationFragment;

public class ReservationAgenceFragment extends Fragment {

    public static ReservationAgenceFragment newInstance() {
        return new ReservationAgenceFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reservation_agence, container, false);
    }
}