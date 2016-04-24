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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.app.application.R;
import org.app.application.cells.CardCell;
import org.app.material.AndroidUtilities;
import org.app.material.widget.LayoutHelper;

import java.util.ArrayList;
import java.util.List;

public class CardFragment extends Fragment {

    public class ListItem {

        public int icon;
        public Drawable dotsMenu;
        public String title;
        public String value;

        public ListItem(int icon, String title, String value, Drawable dotsMenu) {
            this.icon = icon;
            this.dotsMenu = dotsMenu;
            this.title = title;
            this.value = value;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout layout = new FrameLayout(getActivity());
        layout.setBackgroundColor(0xFFECEFF1);

        List<ListItem> items = new ArrayList<>();
        items.add(new ListItem(R.mipmap.ic_launcher, "Recycler Item 1", "Recycler Value 1", AndroidUtilities.getIcon(R.drawable.ic_dots_menu, 0xFF757575)));
        items.add(new ListItem(R.mipmap.ic_launcher, "Recycler Item 2", "Recycler Value 2", AndroidUtilities.getIcon(R.drawable.ic_dots_menu, 0xFF757575)));
        items.add(new ListItem(R.mipmap.ic_launcher, "Recycler Item 3", "Recycler Value 3", AndroidUtilities.getIcon(R.drawable.ic_dots_menu, 0xFF757575)));
        items.add(new ListItem(R.mipmap.ic_launcher, "Recycler Item 4", "Recycler Value 4", AndroidUtilities.getIcon(R.drawable.ic_dots_menu, 0xFF757575)));
        items.add(new ListItem(R.mipmap.ic_launcher, "Recycler Item 5", "Recycler Value 5", AndroidUtilities.getIcon(R.drawable.ic_dots_menu, 0xFF757575)));

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(items, getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView recyclerView = new RecyclerView(getActivity());
        recyclerView.setVerticalScrollBarEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        layout.addView(recyclerView, LayoutHelper.makeFrame(getActivity(), LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        return layout;
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.Holder> {

        private Context mContext;
        private List<ListItem> items;

        private RecyclerViewAdapter(List<ListItem> items, Context context){
            this.items = items;
            this.mContext = context;
        }

        public class Holder extends RecyclerView.ViewHolder {

            private Holder(View itemView) {
                super(itemView);
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = new CardCell(mContext);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //int itemPosition = recyclerView.getChildPosition(v);
                }
            });
            ((CardCell) view).setOnOptionsClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            Holder holder = new Holder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int i) {
            ListItem item = items.get(i);
            ((CardCell) holder.itemView).addTitle(item.title).addValue(item.value).addImage(item.icon).addOptionButton(item.dotsMenu);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }
    }
}