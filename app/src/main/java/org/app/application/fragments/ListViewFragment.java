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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.app.application.R;
import org.app.application.cells.CheckBoxCell;
import org.app.application.cells.DoubleCell;
import org.app.application.cells.EmptyCell;
import org.app.application.cells.RadioCell;
import org.app.application.cells.SwitchCell;
import org.app.application.cells.TextCell;
import org.app.material.widget.LayoutHelper;

public class ListViewFragment extends Fragment {

    private int rowCount;
    private int textRow;
    private int textValueRow1;
    private int textValueRow2;
    private int switchRow;
    private int checkBoxRow;
    private int doubleTextRow;
    private int radioRow;
    private int headerRow1;
    private int headerRow2;
    private int headerRow3;
    private int headerRow4;
    private int headerRow5;
    private int headerRow6;
    private int headerRow7;

    private boolean switchParam = true;
    private boolean checkBoxParam = true;
    private boolean radioParam = true;

    private ListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout layout = new FrameLayout(getActivity());
        layout.setBackgroundColor(0xFFF0F0F0);

        rowCount = 0;
        headerRow1 = rowCount++;
        textRow = rowCount++;
        headerRow2 = rowCount++;
        textValueRow1 = rowCount++;
        textValueRow2 = rowCount++;
        headerRow4 = rowCount++;
        doubleTextRow = rowCount++;
        headerRow3 = rowCount++;
        switchRow = rowCount++;
        headerRow5 = rowCount++;
        checkBoxRow = rowCount++;
        headerRow6 = rowCount++;
        radioRow = rowCount++;
        //headerRow7 = rowCount++;

        adapter = new ListAdapter();

        ListView listView = new ListView(getActivity());
        listView.setLayoutParams(LayoutHelper.makeFrame(getActivity(), LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        listView.setAdapter(adapter);
        listView.setDividerHeight(0);
        listView.setDrawSelectorOnTop(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int i, long id) {
                if (i == textRow || i == textValueRow1 || i == textValueRow2 || i == doubleTextRow) {
                    Toast.makeText(getActivity(), getString(R.string.Position, i), Toast.LENGTH_SHORT).show();
                } else if (i == switchRow) {
                    switchParam = !switchParam;
                    if (view instanceof SwitchCell) {
                        ((SwitchCell) view).setChecked(switchParam);
                    }
                } else if (i == checkBoxRow) {
                    checkBoxParam = !checkBoxParam;
                    if (view instanceof CheckBoxCell) {
                        ((CheckBoxCell) view).setChecked(checkBoxParam, true);
                    }
                } else if (i == radioRow) {
                    radioParam = !radioParam;
                    if (view instanceof RadioCell) {
                        ((RadioCell) view).setChecked(radioParam, true);
                    }
                }
            }
        });

        layout.addView(listView);
        return layout;
    }

    public class ListAdapter extends BaseAdapter {

        @Override
        public boolean isEnabled(int i) {
            return i == textRow || i == textValueRow1 || i == textValueRow2 || i == switchRow ||
                   i == doubleTextRow || i == checkBoxRow || i == radioRow;
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
                    view = new TextCell(getActivity());
                }

                TextCell cell = (TextCell) view;

                if (i == textRow) {
                    cell.setText(R.string.SomeTitleText);
                } else if (i == textValueRow1) {
                    cell.setText(R.string.TextLock).setValue(R.string.TextDisabled).setDivider(true);
                } else if (i == textValueRow2) {
                    cell.setText(R.string.Size).setValue(String.valueOf(16));
                }
            } else if (type == 1) {
                if (view == null) {
                    view = new EmptyCell(getActivity());
                }

                EmptyCell cell = (EmptyCell) view;

                if (i == headerRow1) {
                    cell.setHeader(R.string.PlainText);
                } else if (i == headerRow2) {
                    cell.setHeader(R.string.TextAndValue);
                } else if (i == headerRow3) {
                    cell.setHeader(R.string.Switch);
                } else if (i == headerRow4) {
                    cell.setHeader(R.string.DoubleText);
                } else if (i == headerRow5) {
                    cell.setHeader(R.string.CheckBox);
                } else if (i == headerRow6) {
                    cell.setHeader(R.string.RadioButton);
                } else if (i == headerRow7) {
                    cell.setHeader(null);
                }
            } else if (type == 2) {
                if (view == null) {
                    view = new SwitchCell(getActivity());
                }

                SwitchCell cell = (SwitchCell) view;

                if (i == switchRow) {
                    cell.setText(R.string.EnableMode).setChecked(switchParam);
                }
            } else if (type == 3) {
                if (view == null) {
                    view = new DoubleCell(getActivity());
                }

                DoubleCell cell = (DoubleCell) view;

                if (i == doubleTextRow) {
                    cell.setText(R.string.FirstLine).setValue(R.string.SecondLine);
                }
            } else if (type == 4) {
                if (view == null) {
                    view = new CheckBoxCell(getActivity());
                }

                CheckBoxCell cell = (CheckBoxCell) view;

                if (i == checkBoxRow) {
                    cell.setText(R.string.Notifications).setChecked(checkBoxParam, false);
                }
            } else if (type == 5) {
                if (view == null) {
                    view = new RadioCell(getActivity());
                }

                RadioCell cell = (RadioCell) view;

                if (i == radioRow) {
                    cell.setText(R.string.Select).setChecked(radioParam, false);
                }
            }

            return view;
        }

        @Override
        public int getItemViewType(int i) {
            if (i == textRow || i == textValueRow1 || i == textValueRow2) {
                return 0;
            } else if (i == headerRow1 || i == headerRow2 || i == headerRow3 || i == headerRow4 || i == headerRow5 || i == headerRow6 || i == headerRow7) {
                return 1;
            } else if (i == switchRow) {
                return 2;
            } else if (i == doubleTextRow) {
                return 3;
            } else if (i == checkBoxRow) {
                return 4;
            } else if (i == radioRow) {
                return 5;
            }

            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 6;
        }
    }
}