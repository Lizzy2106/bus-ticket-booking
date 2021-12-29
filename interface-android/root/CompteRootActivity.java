package com.example.liaison.root;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.liaison.LogoutUserFragment;
import com.example.liaison.R;
import com.example.liaison.agence.AccueilAgenceFragment;
import com.example.liaison.agence.VoyageAgenceFragment;
import com.google.android.material.navigation.NavigationView;

public class CompteRootActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //FOR DESIGN
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    //FOR FRAGMENTS
    private Fragment fragmentAddLieu, fragmentLogout, fragmentAgence;

    ////FOR DATAS
    private static final int FRAGMENT_ADD_LOCATION = 1;
    private static final int FRAGMENT_DECONNEXION = 2;
    private static final int FRAGMENT_AGENCE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compte_root);

        // Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        // Show First Fragment
        this.showFirstFragment();
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        // Show fragment after user clicked on a menu item
        switch (id){
            case R.id.agence:
                this.showFragment(FRAGMENT_AGENCE);
                break;
            case R.id.lieu:
                this.showFragment(FRAGMENT_ADD_LOCATION);
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
            this.showFragment(FRAGMENT_ADD_LOCATION);
            // Mark as selected the menu item corresponding to NewsFragment
            this.navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    // Show fragment according an Identifier

    private void showFragment(int fragmentIdentifier){
        switch (fragmentIdentifier){
            case FRAGMENT_ADD_LOCATION:
                this.showAddLocationFragment();
                break;
            case FRAGMENT_AGENCE:
                this.showAgenceFragment();
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
    private void showAddLocationFragment(){
        if (this.fragmentAddLieu == null) this.fragmentAddLieu = AddCityFragment.newInstance();
        this.startTransactionFragment(this.fragmentAddLieu);
        getSupportActionBar().setTitle("Ajouter un lieu");
    }

    private void showAgenceFragment(){
        if (this.fragmentAgence == null) this.fragmentAgence = VoyageAgenceFragment.newInstance();
        this.startTransactionFragment(this.fragmentAgence);
        getSupportActionBar().setTitle("Liste des agences en attente");
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