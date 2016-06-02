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

package org.app.application.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import org.app.application.R;
import org.app.application.cells.ListCell;
import org.app.application.cells.EmptyCell;
import org.app.material.widget.LayoutHelper;

public class ListViewFragment extends Fragment {

    private ListAdapter mAdapter;

    private int rowCount;
    private int textRow;
    private int textValueRow;
    private int headerRow1;
    private int headerRow2;
    private int noteRow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout layout = new FrameLayout(getActivity());
        layout.setBackgroundColor(0xFFECEFF1);

        mAdapter = new ListAdapter(getActivity());

        rowCount = 0;
        headerRow1 = rowCount++;
        textRow = rowCount++;
        headerRow2 = rowCount++;
        textValueRow = rowCount++;
        noteRow = rowCount++;

        ListView listView = new ListView(getActivity());
        listView.setLayoutParams(LayoutHelper.makeFrame(getActivity(), LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        listView.setAdapter(mAdapter);
        listView.setDividerHeight(0);
        listView.setDrawSelectorOnTop(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int i, long id) {
                if (i == textRow) {
                    Snackbar.make(view, getString(R.string.Position, i), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                } else if (i == textValueRow) {
                    Snackbar.make(view, getString(R.string.Position, i), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            }
        });

        layout.addView(listView);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public class ListAdapter extends android.widget.BaseAdapter {

        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public boolean isEnabled(int i) {
            return i == textRow || i == textValueRow;
        }

        @Override
        public int getCount() {
            return rowCount;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {
            int type = getItemViewType(i);

            if (type == 0) {
                if (view == null) {
                    view = new ListCell(mContext);
                }

                ListCell cell = (ListCell) view;

                if (i == textRow) {
                    cell.addTitle(R.string.SomeTitleText);
                } else if (i == textValueRow) {
                    cell.addTitle(R.string.TextLock).addValue(R.string.TextDisabled);
                }
            } else if (type == 1) {
                if (view == null) {
                    view = new EmptyCell(mContext);
                }

                EmptyCell cell = (EmptyCell) view;

                if (i == headerRow1) {
                    cell.addHead(R.string.PlainText);
                } else if (i == headerRow2) {
                    cell.addHead(R.string.TextAndValue);
                } else if (i == noteRow) {
                    cell.addNote(R.string.ListViewNote);
                }
            }

            return view;
        }

        @Override
        public int getItemViewType(int i) {
            if (i == textRow || i == textValueRow) {
                return 0;
            } else if (i == headerRow1 || i == headerRow2 || i == noteRow) {
                return 1;
            }

            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }
    }
}