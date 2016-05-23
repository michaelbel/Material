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
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentActivity;
import android.widget.LinearLayout;

import org.app.material.AndroidUtilities;
import org.app.material.widget.LayoutHelper;
import org.app.material.widget.Toolbar;

public class ViewController extends FragmentActivity {

    public Toolbar toolbar;
    public LinearLayout viewLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CoordinatorLayout containerLayout = new CoordinatorLayout(this);
        containerLayout.setLayoutParams(LayoutHelper.makeCoordinator(this, LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        toolbar = new Toolbar(this);
        toolbar.setTitle(R.string.MaterialDemo);
        toolbar.setNavIcon(R.drawable.ic_menu);
        //toolbar.setNavIconClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        Toast.makeText(ViewController.this, "Menus", Toast.LENGTH_SHORT).show();
        //    }
        //});

        toolbar.addIcon(R.drawable.ic_dots_menu);
        toolbar.addIcon(R.drawable.ic_github);

        containerLayout.addView(toolbar);

        viewLayout = new LinearLayout(this);
        viewLayout.setOrientation(LinearLayout.VERTICAL);
        viewLayout.setPadding(0, AndroidUtilities.dp(this, 56), 0, 0);
        viewLayout.setLayoutParams(LayoutHelper.makeLinear(this, LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        containerLayout.addView(viewLayout);

        setContentView(containerLayout);
    }
}