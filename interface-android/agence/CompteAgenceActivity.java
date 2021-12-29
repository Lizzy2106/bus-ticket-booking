package com.example.liaison.agence;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.liaison.LogoutUserFragment;
import com.example.liaison.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CompteAgenceActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    //FOR DESIGN
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    //FOR FRAGMENTS
    private Fragment fragmentaccueil, fragmentReservation;
    private Fragment fragmentBus, fragmentLogout, fragmentVoyage;

    //String email = getIntent().getStringExtra("email");

    String email = "admin@liaison.com";
    ////FOR DATAS
    private static final int FRAGMENT_ACCUEIL = 1;
    private static final int FRAGMENT_RESERVATION = 2;
    private static final int FRAGMENT_BUS = 3;
    private static final int FRAGMENT_DECONNEXION = 4;
    private static final int FRAGMENT_VOYAGE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compte_agence);

        // Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        // Show First Fragment
        this.showFirstFragment();

        String email = "admin@liaison.com";
        String uri = String.format("https://liaison-api.herokuapp.com/api/city/?email=" + email);

        JsonArrayRequest myReq = new JsonArrayRequest(Request.Method.GET,
                uri, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray Response) {
                        // get response
                        //List<String> maListe = new ArrayList<String>();
                        //Log.d("city", String.valueOf(Response));
                        try {
                            ArrayList<JSONObject> city = new ArrayList<>();
                            for(int i=0; i<Response.length(); i++) {
                                JSONObject jsonObject = Response.getJSONObject(i);
                                city.add(jsonObject);
                                Toast.makeText (getContext(), jsonObject.getString("name"), Toast.LENGTH_LONG).show();
                                citylist.add(jsonObject.getString("name"));

                            }
                            Log.d("city", String.valueOf(citylist));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("add", String.valueOf(citylist));
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
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(myReq);
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
            case R.id.home:
                this.showFragment(FRAGMENT_ACCUEIL);
                break;
            case R.id.reservation:
                this.showFragment(FRAGMENT_RESERVATION);
                break;
            case R.id.bus:
                this.showFragment(FRAGMENT_BUS);
                break;
            case R.id.voyage:
                this.showFragment(FRAGMENT_VOYAGE);
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
        this.toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
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
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
    }

    // ---------------------
    // FRAGMENTS
    // ---------------------

    // Show first fragment when activity is created
    private void showFirstFragment(){
        Fragment visibleFragment = getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);
        if (visibleFragment == null){
            // Show News Fragment
            this.showFragment(FRAGMENT_ACCUEIL);
            // Mark as selected the menu item corresponding to NewsFragment
            this.navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    // Show fragment according an Identifier

    private void showFragment(int fragmentIdentifier){
        switch (fragmentIdentifier){
            case FRAGMENT_ACCUEIL:
                this.showAccueilFragment();
                break;
            case FRAGMENT_RESERVATION:
                this.showReservationFragment();
                break;
            case FRAGMENT_BUS:
                this.showBusFragment();
                break;
            case FRAGMENT_VOYAGE:
                this.showVoyageFragment();
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
    private void showAccueilFragment(){
        if (this.fragmentaccueil == null) this.fragmentaccueil = AccueilAgenceFragment.newInstance();
        this.startTransactionFragment(this.fragmentaccueil);
        getSupportActionBar().setTitle("Accueil");
    }

    private void showReservationFragment(){
        if (this.fragmentReservation == null) this.fragmentReservation = ReservationAgenceFragment.newInstance();
        this.startTransactionFragment(this.fragmentReservation);
        getSupportActionBar().setTitle("Liste des réservations");
    }

    private void showBusFragment(){
        if (this.fragmentBus == null) this.fragmentBus = BusAgenceFragment.newInstance(email);
        this.startTransactionFragment(this.fragmentBus);
        /*Bundle bundle = new Bundle();
        bundle.putString("email", email);
        fragmentBus.setArguments(bundle);*/
        getSupportActionBar().setTitle("Liste des bus");
    }

    private void showVoyageFragment(){
        if (this.fragmentBus == null) this.fragmentBus = VoyageAgenceFragment.newInstance();
        this.startTransactionFragment(this.fragmentBus);
        getSupportActionBar().setTitle("Liste des voyages");
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