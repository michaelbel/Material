/*
 * Copyright 2016 Michael Bel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.app.application;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.app.material.AndroidUtilities;
import org.app.material.MaterialActivity;

public class ViewController extends MaterialActivity {

    Button button1;
    Button button2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar.setTitle("View Controller");
        toolbar.setBackButtonIcon(AndroidUtilities.getIcon(this, R.drawable.ic_menu, 0xFFFFFFFF));
        toolbar.addIcon(AndroidUtilities.getIcon(this, R.drawable.ic_dots_menu, 0xFFFFFFFF));
        toolbar.addIcon(AndroidUtilities.getIcon(this, R.drawable.ic_github, 0xFFFFFFFF));

        button1 = new Button(this);
        button1.setText("Primary");
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        addView(button1);

        button2 = new Button(this);
        button2.setText("Accent");
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        addView(button2);
    }

    private void makeToast(String resId) {
        Toast.makeText(this, "" + resId, Toast.LENGTH_SHORT).show();
    }
}