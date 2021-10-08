package com.example.gardenmanagmentapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.gardenmanagmentapp.database.FirebaseDatabaseHelper;
import com.example.gardenmanagmentapp.dialogs.LanguageSwitchDialog;
import com.example.gardenmanagmentapp.R;
import com.example.gardenmanagmentapp.dialogs.SignInDialog;
import com.example.gardenmanagmentapp.fragments.DefaultFragment;
import com.example.gardenmanagmentapp.fragments.NotificationsFragment;
import com.example.gardenmanagmentapp.fragments.PictureSelectionFragment;
import com.example.gardenmanagmentapp.fragments.PictureStorageFragment;
import com.example.gardenmanagmentapp.model.Notification;
import com.example.gardenmanagmentapp.model.Picture;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SignInDialog.SignInDialogListener
        , NotificationsFragment.AddNotificationDialogListener
        , PictureSelectionFragment.PictureUploadListener
        ,PictureStorageFragment.PictureStorageListener{

    private FirebaseDatabaseHelper firebaseManager;
    private FragmentManager fragmentManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private final String NOTIFICATIONS_FRAGMENT_TAG = "notifications_fragment";
    private final String CHAT_FRAGMENT_TAG = "chat_fragment";
    private final String CALENDAR_FRAGMENT_TAG = "calendar_fragment";
    private final String PROFILE_FRAGMENT_TAG = "profile_fragment";
    private final String PICTURES_FRAGMENT_TAG = "pictures_fragment";
    private final String DEFAULT_FRAGMENT_TAG = "default_fragment";

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

        fragmentManager = getSupportFragmentManager();
        firebaseManager = new FirebaseDatabaseHelper();

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
                String title = "";
                String context = "";

                item.setChecked(true);

                switch(item.getItemId())
                {
                    case R.id.item_notifications:
                        title = "Notification Fragment";
                        context = "this is a default notification fragment";
                        openDefaultFragment(R.id.item_notifications, title, context);

//                        if(!textViewUsername.equals("Visitor")) {
//                            fragmentManager.beginTransaction()
//                                    .replace(R.id.drawer_layout, new NotificationsFragment(new ArrayList<>()), NOTIFICATIONS_FRAGMENT_TAG)
//                                    .addToBackStack(null)
//                                    .commit();
//                        }

                        break;
                    case R.id.item_chat:
                        title = "Chat Fragment";
                        context = "this is a default chat fragment";
                        openDefaultFragment(R.id.item_chat, title, context);
                        /*fragmentManager.beginTransaction()
                                .add(R.id.drawer_layout, new ChatFragment(), CHAT_FRAGMENT_TAG)
                                .commit();*/
                        break;
                    case R.id.item_calendar:
                        title = "Calendar Fragment";
                        context = "this is a default calendar fragment";
                        openDefaultFragment(R.id.item_calendar, title, context);
                        /*fragmentManager.beginTransaction()
                                .add(R.id.drawer_layout, new CalendarFragment(), CALENDAR_FRAGMENT_TAG)
                                .commit();*/
                        break;
                    case R.id.item_pictures:
                        title = "Pictures Fragment";
                        context = "this is a default pictures fragment";
                        openDefaultFragment(R.id.item_pictures, title, context);

                        List<Picture> pictureList = firebaseManager.LoadPicturesData();
                        PictureStorageFragment pictureStorageFragment = PictureStorageFragment.newInstance(pictureList);
                        fragmentManager.beginTransaction()
                                .replace(R.id.drawer_layout, pictureStorageFragment, PICTURES_FRAGMENT_TAG)
                                .commit();
                        break;
                    case R.id.item_profile:
                        title = "Profile Fragment";
                        context = "this is a default profile fragment";
                        openDefaultFragment(R.id.item_profile, title, context);

//                        //TODO: read user information from firebase
//                        User user = new User("Izabela", "12345679", "054-43857654", "izabela@gmail.com","111222333","");
//                        ProfileFragment profileFragment = ProfileFragment.newInstance(user);
//                        fragmentManager.beginTransaction()
//                                .add(R.id.drawer_layout, profileFragment, PROFILE_FRAGMENT_TAG)
//                                .commit();
                        break;
                    case R.id.item_settings:
                        break;

                    case R.id.item_language:
                        openLanguageSwitchDialog();
                        break;
                }

                drawerLayout.closeDrawers();
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

    @Override
    public void applyUserInfo(String email, String username, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    textViewUsername.setText(username);
                    Snackbar.make(drawerLayout, "User sign-in successfully", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void applyNotificationInfo(Notification notification) {
        firebaseManager.UpdateNotificationsDatabase(notification);
    }

    @Override
    public void applyPictureInfo(Picture picture, String fileExtension) {
        firebaseManager.UploadPicturesDatabase(picture, fileExtension);
    }

    @Override
    public void deletePictureInfo(Picture picture) {
        firebaseManager.DeletePictureFromDatabase(picture);
    }

    private void openLanguageSwitchDialog() {
        LanguageSwitchDialog languageSwitchDialog = new LanguageSwitchDialog();
        languageSwitchDialog.show(getSupportFragmentManager(), LanguageSwitchDialog.TAG);
    }

    private void openDefaultFragment(int selectedItem, String title, String context) {
        DefaultFragment defaultFragment = DefaultFragment.newInstance(selectedItem, title, context);

        fragmentManager.beginTransaction()
                .replace(R.id.id_to_fill, defaultFragment, DEFAULT_FRAGMENT_TAG)
//                .addToBackStack(null)
                .commit();
    }
}