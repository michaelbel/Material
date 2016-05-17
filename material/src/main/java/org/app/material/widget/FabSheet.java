package org.app.material.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.app.material.AndroidUtilities;
import org.app.material.FabSheet.java.DimOverlayFrameLayout;
import org.app.material.FabSheet.java.Fab;
import org.app.material.FabSheet.java.RevealLinearLayout;
import org.app.material.R;

public class FabSheet extends FrameLayout {

    public Fab fab;
    public DimOverlayFrameLayout dimOverlayFrameLayout;
    public RevealLinearLayout revealLinearLayout;
    public CardView cardView;
    public LinearLayout linearLayout;
    public TextView textView1;
    public TextView textView2;
    public TextView textView3;
    public LinearLayout linearLayoutBottom;
    public TextView textView4;

    public FabSheet(Context context) {
        super(context);
        init(null, 0);
    }

    public FabSheet(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FabSheet(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FabSheet, defStyle, 0);
        a.recycle();

        fab = new Fab(getContext());
        addView(fab, LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END, 16, 16, 16, 16));

        dimOverlayFrameLayout = new DimOverlayFrameLayout(getContext());
        addView(dimOverlayFrameLayout, LayoutHelper.makeFrame(getContext(), LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        revealLinearLayout = new RevealLinearLayout(getContext());
        revealLinearLayout.setOrientation(LinearLayout.VERTICAL);
        revealLinearLayout.setGravity(Gravity.BOTTOM | Gravity.END);
        addView(revealLinearLayout, LayoutHelper.makeFrame(getContext(), LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        cardView = new CardView(getContext());
        revealLinearLayout.addView(cardView, LayoutHelper.makeLinear(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        cardView.addView(linearLayout, LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        textView1 = new TextView(getContext());
        textView1.setCompoundDrawables(AndroidUtilities.getIcon(getContext(), R.drawable.ic_storage, 0xFFFFFFFF), null, null, null);
        textView1.setText("Click me");
        linearLayout.addView(textView1, LayoutHelper.makeLinear(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        textView2 = new TextView(getContext());
        textView2.setCompoundDrawables(AndroidUtilities.getIcon(getContext(), R.drawable.ic_storage, 0xFFFFFFFF), null, null, null);
        textView2.setText("Click me");
        linearLayout.addView(textView2, LayoutHelper.makeLinear(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        textView3 = new TextView(getContext());
        textView3.setCompoundDrawables(AndroidUtilities.getIcon(getContext(), R.drawable.ic_storage, 0xFFFFFFFF), null, null, null);
        textView3.setText("Click me");
        linearLayout.addView(textView3, LayoutHelper.makeLinear(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        linearLayoutBottom = new LinearLayout(getContext());
        linearLayoutBottom.setBackgroundColor(0xFFFFEB3B);
        linearLayout.addView(linearLayoutBottom, LayoutHelper.makeLinear(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        textView4 = new TextView(getContext());
        textView4.setCompoundDrawables(AndroidUtilities.getIcon(getContext(), R.drawable.ic_storage, 0xFFFFFFFF), null, null, null);
        textView4.setText("Click me");
        linearLayoutBottom.addView(textView4);
    }
}