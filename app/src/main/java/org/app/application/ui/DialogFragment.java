package org.app.application.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.app.application.R;
import org.app.material.widget.FilePicker;
import org.app.material.widget.LayoutHelper;
import org.app.material.widget.NumberPicker;

public class DialogFragment extends Fragment implements View.OnClickListener {

    private Button button1;
    private Button button2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(0xFFECEFF1);

        button1 = new Button(getActivity());
        button1.setText(getResources().getString(R.string.NumberPicker));
        button1.setOnClickListener(this);
        layout.addView(button1, LayoutHelper.makeLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));

        button2 = new Button(getActivity());
        button2.setText(getResources().getString(R.string.FilePicker));
        button2.setOnClickListener(this);
        layout.addView(button2, LayoutHelper.makeLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));

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
        } else if (view == button2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            FilePicker filePicker = new FilePicker(getActivity());
            filePicker.showDivider();

            builder.setView(filePicker);
            builder.show();
        }
    }
}