/*
 * Copyright 2015 Michael Bel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.michaelbel.material.widget;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class FragmentsPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<CharSequence> mTitles = new ArrayList<>();

    public FragmentsPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        mContext = context;
    }

    public void addFragment(Fragment fragment, CharSequence title) {
        mFragments.add(fragment);
        mTitles.add(title);
    }

    public void addFragment(Fragment fragment, @StringRes int stringId) {
        addFragment(fragment, mContext.getText(stringId));
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}