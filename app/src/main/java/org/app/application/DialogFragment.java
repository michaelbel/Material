package org.app.application;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.app.material.LayoutHelper;
import org.app.material.widget.NumberPicker;

public class DialogFragment extends Fragment implements View.OnClickListener {

    private Button button1;

    public DialogFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout layout = new FrameLayout(getActivity());
        //layout.setLayoutParams(new ViewGroup.LayoutParams(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        layout.setBackgroundColor(0xffffffff);

        button1 = new Button(getActivity());
        button1.setText(getResources().getString(R.string.NumberPicker));
        button1.setOnClickListener(this);
        layout.addView(button1, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER));

        return layout;
    }

    @Override
    public void onClick(View view) {
        if (view == button1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Fast NumberPicker");

            final NumberPicker numberPicker = new NumberPicker(getActivity());
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(100);
            numberPicker.setValue(10);
            numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            numberPicker.setSelectionDividerColor(0xff4285f4);

            builder.setView(numberPicker);
            builder.setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), "Value = " + numberPicker.getValue(), Toast.LENGTH_SHORT).show();
                }
            });
            builder.show().getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(0xff4285f4);
        }
    }
}