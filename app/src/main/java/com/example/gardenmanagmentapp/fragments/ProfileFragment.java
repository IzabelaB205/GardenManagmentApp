package com.example.gardenmanagmentapp.fragments;

import android.database.DataSetObserver;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gardenmanagmentapp.R;
import com.example.gardenmanagmentapp.model.User;
import com.google.android.material.textfield.TextInputEditText;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user application user.
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance(User user) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        CircleImageView circleImageView = view.findViewById(R.id.profile_image);
        TextView textViewUsername = view.findViewById(R.id.profile_username_text_view);
        TextInputEditText editTextId = view.findViewById(R.id.profile_id_edit_text);
        TextInputEditText editTextEmail = view.findViewById(R.id.email_edit_text);
        TextInputEditText editTextPhone = view.findViewById(R.id.profile_phone_edit_text);

        User user = (User) getArguments().get("user");

        textViewUsername.setText(user.getFullName());
        editTextId.setText(user.getId());
        editTextEmail.setText(user.getEmail());
        editTextPhone.setText(user.getPhone());

        //TODO: add user photo by using glide library

        return view;
    }
}