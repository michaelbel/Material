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

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.app.application.R;
import org.app.application.cells.CardCell;
import org.app.material.widget.LayoutHelper;

import java.util.ArrayList;
import java.util.List;

public class CardFragment extends Fragment {

    public class CardModel {

        private int mImage;
        private String mText1;
        private String mText2;
        private String mText3;

        public CardModel(int image, String text1, String text2, String text3) {
            this.mImage = image;
            this.mText1 = text1;
            this.mText2 = text2;
            this.mText3 = text3;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout layout = new FrameLayout(getActivity());
        layout.setBackgroundColor(0xFFF0F0F0);

        ArrayList<Object> items = new ArrayList<>();
        items.add(new CardModel(R.drawable.space3, "Card main text 0", "Card middle text 0", "Card small text 0"));
        items.add(new CardModel(R.drawable.space3, "Card main text 1", "Card middle text 1", "Card small text 1"));
        items.add(new CardModel(R.drawable.space3, "Card main text 2", "Card middle text 2", "Card small text 2"));
        items.add(new CardModel(R.drawable.space4, "Card main text 3", "Card middle text 3", "Card small text 3"));
        items.add(new CardModel(R.drawable.space5, "Card main text 4", "Card middle text 4", "Card small text 4"));
        items.add(new CardModel(R.drawable.space6, "Card main text 5", "Card middle text 5", "Card small text 5"));
        items.add(new CardModel(R.drawable.space1, "Card main text 6", "Card middle text 6", "Card small text 6"));
        items.add(new CardModel(R.drawable.space2, "Card main text 7", "Card middle text 7", "Card small text 7"));
        items.add(new CardModel(R.drawable.space3, "Card main text 8", "Card middle text 8", "Card small text 8"));
        items.add(new CardModel(R.drawable.space4, "Card main text 9", "Card middle text 9", "Card small text 9"));
        items.add(new CardModel(R.drawable.space5, "Card main text 10", "Card middle text 10", "Card small text 10"));
        items.add(new CardModel(R.drawable.space6, "Card main text 11", "Card middle text 11", "Card small text 11"));

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(items);

        RecyclerView recyclerView = new RecyclerView(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutParams(LayoutHelper.makeFrame(getActivity(), LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        layout.addView(recyclerView);
        return layout;
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        private CardCell cardCell;

        public CardViewHolder(View itemView) {
            super(itemView);

            cardCell = (CardCell) itemView;

            cardCell.setOnCardClick(new CardCell.OnCardClickListener() {
                @Override
                public void onClick() {
                    int i = getAdapterPosition();
                    Toast.makeText(getActivity(), getString(R.string.ClickOnCard, i), Toast.LENGTH_SHORT).show();
                }
            });

            cardCell.setOnOptionsClick(new CardCell.OnOptionClickListener() {
                @Override
                public void onClick() {
                    int i = getAdapterPosition();
                    DialogFragment dialog = new ItemsDialog();
                    dialog.show(getFragmentManager(), "options");
                }
            });
        }
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<Object> items;

        public RecyclerViewAdapter(List<Object> items) {
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
            CardModel item = (CardModel) items.get(position);
            ((CardViewHolder) viewHolder).cardCell
                    .setText1(item.mText1)
                    .setText2(item.mText2)
                    .setText3(item.mText3)
                    .setImage(item.mImage);
        }
    }

    public static class ItemsDialog extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.Options);
            builder.setItems(new CharSequence[]{
                    getString(R.string.Open),
                    getString(R.string.Delete)
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i == 0) {
                        Toast.makeText(getActivity(), getString(R.string.Opening), Toast.LENGTH_SHORT).show();
                    } else if (i == 1) {

                    }
                }
            });

            return builder.create();
        }
    }
}