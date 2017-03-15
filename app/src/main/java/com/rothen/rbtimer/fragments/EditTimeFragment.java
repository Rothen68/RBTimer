package com.rothen.rbtimer.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.rothen.rbtimer.R;

/**
 * Created by apest on 15/03/2017.
 */

public class EditTimeFragment extends DialogFragment {

    public static final String OLD_VALUE = "OldValue";
    public static final String VALUE_TYPE = "ValueType";

    public  interface  EditTimeDialogListener
    {
        void onDialogPositiveClick(DialogFragment dialog,String valueType, int value);
    }

    private EditText etxtValue;
    private EditTimeDialogListener listener;

    private String valueType;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.edit_time_frag,null);
        etxtValue = (EditText) v.findViewById(R.id.etxtValue);
        builder.setView(v);
        int value = getArguments().getInt(OLD_VALUE);
        etxtValue.setText(value + "");
        valueType = getArguments().getString(VALUE_TYPE);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener!=null)
                {
                    int value = Integer.valueOf(etxtValue.getText().toString());
                    listener.onDialogPositiveClick(EditTimeFragment.this,valueType, value );
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try
        {
            listener = (EditTimeDialogListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " does not implement EditTimeDialogListener");
        }
    }
}
