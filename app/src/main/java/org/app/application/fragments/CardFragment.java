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
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.app.application.R;
import org.app.application.cells.CardCell;
import org.app.application.model.CardModel;
import org.app.material.widget.LayoutHelper;

import java.util.ArrayList;
import java.util.List;

public class CardFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout layout = new FrameLayout(getActivity());
        layout.setBackgroundColor(0xFFF0F0F0);

        ArrayList<CardModel> items = new ArrayList<>();

        items.add(new CardModel(1, R.drawable.space1, "1. Main text", "Middle text", "Small text"));
        items.add(new CardModel(2, R.drawable.space2, "2. Main text", "Middle text", "Small text"));
        items.add(new CardModel(3, R.drawable.space3, "3. Main text", "Middle text", "Small text"));
        items.add(new CardModel(4, R.drawable.space4, "4. Main text", "Middle text", "Small text"));
        items.add(new CardModel(5, R.drawable.space5, "5. Main text", "Middle text", "Small text"));
        items.add(new CardModel(6, R.drawable.space6, "6. Main text", "Middle text", "Small text"));
        items.add(new CardModel(7, R.drawable.space1, "7. Main text", "Middle text", "Small text"));
        items.add(new CardModel(8, R.drawable.space2, "8. Main text", "Middle text", "Small text"));
        items.add(new CardModel(9, R.drawable.space3, "9. Main text", "Middle text", "Small text"));

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(items);

        RecyclerView recyclerView = new RecyclerView(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutParams(LayoutHelper.makeFrame(getActivity(), LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        layout.addView(recyclerView);
        return layout;
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<CardModel> items;

        public RecyclerViewAdapter(List<CardModel> items) {
            this.items = items;
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
            viewHolder = new CardViewHolder(new CardCell(getContext()));

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            CardModel item = items.get(position);
            ((CardViewHolder) viewHolder).cardCell
                    .setImage(item.getImage())
                    .setText1(item.getText1())
                    .setText2(item.getText2())
                    .setText3(item.getText3());
        }

        public class CardViewHolder extends RecyclerView.ViewHolder {

            private CardCell cardCell;

            public CardViewHolder(final View itemView) {
                super(itemView);

                cardCell = (CardCell) itemView;

                cardCell.setOnCardClick(new CardCell.OnCardClickListener() {
                    @Override
                    public void onClick() {
                        final CardModel item = items.get(getAdapterPosition());
                        Toast.makeText(getActivity(), getString(R.string.ClickOnCard, item.getId()), Toast.LENGTH_SHORT).show();
                    }
                });

                cardCell.setOnOptionsClick(new CardCell.OnOptionClickListener() {
                    @Override
                    public void onClick() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(R.string.Options);
                        builder.setItems(new CharSequence[]{getString(R.string.Open), getString(R.string.Remove)
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final CardModel item = items.get(getAdapterPosition());

                                if (i == 0) {
                                    Toast.makeText(getActivity(), getString(R.string.OpeningCard, item.getId()), Toast.LENGTH_SHORT).show();
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