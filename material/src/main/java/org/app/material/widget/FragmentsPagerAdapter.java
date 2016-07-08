/*
 * Copyright 2015 Michael Bel
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

package org.app.material.widget;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentsPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private final List<Fragment> fragments = new ArrayList<>();
    private final List<CharSequence> titles = new ArrayList<>();

    public FragmentsPagerAdapter(Context context, FragmentManager manager) {
        super(manager);

        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        titles.add(title);
    }

    public void addFragment(Fragment fragment, @StringRes int resId) {
        fragments.add(fragment);
        titles.add(context.getResources().getString(resId));
    }

    public void addFragments(FragmentItem... fragmentSet) {
        for (FragmentItem set : fragmentSet) {
            fragments.add(set.getFragment());
            titles.add(context.getResources().getString(set.getResId()));
        }
    }

    /*public void addFragments(FragmentSet... fragmentSet) {
        for (FragmentSet set : fragmentSet) {
            fragments.add(set.getFragment());
            titles.add(set.getTitle());
        }
    }*/

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}