package org.app.material;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import org.app.material.adapter.BaseListAdapter;
import org.app.material.cell.TextCell;

import java.util.ArrayList;

public class PopupMenu extends FrameLayout {

    private ListAdapter listAdapter;
    private ArrayList<List> items = new ArrayList<>();

    public class List {
        public String title = null;
    }

    public PopupMenu(Context context) {
        super(context);

        this.setBackgroundColor(0xffffffff);
        listAdapter = new ListAdapter(context);

        ListView listView = new ListView(context);
        listView.setAdapter(listAdapter);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setBackgroundColor(0xffffffff);
        addView(listView, LayoutHelper.makeFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
    }

    public void addItem(int position, String title) {
        List list = new List();
        list.title = title;
        items.add(position, list);
        listAdapter.notifyDataSetChanged();
    }

    private class ListAdapter extends BaseListAdapter {

        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = new TextCell(mContext);
            }

            TextCell cell = (TextCell) view;
            List item = items.get(position);
            cell.addTitle(item.title);
            return view;
        }
    }
}