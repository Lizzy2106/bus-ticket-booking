package com.example.liaison.root;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.liaison.ConnexionActivity;
import com.example.liaison.R;

public class LogoutRootFragment extends Fragment {

    public static LogoutRootFragment newInstance() {
        return new LogoutRootFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_root_logout, container, false);

        Button oui = (Button) result.findViewById(R.id.oui_button);
        Button non = (Button) result.findViewById(R.id.non_button);

        oui.setOnClickListener(ouiListener);
        non.setOnClickListener(nonListener);

        return result;
    }

    private final View.OnClickListener ouiListener = v ->{
        Intent intent = new Intent(LogoutRootFragment.this.getActivity(),
                ConnexionActivity.class);
        startActivity(intent);
        System.exit(0);
    };

    private final View.OnClickListener nonListener = v -> {
        Intent intent = new Intent(LogoutRootFragment.this.getActivity(),
                CompteRootActivity.class);
        startActivity(intent);
    };
}