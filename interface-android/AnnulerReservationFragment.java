package com.example.liaison;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AnnulerReservationFragment extends Fragment {

    public static AnnulerReservationFragment newInstance() {
        return new AnnulerReservationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_annuler_reservation, container, false);
    }
}