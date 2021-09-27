package com.example.gardenmanagmentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements SignInDialog.SignInDialogListener {

    private String username = null;
    private final String NOTIFICATIONS_FRAGMENT_TAG = "notifications_fragment";
    private final String CALENDAR_FRAGMENT_TAG = "calendar_fragment";

    TextView textViewUsername;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                View headerView = navigationView.getHeaderView(0);
                textViewUsername = headerView.findViewById(R.id.navigation_header_text_view);
                username = textViewUsername.getText().toString();

                item.setChecked(true);

                switch(item.getItemId())
                {
                    case R.id.item_notifications:
                        break;
                    case R.id.item_calendar:

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.add(R.id.drawer_layout, new CalendarFragment(), CALENDAR_FRAGMENT_TAG);
                        transaction.commit();
                        break;
                    case R.id.item_pictures:
                        break;
                    case R.id.item_profile:
                        break;
                    case R.id.item_settings:
                        break;

                    case R.id.item_language:
                        openLanguageSwitchDialog();
                        break;
                }

                drawerLayout.closeDrawers();

                //TODO:should represent dialog only for visitor user
                openSignInDialog();

                return true;
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(Gravity.START);
        }

        return super.onOptionsItemSelected(item);
    }

    private void openSignInDialog() {
        SignInDialog signInDialog = new SignInDialog();
        signInDialog.show(getSupportFragmentManager(), SignInDialog.TAG);
    }

    @Override
    public void applyUserInfo(String username, String password) {

    }

    private void openLanguageSwitchDialog() {
        LanguageSwitchDialog languageSwitchDialog = new LanguageSwitchDialog();
        languageSwitchDialog.show(getSupportFragmentManager(), LanguageSwitchDialog.TAG);
    }
}