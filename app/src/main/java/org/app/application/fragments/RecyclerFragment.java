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

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.app.application.R;
import org.app.application.cells.RecyclerCell;
import org.app.material.widget.ItemTouchHelperClass;
import org.app.material.widget.LayoutHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerFragment extends Fragment {

    private FrameLayout layout;
    private ItemModel mRemovedItem;
    private int mIndexOfRemovedItem;

    public class ItemModel {

        private int mId;
        private int mImage;
        private String mText1;
        private String mText2;

        public ItemModel(int id, int image, String text1, String text2) {
            this.mId = id;
            this.mImage = image;
            this.mText1 = text1;
            this.mText2 = text2;
        }

        public int getId() {
            return mId;
        }

        public int getImage() {
            return mImage;
        }

        public String getText1() {
            return mText1;
        }

        public String getText2() {
            return mText2;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = new FrameLayout(getActivity());
        layout.setBackgroundColor(0xFFF0F0F0);

        ArrayList<ItemModel> items = new ArrayList<>();
        items.add(new ItemModel(0, R.drawable.space1, "Primary text 0", "Secondary text 0"));
        items.add(new ItemModel(1, R.drawable.space2, "Primary text 1", "Secondary text 1"));
        items.add(new ItemModel(2, R.drawable.space3, "Primary text 2", "Secondary text 2"));
        items.add(new ItemModel(3, R.drawable.space4, "Primary text 3", "Secondary text 3"));
        items.add(new ItemModel(4, R.drawable.space5, "Primary text 4", "Secondary text 4"));
        items.add(new ItemModel(5, R.drawable.space6, "Primary text 5", "Secondary text 5"));
        items.add(new ItemModel(6, R.drawable.space1, "Primary text 6", "Secondary text 6"));
        items.add(new ItemModel(7, R.drawable.space2, "Primary text 7", "Secondary text 7"));
        items.add(new ItemModel(8, R.drawable.space3, "Primary text 8", "Secondary text 8"));
        items.add(new ItemModel(9, R.drawable.space4, "Primary text 9", "Secondary text 9"));
        items.add(new ItemModel(10, R.drawable.space5, "Primary text 10", "Secondary text 10"));
        items.add(new ItemModel(11, R.drawable.space6, "Primary text 11", "Secondary text 11"));
        items.add(new ItemModel(12, R.drawable.space1, "Primary text 12", "Secondary text 12"));

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(items);

        RecyclerView recyclerView = new RecyclerView(getActivity());

        ItemTouchHelper.Callback callback = new ItemTouchHelperClass(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutParams(LayoutHelper.makeFrame(getActivity(), LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        layout.addView(recyclerView);

        return layout;
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperClass.ItemTouchHelperAdapter {

        private List<ItemModel> items;

        public RecyclerViewAdapter(List<ItemModel> items) {
            this.items = items;
        }

        @Override
        public void onItemMoved(int fromPosition, int toPosition) {
            if (fromPosition<toPosition){
                for (int i = fromPosition; i < toPosition; i++){
                    Collections.swap(items, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--){
                    Collections.swap(items, i, i - 1);
                }
            }

            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onItemRemoved(final int position) {
            mRemovedItem =  items.remove(position);
            mIndexOfRemovedItem = position;

            notifyItemRemoved(position);
            notifyItemRangeChanged(position, items.size());

            Snackbar.make(layout, getString(R.string.ItemRemoved), Snackbar.LENGTH_LONG).setAction(R.string.Undo, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    items.add(mIndexOfRemovedItem, mRemovedItem);
                    notifyItemInserted(mIndexOfRemovedItem);
                }
            }).show();
        }

        @Override
        public int getItemCount() {
            return this.items.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            RecyclerView.ViewHolder viewHolder;
            viewHolder = new ItemViewHolder(new RecyclerCell(getContext()));

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            ItemModel item = items.get(position);
            ((ItemViewHolder) viewHolder).recyclerCell
                    .setImage(item.getImage())
                    .setText1(item.getText1())
                    .setText2(item.getText2())
                    .setDivider(true);
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            private RecyclerCell recyclerCell;

            public ItemViewHolder(final View itemView) {
                super(itemView);

                recyclerCell = (RecyclerCell) itemView;

                recyclerCell.setOnItemClick(new RecyclerCell.OnRecyclerClickListener() {
                    @Override
                    public void onClick() {
                        int i = getAdapterPosition();
                        Toast.makeText(getActivity(), getString(R.string.ClickOnCard, i), Toast.LENGTH_SHORT).show();
                    }
                });

                recyclerCell.setOnOptionsClick(new RecyclerCell.OnOptionClickListener() {
                    @Override
                    public void onClick() {
                        final ItemModel item = (ItemModel) items.get(getAdapterPosition());

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(R.string.Options);
                        builder.setItems(new CharSequence[]{getString(R.string.Open), getString(R.string.Remove)
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    Toast.makeText(getActivity(), "Opening card " + item.getId(), Toast.LENGTH_SHORT).show();
                                } else if (i == 1) {
                                    items.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                    notifyItemRangeChanged(getAdapterPosition(), items.size());
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }
        }
    }
}