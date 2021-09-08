package com.sanchezemir.appcobranza.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.sanchezemir.appcobranza.R;
import com.sanchezemir.appcobranza.fragments.ClientesFragment;
import com.sanchezemir.appcobranza.fragments.DashboardFragment;
import com.sanchezemir.appcobranza.fragments.InstalacionesFragment;
import com.sanchezemir.appcobranza.fragments.PagosFragment;

import org.jetbrains.annotations.NotNull;

public class HomeActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);
        loadFragment(new DashboardFragment());

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()){
            case R.id.itemDash:
                fragment = new DashboardFragment();
                break;
            case R.id.itemClientes:
                fragment = new ClientesFragment();
                break;
            case R.id.itemPagos:
                fragment = new PagosFragment();
                break;
            case R.id.itemInstalaciones:
                fragment = new InstalacionesFragment();
                break;
        }

        if (fragment != null){
            loadFragment(fragment);
        }

        return true;
    }

    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}