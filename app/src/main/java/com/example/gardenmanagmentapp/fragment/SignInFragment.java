package com.example.gardenmanagmentapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.gardenmanagmentapp.R;
import com.example.gardenmanagmentapp.viewmodel.AuthenticationViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class SignInFragment extends Fragment {

    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private Button buttonSignIn;
    private AuthenticationViewModel viewModel;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setListeners();

        viewModel = new ViewModelProvider(requireActivity()).get(AuthenticationViewModel.class);
        viewModel.getFirebaseUser().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null) {
                    Snackbar.make(view, getString(R.string.sign_in_successfully), Snackbar.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
    }

    private void initViews(View view) {

        editTextEmail = view.findViewById(R.id.sign_in_email_edit_text);
        editTextPassword = view.findViewById(R.id.sign_in_password_edit_text);
        buttonSignIn = view.findViewById(R.id.sign_in_button);
    }

    private void setListeners() {
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if (!email.trim().isEmpty() && !password.trim().isEmpty()) {
                    viewModel = new ViewModelProvider(requireActivity()).get(AuthenticationViewModel.class);
                    viewModel.SignIn(email, password);
                }
                else {
                    Snackbar.make(v, getString(R.string.provide_email_and_password), Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}
