package com.example.gardenmanagmentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SignInDialog.SignInDialogListener {

    private String username = null;
    private String password = null;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

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

        View headerView = navigationView.getHeaderView(0);
        textViewUsername = headerView.findViewById(R.id.navigation_header_text_view);

        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null) {
                    textViewUsername.setText(user.getDisplayName());
                    Snackbar.make(drawerLayout, "Welcome, " + textViewUsername.getText().toString() + "!", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
                else {
                    Snackbar.make(drawerLayout, "Wrong username or password!", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            }
        };

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
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

    @Override
    public void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(authStateListener);
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

        //TODO: find user in users list - then sign-in user using his email

        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    textViewUsername.setText(username);
                    Snackbar.make(drawerLayout, "User sign-in successfully", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void openLanguageSwitchDialog() {
        LanguageSwitchDialog languageSwitchDialog = new LanguageSwitchDialog();
        languageSwitchDialog.show(getSupportFragmentManager(), LanguageSwitchDialog.TAG);
    }
}