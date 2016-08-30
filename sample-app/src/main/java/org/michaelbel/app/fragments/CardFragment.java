package org.michaelbel.app.fragments;

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

import org.michaelbel.app.R;
import org.michaelbel.app.cells.CardCell;
import org.michaelbel.app.model.Card;
import org.michaelbel.material.utils.AndroidUtilities;
import org.michaelbel.material.widget.LayoutHelper;
import org.michaelbel.material.widget.RecyclerListView;

import java.util.ArrayList;

public class CardFragment extends Fragment {

    private ArrayList<Card> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup view, Bundle savedInstanceState) {
        FrameLayout fragmentView = new FrameLayout(getActivity());
        fragmentView.setBackgroundColor(0xFFF0F0F0);

        items = new ArrayList<>();
        items.add(new Card(1, R.drawable.space1, "1. Main text", "Middle text", "Small text"));
        items.add(new Card(2, R.drawable.space2, "2. Main text", "Middle text", "Small text"));
        items.add(new Card(3, R.drawable.space3, "3. Main text", "Middle text", "Small text"));
        items.add(new Card(4, R.drawable.space4, "4. Main text", "Middle text", "Small text"));
        items.add(new Card(5, R.drawable.space5, "5. Main text", "Middle text", "Small text"));
        items.add(new Card(6, R.drawable.space6, "6. Main text", "Middle text", "Small text"));
        items.add(new Card(7, R.drawable.space1, "7. Main text", "Middle text", "Small text"));
        items.add(new Card(8, R.drawable.space2, "8. Main text", "Middle text", "Small text"));
        items.add(new Card(9, R.drawable.space3, "9. Main text", "Middle text", "Small text"));

        RecyclerViewAdapter adapter = new RecyclerViewAdapter();

        RecyclerListView recyclerView = new RecyclerListView(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setInstantClick(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutParams(LayoutHelper.makeFrame(getActivity(), LayoutHelper.MATCH_PARENT,
                LayoutHelper.MATCH_PARENT));
        recyclerView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Card item = items.get(position);
                Toast.makeText(getActivity(), getString(R.string.ClickOnCard, item.getId()),
                        Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() {
            @Override
            public boolean onItemClick(View view, int position) {
                Card item = items.get(position);
                Toast.makeText(getActivity(), getString(R.string.LongClickOnCard, item.getId()),
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        fragmentView.addView(recyclerView);

        return fragmentView;
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public RecyclerViewAdapter() {}

        @Override
        public int getItemCount() {
            return items.size();
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
            Card item = items.get(position);
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
                cardCell.setCardBackgroundColor(0xFFFFFFFF);
                cardCell.setRadius(AndroidUtilities.dp(3.5F));
                cardCell.setCardElevation(AndroidUtilities.dp(1.8F));
                cardCell.setBackgroundResource(R.drawable.list_selector_white);
                cardCell.setLayoutParams(LayoutHelper.makeFrame(getContext(),
                        LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 6, 5, 6, 1));

                cardCell.setOnOptionsClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(R.string.Options);
                        builder.setItems(new CharSequence[]{getString(R.string.Open), getString(R.string.Remove)
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final Card item = items.get(getAdapterPosition());

                                if (i == 0) {
                                    Toast.makeText(getActivity(), getString(R.string.OpeningCard,
                                            item.getId()), Toast.LENGTH_SHORT).show();
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