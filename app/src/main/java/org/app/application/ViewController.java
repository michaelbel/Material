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
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import org.app.material.AndroidUtilities;
import org.app.material.FabButton;
import org.app.material.FabMenu;
import org.app.material.LayoutHelper;
import org.app.material.MaterialActivity;

public class ViewController extends MaterialActivity {

    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar.setTitle("View Controller");
        toolbar.setBackButtonIcon(AndroidUtilities.getIcon(this, R.drawable.ic_menu, 0xFFFFFFFF));
        toolbar.addIcon(AndroidUtilities.getIcon(this, R.drawable.ic_dots_menu, 0xFFFFFFFF));
        toolbar.addIcon(AndroidUtilities.getIcon(this, R.drawable.ic_github, 0xFFFFFFFF));

        //button = new Button(this);
        //button.setText("Open Search");
        //addView(button);
        //button.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        toolbar.openSearchField();
        //    }
        //});

        FabMenu fabMenu = new FabMenu(this);
        fabMenu.setIcon(AndroidUtilities.getIcon(this, R.drawable.ic_github, 0xFF000000));
        fabMenu.setVisibility(View.VISIBLE);
        fabMenu.setBackgroundColor(0xffFF5252);
        fabMenu.setLayoutParams(LayoutHelper.makeLinear(this, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END));
        fabMenu.setMenuButtonColorNormal(0xffFF5252);
        fabMenu.setMenuButtonColorPressed(0xffFF5252);
        fabMenu.setMenuButtonLabelText("Fab menu");
        fabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.hide();
            }
        });
        addView(fabMenu);

        FabButton fabButton = new FabButton(this);
        fabButton.setColorNormal(0xffFF5252);
        fabButton.setImageDrawable(AndroidUtilities.getIcon(this, R.drawable.ic_share, 0xFFFFFFFF));
        fabButton.setLayoutParams(LayoutHelper.makeLinear(this, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | Gravity.START, 16, 16, 16, 16));
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setStatusBarColor(0xffFF5252);
            }
        });
        addView(fabButton);
    }
}