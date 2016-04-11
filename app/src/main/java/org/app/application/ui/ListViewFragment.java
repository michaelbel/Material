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
import org.app.material.LayoutHelper;
import org.app.material.adapter.BaseAdapter;
import org.app.material.cell.EmptyCell;

public class ListViewFragment extends Fragment {

    private ListAdapter adapter;

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

        adapter = new ListAdapter(getActivity());

        rowCount = 0;
        headerRow1 = rowCount++;
        textRow = rowCount++;
        headerRow2 = rowCount++;
        textValueRow = rowCount++;
        noteRow = rowCount++;

        ListView listView = new ListView(getActivity());
        listView.setDivider(null);
        listView.setAdapter(adapter);
        listView.setDividerHeight(0);
        listView.setDrawSelectorOnTop(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int i, long id) {
                if (i == textRow) {
                    Snackbar.make(view, "Clicked on position = " + i, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                } else if (i == textValueRow) {
                    Snackbar.make(view, "Clicked on position = " + i, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            }
        });

        layout.addView(listView, LayoutHelper.makeFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public class ListAdapter extends BaseAdapter {
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
        public View getView(int i, View view, ViewGroup parent) {
            int type = getItemViewType(i);

            if (type == 0) {
                if (view == null) {
                    view = new ListCell(mContext);
                }

                ListCell cell = (ListCell) view;

                if (i == textRow) {
                    cell.addTitle(getResources().getString(R.string.SomeTitleText));
                } else if (i == textValueRow) {
                    cell.addTitle(getResources().getString(R.string.TextLock)).addValue(getResources().getString(R.string.TextDisabled).toUpperCase());
                }
            } else if (type == 1) {
                if (view == null) {
                    view = new EmptyCell(mContext);
                }

                EmptyCell cell = (EmptyCell) view;

                if (i == headerRow1) {
                    cell.addHead(getResources().getString(R.string.PlainText), true);
                } else if (i == headerRow2) {
                    cell.addHead(getResources().getString(R.string.TextAndValue), true);
                } else if (i == noteRow) {
                    cell.addNote(getResources().getString(R.string.ListViewNoteRow));
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

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }
}