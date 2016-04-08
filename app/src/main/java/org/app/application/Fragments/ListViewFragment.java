package org.app.application.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import org.app.material.LayoutHelper;
import org.app.material.adapter.BaseListAdapter;
import org.app.material.cell.HeaderCell;
import org.app.material.cell.RadioCell;
import org.app.material.cell.SwitchCell;
import org.app.material.cell.TextCell;

public class ListViewFragment extends Fragment {

    private ListAdapter adapter;

    private boolean switch1 = true;
    private boolean switch2 = false;
    private boolean switch3 = true;

    private boolean radio1 = false;
    private boolean radio2 = true;
    private boolean radio3 = false;

    private int rowCount;
    private int headerRow1;
    private int headerRow2;
    private int headerRow3;
    private int headerRow4;
    private int textRow1;
    private int textRow2;
    private int textRow3;
    private int textValueRow1;
    private int textValueRow2;
    private int textValueRow3;
    private int switchRow1;
    private int switchRow2;
    private int switchRow3;
    private int radioRow1;
    private int radioRow2;
    private int radioRow3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adapter = new ListAdapter(getActivity());

        rowCount = 0;
        headerRow1 = rowCount++;
        textRow1 = rowCount++;
        textRow2 = rowCount++;
        textRow3 = rowCount++;
        headerRow2 = rowCount++;
        textValueRow1 = rowCount++;
        textValueRow2 = rowCount++;
        textValueRow3 = rowCount++;
        headerRow3 = rowCount++;
        switchRow1 = rowCount++;
        switchRow2 = rowCount++;
        switchRow3 = rowCount++;
        headerRow4 = rowCount++;
        radioRow1 = rowCount++;
        radioRow2 = rowCount++;
        radioRow3 = rowCount++;

        FrameLayout layout = new FrameLayout(getActivity());
        //layout.setBackgroundColor(0xFFF0F0F0);
        //layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        ListView listView = new ListView(getActivity());
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setDrawSelectorOnTop(true);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int i, long id) {
                if (i == switchRow1) {
                    switch1 = !switch1;
                } else if (i == switchRow2) {
                    switch2 = !switch2;
                } else if (i == switchRow3) {
                    switch3 = !switch3;
                } else if (i == radioRow1) {
                    radio1 = true;
                    radio2 = false;
                    radio3 = false;
                } else if (i == radioRow2) {
                    radio1 = false;
                    radio2 = true;
                    radio3 = false;
                } else if (i == radioRow3) {
                    radio1 = false;
                    radio2 = false;
                    radio3 = true;
                }

                adapter.notifyDataSetChanged();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int i, long id) {
                return false;
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

    public class ListAdapter extends BaseListAdapter {

        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public boolean isEnabled(int i) {
            return i == textRow1 || i == textRow2 || i == textRow3 ||
                   i == textValueRow1 || i == textValueRow2 || i == textValueRow3 ||
                   i == switchRow1 || i == switchRow2 || i == switchRow3 ||
                   i == radioRow1 || i == radioRow2 || i == radioRow3;
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
                    view = new TextCell(mContext);
                }

                TextCell cell = (TextCell) view;
                cell.setHeight(48);

                if (i == textRow1) {
                    cell.addTitle("Plain text").addDivider();
                } else if (i == textRow2) {
                    cell.addTitle("Plain text").addDivider();
                } else if (i == textRow3) {
                    cell.addTitle("Plain text");
                } else if (i == textValueRow1) {
                    cell.addTitle("Score").addValue("1000").addDivider();
                } else if (i == textValueRow2) {
                    cell.addTitle("Language").addValue("English").addDivider();
                } else if (i == textValueRow3) {
                    cell.addTitle("Lock").addValue("DISABLED");
                }
            } else if (type == 1) {
                if (view == null) {
                    view = new SwitchCell(mContext);
                }

                SwitchCell cell = (SwitchCell) view;
                cell.setHeight(48);

                if (i == switchRow1) {
                    cell.addTitle("Enable").addSwitch(switch1).addDivider();
                } else if (i == switchRow2) {
                    cell.addTitle("Disable").addSwitch(switch2).addDivider();
                } else if (i == switchRow3) {
                    cell.addTitle("Switch").addSwitch(switch3);
                }
            } else if (type == 2) {
                if (view == null) {
                    view = new RadioCell(mContext);
                }

                RadioCell cell = (RadioCell) view;

                if (i == radioRow1) {
                    cell.addTitle("Radio Button").addRadio(radio1).addDivider();
                } else if (i == radioRow2) {
                    cell.addTitle("Radio Button").addRadio(radio2).addDivider();
                } else if (i == radioRow3) {
                    cell.addTitle("Radio Button").addRadio(radio3);
                }
            } else if (type == 3) {
                if (view == null) {
                    view = new HeaderCell(mContext);
                }

                HeaderCell cell = (HeaderCell) view;

                if (i == headerRow1) {
                    cell.addHead("Plain Text").addDivider();
                } else if (i == headerRow2) {
                    cell.addHead("Text and Value").addDivider();
                } else if (i == headerRow3) {
                    cell.addHead("Switches").addDivider();
                } else if (i == headerRow4) {
                    cell.addHead("Radio Buttons").addDivider();
                }
            }

            return view;
        }

        @Override
        public int getItemViewType(int i) {
            if (i == textRow1 || i == textRow2 || i == textRow3 || i == textValueRow1 || i == textValueRow2 || i == textValueRow3) {
                return 0;
            } else if (i == switchRow1 || i == switchRow2 || i == switchRow3) {
                return 1;
            } else if (i == radioRow1 || i == radioRow2 || i == radioRow3) {
                return 2;
            } else if (i == headerRow1 || i == headerRow2 || i == headerRow3 || i == headerRow4) {
                return 3;
            }

            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 4;
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