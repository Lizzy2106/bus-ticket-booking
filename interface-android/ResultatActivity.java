package com.example.liaison;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class ResultatActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //FOR DESIGN
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    //FOR FRAGMENTS
    private Fragment fragmentRechercher, fragmentReservation;
    private Fragment fragmentAnnuler, fragmentLogout;
    private Activity ProfileActivity;

    ////FOR DATAS
    private static final int FRAGMENT_RECHERCHER = 1;
    private static final int FRAGMENT_RESERVATION = 2;
    private static final int FRAGMENT_ANNULER = 3;
    private static final int FRAGMENT_PARAMETRE = 4;
    private static final int FRAGMENT_DECONNEXION = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat);

        // Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        //this.configureNavigationView();

        // Show First Fragment
        //this.showFirstFragment();

        Button continuer = (Button) findViewById(R.id.continuer);

        continuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DetailReservationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        // Show fragment after user clicked on a menu item
        switch (id){
            case R.id.search:
                this.showFragment(FRAGMENT_RECHERCHER);
                break;
            case R.id.reservation:
                this.showFragment(FRAGMENT_RESERVATION);
                break;
            case R.id.delete:
                this.showFragment(FRAGMENT_ANNULER);
                break;
            case R.id.settings:
                this.showFragment(FRAGMENT_PARAMETRE);
                break;
            case R.id.logout:
                this.showFragment(FRAGMENT_DECONNEXION);
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    // Configure Toolbar
    private void configureToolBar(){
        this.toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        //ActionBar ab = getSupportActionBar();
        //ab.setDisplayHomeAsUpEnabled(true);
    }

    // Configure Drawer Layout
    private void configureDrawerLayout(){
        this.drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Configure NavigationView
    private void configureNavigationView(){
        this.navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // ---------------------
    // FRAGMENTS
    // ---------------------

    // Show first fragment when activity is created
    private void showFirstFragment(){
        Fragment visibleFragment = getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);
        if (visibleFragment == null){
            // Show News Fragment
            this.showFragment(FRAGMENT_RECHERCHER);
            // Mark as selected the menu item corresponding to NewsFragment
            this.navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    // Show fragment according an Identifier

    private void showFragment(int fragmentIdentifier){
        switch (fragmentIdentifier){
            case FRAGMENT_RECHERCHER:
                this.showRechercherFragment();
                break;
            case FRAGMENT_RESERVATION:
                this.showReservationFragment();
                break;
            case FRAGMENT_ANNULER:
                this.showAnnulerFragment();
                break;
            case FRAGMENT_DECONNEXION:
                this.showLogoutFragment();
                break;
            default:
                break;
        }
    }

    // ---

    // Create each fragment page and show it
    private void showRechercherFragment(){
        if (this.fragmentRechercher == null) this.fragmentRechercher = RechercherUserFragment.newInstance();
        this.startTransactionFragment(this.fragmentRechercher);
        getSupportActionBar().setTitle("Rechercher des voyages");
    }

    private void showReservationFragment(){
        if (this.fragmentReservation == null) this.fragmentReservation = ReservationFragment.newInstance();
        this.startTransactionFragment(this.fragmentReservation);
        getSupportActionBar().setTitle("Liste des réservations");
    }

    private void showAnnulerFragment(){
        if (this.fragmentAnnuler == null) this.fragmentAnnuler = AnnulerReservationFragment.newInstance();
        this.startTransactionFragment(this.fragmentAnnuler);
        getSupportActionBar().setTitle("Annuler une réservation");
    }


    private void showLogoutFragment(){
        if (this.fragmentLogout == null) this.fragmentLogout = LogoutUserFragment.newInstance();
        this.startTransactionFragment(this.fragmentLogout);
        getSupportActionBar().setTitle("Déconnexion");
    }

    // ---

    // Generic method that will replace and show a fragment inside the MainActivity Frame Layout
    private void startTransactionFragment(Fragment fragment){
        if (!fragment.isVisible()){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_frame_layout, fragment).commit();
        }
    }
}