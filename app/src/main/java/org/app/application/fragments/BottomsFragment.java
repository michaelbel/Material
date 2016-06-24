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

package org.app.application.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.app.application.R;
import org.app.application.cells.TextCell;
import org.app.application.dialogs.BottomSheetDialog;
import org.app.material.widget.BottomPickerLayout;
import org.app.material.widget.LayoutHelper;

public class BottomsFragment extends Fragment {

    private int rowCount;
    private int bottomBarRow;
    private int bottomSheetRow;
    private int bottomPickerRow;

    private boolean plVisible = false;
    private BottomPickerLayout pickerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout layout = new FrameLayout(getActivity());
        layout.setBackgroundColor(0xFFF0F0F0);

        pickerLayout = new BottomPickerLayout(getActivity());
        pickerLayout.setVisibility(View.INVISIBLE);
        pickerLayout.setLayoutParams(LayoutHelper.makeFrame(getActivity(), LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM));
        pickerLayout.setPositiveButton(R.string.Done, new BottomPickerLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), getString(R.string.Done), Toast.LENGTH_SHORT).show();
            }
        });
        pickerLayout.setNegativeButton(R.string.Cancel, new BottomPickerLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), getString(R.string.Cancel), Toast.LENGTH_SHORT).show();
            }
        });

        layout.addView(pickerLayout);

        rowCount = 0;
        bottomBarRow = rowCount++;
        bottomSheetRow = rowCount++;
        bottomPickerRow = rowCount++;

        ListView listView = new ListView(getActivity());
        listView.setDividerHeight(0);
        listView.setDrawSelectorOnTop(true);
        listView.setAdapter(new ListViewAdapter());
        listView.setLayoutParams(LayoutHelper.makeFrame(getActivity(), LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int i, long id) {
                if (i == bottomBarRow) {
                    Toast.makeText(getActivity(), "BottomBar is not implemented", Toast.LENGTH_SHORT).show();
                } else if (i == bottomSheetRow) {
                    DialogFragment dialog = new BottomSheetDialog();
                    dialog.show(getFragmentManager(), "bottomSheet");
                } else if (i == bottomPickerRow) {
                    if (plVisible = !plVisible) {
                        pickerLayout.setVisibility(View.VISIBLE);
                    } else {
                        pickerLayout.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        layout.addView(listView);

        return layout;
    }

    public class ListViewAdapter extends BaseAdapter {

        /*@Override
        public boolean isEnabled(int i) {
            return i == bottomBarRow || i == bottomSheetRow || i == bottomPickerRow;
        }*/

        @Override
        public int getCount() {
            return rowCount;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextCell cell = new TextCell(getActivity());

            if (i == bottomBarRow) {
                cell.setText(R.string.BottomBar).setDivider(true);
            } else if (i == bottomSheetRow) {
                cell.setText(R.string.BottomSheetDialog).setDivider(true);
            } else if (i == bottomPickerRow) {
                cell.setText(R.string.BottomPickerLayout);
            }

            return cell;
        }
    }
}