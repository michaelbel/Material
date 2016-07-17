package org.app.application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.app.material.FAB.FabButton;
import org.app.material.FAB.FabMenu;

public class TestFabSheet extends AppCompatActivity {

    private FabMenu mFabMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        mFabMenu = (FabMenu) findViewById(R.id.fabMenu);

        FabButton fabButton1 = new FabButton(this);
        fabButton1.setImageResource(R.drawable.ic_email);
        fabButton1.setLabelText("Fab Button 1");
        fabButton1.setButtonSize(FabButton.SIZE_MINI);
        fabButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestFabSheet.this, "1", Toast.LENGTH_SHORT).show();
                mFabMenu.close(true);
            }
        });

        FabButton fabButton2 = new FabButton(this);
        fabButton2.setImageResource(R.drawable.ic_email);
        fabButton2.setLabelText("Fab Button 2");
        fabButton2.setButtonSize(FabButton.SIZE_MINI);
        fabButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestFabSheet.this, "2", Toast.LENGTH_SHORT).show();
                mFabMenu.close(true);
            }
        });

        final FabButton fabButton3 = new FabButton(this);
        fabButton3.setImageResource(R.drawable.ic_email);
        fabButton3.setLabelText("Fab Button 3");
        fabButton3.setButtonSize(FabButton.SIZE_MINI);
        fabButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestFabSheet.this, "3", Toast.LENGTH_SHORT).show();
                mFabMenu.close(true);
            }
        });

        mFabMenu.addMenuButton(fabButton1);
        mFabMenu.addMenuButton(fabButton2);
        mFabMenu.addMenuButton(fabButton3);


        /*Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFabMenu.open(true);
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFabMenu.close(true);
            }
        });

        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFabMenu.removeAllMenuButtons();
            }
        });

        Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFabMenu.setMenuButtonLabelText("Menu Button");
            }
        });

        Button button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFabMenu.removeMenuButton(fabButton3);
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        if (mFabMenu.isOpened()) {
            mFabMenu.close(true);
        } else {
            super.onBackPressed();
        }
    }
}