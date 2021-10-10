package com.example.gardenmanagmentapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gardenmanagmentapp.repository.FirebaseDatabaseHelper;
import com.example.gardenmanagmentapp.R;
import com.example.gardenmanagmentapp.fragment.ChatFragment;
import com.example.gardenmanagmentapp.fragment.HomeFragment;
import com.example.gardenmanagmentapp.fragment.NotificationsListFragment;
import com.example.gardenmanagmentapp.fragment.PictureSelectionFragment;
import com.example.gardenmanagmentapp.fragment.GalleryFragment;
import com.example.gardenmanagmentapp.fragment.ProfileFragment;
import com.example.gardenmanagmentapp.fragment.SignInFragment;
import com.example.gardenmanagmentapp.model.Picture;
import com.example.gardenmanagmentapp.viewmodel.AuthenticationViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements PictureSelectionFragment.PictureUploadListener/*, HomeFragment.HomeFragmentListener */{

    private FirebaseDatabaseHelper firebaseManager;
    private FragmentManager fragmentManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AuthenticationViewModel viewModel;

    private final String NOTIFICATIONS_FRAGMENT_TAG = "notifications_fragment";
    private final String CHAT_FRAGMENT_TAG = "chat_fragment";
    private final String CALENDAR_FRAGMENT_TAG = "calendar_fragment";
    private final String PROFILE_FRAGMENT_TAG = "profile_fragment";
    private final String PICTURES_FRAGMENT_TAG = "pictures_fragment";
    private final String DEFAULT_FRAGMENT_TAG = "default_fragment";
    private final String HOME_FRAGMENT_TAG = "home_fragment";
    private final String SIGN_IN_FRAGMENT_TAG = "sign_in_fragment";

    private TextView textViewUsername;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        View headerView = navigationView.getHeaderView(0);
        textViewUsername = headerView.findViewById(R.id.navigation_header_text_view);

        fragmentManager = getSupportFragmentManager();
        firebaseManager = new FirebaseDatabaseHelper();

        fragmentManager.beginTransaction().add(R.id.id_to_fill, new HomeFragment(), HOME_FRAGMENT_TAG).commit();

        viewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);

        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null) {

                }
               else {
                   viewModel.SignOut();
                }
            }
        };

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                String title = "";
                String context = "";

                item.setChecked(true);

                switch(item.getItemId())
                {
                    case R.id.item_home:
                        //openDefaultFragment(R.id.item_calendar, title, context);

                        fragmentManager.beginTransaction()
                                .replace(R.id.id_to_fill, new HomeFragment(), HOME_FRAGMENT_TAG)
                                .addToBackStack(null)
                                .commit();

                        break;
                    case R.id.item_notifications:
                        title = "Notification Fragment";
                        context = "this is a default notification fragment";
                        //openDefaultFragment(R.id.item_notifications, title, context);

                        fragmentManager.beginTransaction()
                                .replace(R.id.id_to_fill, new NotificationsListFragment(), NOTIFICATIONS_FRAGMENT_TAG)
                                .addToBackStack(null)
                                .commit();

                    /*    if(!textViewUsername.equals("Visitor")) {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.drawer_layout, new NotificationsListFragment(), NOTIFICATIONS_FRAGMENT_TAG)
                                    .addToBackStack(null)
                                    .commit();
                        }*/

                        break;
                    case R.id.item_chat:
                        title = "Chat Fragment";
                        context = "this is a default chat fragment";
                        //openDefaultFragment(R.id.item_chat, title, context);

                        fragmentManager.beginTransaction()
                                .replace(R.id.id_to_fill,  new ChatFragment(), CHAT_FRAGMENT_TAG)
                                .addToBackStack(null)
                                .commit();

                        break;
                    case R.id.item_pictures:
                        title = "Pictures Fragment";
                        context = "this is a default pictures fragment";
/*
                        openDefaultFragment(R.id.item_pictures, title, context);
*/

                        fragmentManager.beginTransaction()
                                .replace(R.id.id_to_fill, new GalleryFragment(), PICTURES_FRAGMENT_TAG)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.item_profile:
                        title = "Profile Fragment";
                        context = "this is a default profile fragment";
/*
                        openDefaultFragment(R.id.item_profile, title, context);
*/

                        fragmentManager.beginTransaction()
                                .replace(R.id.id_to_fill, new ProfileFragment(), PROFILE_FRAGMENT_TAG)
                                .commit();
                        break;
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });

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

    @Override
    public void applyPictureInfo(Picture picture, String fileExtension) {
        firebaseManager.UploadPictureToFirebase(picture, fileExtension);
    }


    private void openDefaultFragment(int selectedItem, String title, String context) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.id_to_fill, new HomeFragment(), HOME_FRAGMENT_TAG);
        transaction.commit();

/*        DefaultFragment defaultFragment = DefaultFragment.newInstance(selectedItem, title, context);

        fragmentManager.beginTransaction()
                .replace(R.id.id_to_fill, defaultFragment, DEFAULT_FRAGMENT_TAG)
//                .addToBackStack(null)
                .commit();*/
    }

//    @Override
//    public void displaySignInDialog() {
//        fragmentManager.beginTransaction()
//                .replace(R.id.id_to_fill, new SignInFragment(), SIGN_IN_FRAGMENT_TAG)
//                .commit();
//    }
}