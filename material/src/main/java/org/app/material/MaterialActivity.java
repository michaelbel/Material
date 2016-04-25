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

package org.app.material;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.app.material.widget.Toolbar;
import org.app.material.widget.LayoutHelper;

public class MaterialActivity extends FragmentActivity {

    public LinearLayout containerLayout;
    public ScrollView scrollView;
    public Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        containerLayout = new LinearLayout(this);

        scrollView = new ScrollView(this);

        toolbar = new Toolbar(this);
        toolbar.setToolbarColor(AndroidUtilities.getContextColor(this, R.attr.colorPrimary));
        toolbar.initialise();

        containerLayout.addView(toolbar, LayoutHelper.makeLinear(this, LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.TOP));
        containerLayout.addView(scrollView, LayoutHelper.makeLinear(this, LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, 0, toolbar.getHeight(), 0, 0));
        setContentView(containerLayout, LayoutHelper.makeLinear(this, LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
    }
}