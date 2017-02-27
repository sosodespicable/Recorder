package com.soundai.soundairecorder.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.soundai.soundairecorder.R;

/**
 * Created by fez on 2017/2/22.
 */

public class LicensesFragment extends DialogFragment{
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View openSourceLicensesView = inflater.inflate(R.layout.fragment_licenses, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(openSourceLicensesView)
                .setTitle("Open Source Licenses")
                .setNeutralButton(android.R.string.ok, null);
        return builder.create();
    }
}
