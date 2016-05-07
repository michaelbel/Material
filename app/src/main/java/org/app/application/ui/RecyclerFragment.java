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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.app.application.R;
import org.app.material.AndroidUtilities;
import org.app.material.widget.LayoutHelper;
import org.app.material.cell.EmptyCell;
import org.app.material.cell.IconCell;
import org.app.material.cell.TextCell;

public class RecyclerFragment extends Fragment {

    private boolean mCheck;
    private boolean mSwitch;
    private boolean mRadio1;
    private boolean mRadio2;

    private int rowCount;
    private int textRow;
    private int textValueRow;
    private int imageValueRow;
    private int radioRow1;
    private int radioRow2;
    private int checkBoxRow;
    private int switchRow;
    private int iconRow1;
    private int iconRow2;
    private int headerRow1;
    private int headerRow2;
    private int headerRow3;
    private int noteRow1;
    private int noteRow2;
    private int noteRow3;
    private int noteRow4;
    private int noteRow5;

    private RecyclerView recyclerView;
    private ListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout layout = new FrameLayout(getActivity());
        layout.setBackgroundColor(0xFFECEFF1);

        mCheck = true;
        mSwitch = true;
        mRadio1 = true;
        mRadio2 = false;

        rowCount = 0;
        headerRow1 = rowCount++;
        textRow = rowCount++;
        headerRow2 = rowCount++;
        textValueRow = rowCount++;
        noteRow1 = rowCount++;
        imageValueRow = rowCount++;
        noteRow2 = rowCount++;
        radioRow1 = rowCount++;
        radioRow2 = rowCount++;
        noteRow3 = rowCount++;
        checkBoxRow = rowCount++;
        noteRow4 = rowCount++;
        switchRow = rowCount++;
        noteRow5 = rowCount++;
        headerRow3 = rowCount++;
        iconRow1 = rowCount++;
        iconRow2 = rowCount++;

        listAdapter = new ListAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        recyclerView = new RecyclerView(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setVerticalScrollBarEnabled(true);

        layout.addView(recyclerView, LayoutHelper.makeFrame(getActivity(), LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        return layout;
    }

    private class ListAdapter extends RecyclerView.Adapter {

        private Context mContext;

        private class Holder extends RecyclerView.ViewHolder {

            public Holder(View itemView) {
                super(itemView);
            }
        }

        public ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getItemCount() {
            return rowCount;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
            if (holder.getItemViewType() == 0) {
                if (i == textRow) {
                    ((TextCell) holder.itemView).addTitle(getResources().getString(R.string.SomeTitleText));
                } else if (i == textValueRow) {
                    ((TextCell) holder.itemView).addTitle(getResources().getString(R.string.TextLock)).addValue(getResources().getString(R.string.TextDisabled).toUpperCase());
                } else if (i == imageValueRow) {
                    ((TextCell) holder.itemView).addTitle("Settings").addImage(AndroidUtilities.getIcon(getActivity(), R.drawable.ic_chevron_right, 0xFFBDBDBD));
                } else if (i == radioRow1) {
                    ((TextCell) holder.itemView).addTitle("Radio 1").addRadio(mRadio1);
                } else if (i == radioRow2) {
                    ((TextCell) holder.itemView).addTitle("Radio 2").addRadio(mRadio2);
                } else if (i == checkBoxRow) {
                    ((TextCell) holder.itemView).addTitle("CheckBox 1").addCheckBox(mCheck);
                }else if (i == switchRow) {
                    ((TextCell) holder.itemView).addTitle("CheckBox 1").addSwitch(mSwitch);
                }
            } else if (holder.getItemViewType() == 1) {
                if (i == headerRow1) {
                    ((EmptyCell) holder.itemView).addHead(R.string.PlainText);
                } else if (i == noteRow1) {
                    ((EmptyCell) holder.itemView).addNote(R.string.TextAndValue);
                } else if (i == headerRow2) {
                    ((EmptyCell) holder.itemView).addHead(R.string.TextAndValue);
                } else if (i == headerRow3) {
                    ((EmptyCell) holder.itemView).addHead("Icon and Text");
                } else if (i == noteRow2) {
                    ((EmptyCell) holder.itemView).addNote("Title text and Value image");
                } else if (i == noteRow3) {
                    ((EmptyCell) holder.itemView).addNote("Title text And Radio button");
                } else if (i == noteRow4) {
                    ((EmptyCell) holder.itemView).addNote("Title text and CheckBox");
                } else if (i == noteRow5) {
                    ((EmptyCell) holder.itemView).addNote("Title text and Switch");
                }
            } else if (holder.getItemViewType() == 2) {
                if (i == iconRow1) {
                    ((IconCell) holder.itemView).addTitle("Title one").addIcon(AndroidUtilities.getIcon(getActivity(), R.drawable.ic_heart, 0xFFF44336)).addShortDivider();
                } else if (i == iconRow2) {
                    ((IconCell) holder.itemView).addTitle("Settings").addIcon(AndroidUtilities.getIcon(getActivity(), R.drawable.ic_heart, 0xFF4285f4));
                }
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;

            if (viewType == 0) {
                view = new TextCell(mContext);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = recyclerView.getChildAdapterPosition(v);

                        if (i == textRow) {
                            Snackbar.make(v, getString(R.string.Position, i), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        } else if (i == textValueRow) {
                            Snackbar.make(v, getString(R.string.Position, i), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        } else if (i == imageValueRow) {
                            Snackbar.make(v, getString(R.string.Position, i), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        } else if (i == radioRow1) {
                            mRadio1 = true;
                            mRadio2 = false;
                        } else if (i == radioRow2) {
                            mRadio1 = false;
                            mRadio2 = true;
                        } else if (i == checkBoxRow) {
                            mCheck = !mCheck;
                        } else if (i == switchRow) {
                            mSwitch = !mSwitch;
                        }

                        //listAdapter.notifyDataSetChanged();
                    }
                });

                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return true;
                    }
                });
            } else if (viewType == 1) {
                view = new EmptyCell(mContext);
            } else if (viewType == 2) {
                view = new IconCell(mContext);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = recyclerView.getChildAdapterPosition(v);

                        if (i == iconRow1) {
                            Snackbar.make(v, getString(R.string.Position, i), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        } else if (i == iconRow2) {
                            Snackbar.make(v, getString(R.string.Position, i), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        }
                    }
                });
            }

            return new Holder(view);
        }

        @Override
        public int getItemViewType(int i) {
            if (i == textRow || i == textValueRow || i == imageValueRow || i == radioRow1 || i == radioRow2 || i == checkBoxRow || i == switchRow) {
                return 0;
            } else if (i == headerRow1 || i == headerRow2 || i == headerRow3 || i == noteRow1 || i == noteRow2 || i == noteRow3 || i == noteRow4 || i == noteRow5) {
                return 1;
            } else if (i == iconRow1 || i == iconRow2) {
                return 2;
            }

            return 0;
        }
    }
}