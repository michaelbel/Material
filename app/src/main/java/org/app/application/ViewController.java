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
import android.support.v7.widget.AppCompatEditText;
import android.util.TypedValue;

import org.app.material.AndroidUtilities;
import org.app.material.LayoutHelper;
import org.app.material.MaterialActivity;

public class ViewController extends MaterialActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar.setTitle("View Controller");
        toolbar.setBackButtonIcon(AndroidUtilities.getIcon(this, R.drawable.ic_menu, 0xFFFFFFFF));
        toolbar.addIcon(AndroidUtilities.getIcon(this, R.drawable.ic_dots_menu, 0xFFFFFFFF));
        toolbar.addIcon(AndroidUtilities.getIcon(this, R.drawable.ic_github, 0xFFFFFFFF));

        AppCompatEditText text = new AppCompatEditText(this);
        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        //text.setHintTextColor(0x88ffffff);
        text.setTextColor(0xffffffff);
        text.setSingleLine(true);
        text.setBackgroundResource(0);
        text.requestFocus();
        text.setLayoutParams(LayoutHelper.makeLinear(this, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 16, 16, 0, 0));
        addView(text);
    }
}