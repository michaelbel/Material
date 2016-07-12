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
import org.app.application.model.RecyclerItemModel;
import org.app.material.widget.ItemTouchHelperClass;
import org.app.material.widget.LayoutHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerFragment extends Fragment {

    private FrameLayout layout;
    private RecyclerItemModel mRemovedItem;
    private int mIndexOfRemovedItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = new FrameLayout(getActivity());
        layout.setBackgroundColor(0xFFF0F0F0);

        ArrayList<RecyclerItemModel> items = new ArrayList<>();

        items.add(new RecyclerItemModel(1, R.drawable.space1, "1. Primary text", "Secondary text"));
        items.add(new RecyclerItemModel(2, R.drawable.space2, "2. Primary text", "Secondary text"));
        items.add(new RecyclerItemModel(3, R.drawable.space3, "3. Primary text", "Secondary text"));
        items.add(new RecyclerItemModel(4, R.drawable.space4, "4. Primary text", "Secondary text"));
        items.add(new RecyclerItemModel(5, R.drawable.space5, "5. Primary text", "Secondary text"));
        items.add(new RecyclerItemModel(6, R.drawable.space6, "6. Primary text", "Secondary text"));
        items.add(new RecyclerItemModel(7, R.drawable.space1, "7. Primary text", "Secondary text"));
        items.add(new RecyclerItemModel(8, R.drawable.space2, "8. Primary text", "Secondary text"));
        items.add(new RecyclerItemModel(9, R.drawable.space3, "9. Primary text", "Secondary text"));

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

        private List<RecyclerItemModel> items;

        public RecyclerViewAdapter(List<RecyclerItemModel> items) {
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

            Snackbar.make(layout, getString(R.string.ItemRemoved), Snackbar.LENGTH_SHORT).setAction(R.string.Undo, new View.OnClickListener() {
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
            RecyclerItemModel item = items.get(position);
            ((ItemViewHolder) viewHolder).recyclerCell
                    .setImage(item.getImage())
                    .setText1(item.getText1())
                    .setText2(item.getText2())
                    .withDivider(true);
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            private RecyclerCell recyclerCell;

            public ItemViewHolder(final View itemView) {
                super(itemView);

                recyclerCell = (RecyclerCell) itemView;

                recyclerCell.setOnItemClick(new RecyclerCell.OnRecyclerClickListener() {
                    @Override
                    public void onClick() {
                        final RecyclerItemModel item = items.get(getAdapterPosition());
                        Toast.makeText(getActivity(), getString(R.string.ClickOnItem, item.getId()), Toast.LENGTH_SHORT).show();
                    }
                });

                recyclerCell.setOnOptionsClick(new RecyclerCell.OnOptionClickListener() {
                    @Override
                    public void onClick() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(R.string.Options);
                        builder.setItems(new CharSequence[]{getString(R.string.Open), getString(R.string.Remove)
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    final RecyclerItemModel item = items.get(getAdapterPosition());
                                    Toast.makeText(getActivity(), getString(R.string.OpeningItem, item.getId()), Toast.LENGTH_SHORT).show();
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