package org.app.application;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.app.material.LayoutHelper;
import org.app.material.widget.NumberPicker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setBackgroundColor(0xffffffff);

        button1 = new Button(this);
        button1.setText(getResources().getString(R.string.NumberPicker));
        button1.setOnClickListener(this);
        frameLayout.addView(button1, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER));

        setContentView(frameLayout);
    }

    @Override
    public void onClick(View v) {
        if (v == button1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Fast NumberPicker");

            final NumberPicker numberPicker = new NumberPicker(this);
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(100);
            numberPicker.setValue(10);
            numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            numberPicker.setSelectionDividerColor(0xff4285f4);

            builder.setView(numberPicker);
            builder.setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Value = " + numberPicker.getValue(), Toast.LENGTH_SHORT).show();
                }
            });
            builder.show().getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(0xff4285f4);
        }
    }
}