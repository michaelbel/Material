/*
 * Copyright 2015-2016 Michael Bel
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

package org.michaelbel.material.widget2;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<View> layouts = new ArrayList<>();
    private List<CharSequence> titles = new ArrayList<>();

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    public void addLayout(View view, CharSequence title) {
        layouts.add(view);
        titles.add(title);
    }

    public void addLayout(View view, @StringRes int stringId) {
        addLayout(view, context.getText(stringId));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getCount() {
        return layouts.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        FrameLayout layout = new FrameLayout(context);
        layout.addView(layouts.get(position));

        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }
}