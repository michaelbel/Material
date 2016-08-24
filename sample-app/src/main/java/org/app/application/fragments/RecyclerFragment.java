package org.app.application.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
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
import org.app.application.model.RecItem;
import org.app.material.widget.LayoutHelper;
import org.app.material.widget.RecyclerListView;

import java.util.ArrayList;
import java.util.Collections;

public class RecyclerFragment extends Fragment {

    private RecItem mRemovedItem;
    private int mIndexOfRemovedItem;
    private RecyclerListView recyclerView;
    private RecyclerViewAdapter adapter;
    private ArrayList<RecItem> items;

    public class TouchHelperCallback extends ItemTouchHelper.Callback {

        public static final float ALPHA_FULL = 1.0f;

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() != 0) {
                return makeMovementFlags(0, 0);
            }

            return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source,
                              RecyclerView.ViewHolder target) {
            if (source.getItemViewType() != target.getItemViewType()) {
                return false;
            }

            adapter.swapElements(source.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                recyclerView.cancelClickRunnables(false);
                viewHolder.itemView.setPressed(true);
            }

            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setPressed(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup view, Bundle savedInstanceState) {
        FrameLayout fragmentView = new FrameLayout(getActivity());
        fragmentView.setBackgroundColor(0xFFF0F0F0);

        items = new ArrayList<>();
        items.add(new RecItem(1, R.drawable.space1, "1. Primary text", "Secondary text"));
        items.add(new RecItem(2, R.drawable.space2, "2. Primary text", "Secondary text"));
        items.add(new RecItem(3, R.drawable.space3, "3. Primary text", "Secondary text"));
        items.add(new RecItem(4, R.drawable.space4, "4. Primary text", "Secondary text"));
        items.add(new RecItem(5, R.drawable.space5, "5. Primary text", "Secondary text"));
        items.add(new RecItem(6, R.drawable.space6, "6. Primary text", "Secondary text"));
        items.add(new RecItem(7, R.drawable.space1, "7. Primary text", "Secondary text"));
        items.add(new RecItem(8, R.drawable.space2, "8. Primary text", "Secondary text"));
        items.add(new RecItem(9, R.drawable.space3, "9. Primary text", "Secondary text"));

        adapter = new RecyclerViewAdapter(getContext());

        recyclerView = new RecyclerListView(getActivity());
        recyclerView.setFocusable(true);
        recyclerView.setTag(7);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelperCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutParams(LayoutHelper.makeFrame(getActivity(), LayoutHelper.MATCH_PARENT,
                LayoutHelper.MATCH_PARENT));
        recyclerView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                RecItem item = items.get(position);
                Toast.makeText(getContext(), "Item = " + item.getId(), Toast.LENGTH_SHORT).show();
            }
        });

        fragmentView.addView(recyclerView);

        return fragmentView;
    }

    public class RecyclerViewAdapter extends RecyclerListView.Adapter {
        private Context mContext;

        public RecyclerViewAdapter(Context context) {
            this.mContext = context;
        }

        private class Holder extends RecyclerView.ViewHolder {

            public Holder(View view) {
                super(view);

                ((RecyclerCell) view).setOnOptionsClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle(R.string.Options);
                        builder.setItems(new CharSequence[]{
                                getString(R.string.Open),
                                getString(R.string.Remove)
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    final RecItem item = items.get(getAdapterPosition());
                                    Toast.makeText(mContext, getString(R.string.OpeningItem, item.getId()),
                                            Toast.LENGTH_SHORT).show();
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

        public void swapElements(int fromPosition, int toPosition) {
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
        public int getItemCount() {
            return items.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup group, final int type) {
            View view;

            view = new RecyclerCell(mContext);
            view.setBackgroundColor(0xFFFFFFFF);
            //view.setClickable(true);
            //view.setBackgroundResource(AndroidUtilities.selectableItemBackgroundBorderless());
            view.setBackgroundResource(R.drawable.list_selector_white);
            view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT));

            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            RecItem item = items.get(position);

            ((RecyclerCell) viewHolder.itemView)
                    .setImage(item.getImage())
                    .setText1(item.getText1())
                    .setText2(item.getText2())
                    .setDivider(true);
        }
    }
}