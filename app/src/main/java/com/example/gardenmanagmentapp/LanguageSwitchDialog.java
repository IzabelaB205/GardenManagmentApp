package com.example.gardenmanagmentapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class LanguageSwitchDialog extends AppCompatDialogFragment {

    private final String[] languagesArray = {"עברית", "English"};
    public static final String TAG = "LANGUAGE_SWITCH_DIALOG";

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.language_switch_title)
                .setSingleChoiceItems(languagesArray, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                setLocale("iw");
                                getActivity().recreate();
                                break;
                            case 1:
                                setLocale("en");
                                getActivity().recreate();
                                break;
                        }

                        dialog.dismiss();
                    }
                });

        return  builder.create();
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = getActivity().getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        //Save data to shared preferences
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("Language", language);
        editor.apply();
    }

    public void LoadLocale() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("Language", "");
        setLocale(language);
    }
}
