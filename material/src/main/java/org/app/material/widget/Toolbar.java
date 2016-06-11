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

package org.app.material.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.app.material.AndroidUtilities;
import org.app.material.R;

import java.util.ArrayList;

public class Toolbar extends FrameLayout {

    private TextView mTitleTextView;
    private TextView mSubtitleTextView;
    private LinearLayout ToolbarIcons;

    private ViewPropertyAnimatorCompat mTranslationAnimator;
    public ArrayList<ToolbarIcon> mToolbarIcons = new ArrayList<>();

    public static org.app.material.widget.OnClickListener navIconClickListener;
    public static org.app.material.widget.OnClickListener tabIconClickListener;

    public Toolbar(Context context) {
        super(context);
        init(context, null, 0);
    }

    public Toolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.MATCH_PARENT, 56, Gravity.TOP));

        ToolbarIcons = new LinearLayout(context);
        ToolbarIcons.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.END));
        ToolbarIcons.setGravity(Gravity.CENTER);
        ToolbarIcons.setOrientation(LinearLayout.HORIZONTAL);
        addView(ToolbarIcons, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.END));

        //TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.Toolbar, defStyleAttr, 0);
/*
        String[] items = {"128 Bit", "192 Bit", "256 Bit"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner mSpinnerLength = new Spinner(getContext());
        mSpinnerLength.setAdapter(adapter);
        mSpinnerLength.setPrompt("Длина ключа AES");
        mSpinnerLength.setSelection(0);
        mSpinnerLength.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL));
        mSpinnerLength.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i == 0) {
                    //Rijndael.setKeyLength(Rijndael.AES128);
                } else if (i == 1) {
                    //Rijndael.setKeyLength(Rijndael.AES192);
                } else if (i == 2) {
                    //Rijndael.setKeyLength(Rijndael.AES256);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Rijndael.setKeyLength(Rijndael.AES128);
            }
        });*/

        //attr.recycle();

        this.setBackgroundColor(AndroidUtilities.getContextColor(context, R.attr.colorPrimary));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public Toolbar setTitle(String title) {
        createTitleTextView();
        mTitleTextView.setText(title);
        return this;
    }

    public Toolbar setTitle(@StringRes int resId) {
        createTitleTextView();
        mTitleTextView.setText(getContext().getResources().getText(resId));
        return this;
    }

    public Toolbar setNavIcon(int navIcon) {
        createNavIconView(navIcon);
        return this;
    }

    public Toolbar setNavIconClickListener(org.app.material.widget.OnClickListener listener) {
        navIconClickListener = listener;
        return this;
    }

    private void createTitleTextView() {
        if (mTitleTextView != null) {
            return;
        }
        mTitleTextView = new TextView(getContext());
        mTitleTextView.setSingleLine(true);
        mTitleTextView.setTextColor(0xFFFFFFFF);
        mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
        mTitleTextView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        if (AndroidUtilities.isLandscape(getContext())) {
            mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }
        mTitleTextView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        mTitleTextView.setPadding(AndroidUtilities.dp(getContext(), 16), 0, 0, 0);
        mTitleTextView.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL));
        addView(mTitleTextView);
    }

    private void createNavIconView(int mNavIcon) {
        mTitleTextView.setPadding(AndroidUtilities.dp(getContext(), 56), 0, 0, 0);
        //mSubtitleTextView.setPadding(AndroidUtilities.dp(getContext(), 56), 0, 0, 0);

        FrameLayout layout = new FrameLayout(getContext());
        layout.setClickable(true);
        layout.setLayoutParams(LayoutHelper.makeFrame(getContext(), 48, LayoutHelper.MATCH_PARENT, Gravity.START));
        layout.setBackgroundResource(AndroidUtilities.selectableItemBackgroundBorderless(getContext()));
        layout.setPadding(AndroidUtilities.dp(getContext(), 12), AndroidUtilities.dp(getContext(), 16), AndroidUtilities.dp(getContext(), 12), AndroidUtilities.dp(getContext(), 16));

        ImageView icon = new ImageView(getContext());
        icon.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER));
        icon.setImageResource(mNavIcon);
        layout.addView(icon);

        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navIconClickListener != null) {
                    navIconClickListener.onClick();
                }
            }
        });
        addView(layout);
    }


//==================================================================================================

    public static class ToolbarIcon extends FrameLayout {
        public ImageView mIconView;

        public ToolbarIcon(Context context) {
            super(context);

            this.setClickable(true);
            this.setLayoutParams(LayoutHelper.makeFrame(context, 48, LayoutHelper.MATCH_PARENT, Gravity.END));
            this.setBackgroundResource(AndroidUtilities.selectableItemBackgroundBorderless(context));
            this.setPadding(AndroidUtilities.dp(context, 12), AndroidUtilities.dp(context, 16), AndroidUtilities.dp(context, 12), AndroidUtilities.dp(context, 16));

            mIconView = new ImageView(context);
            mIconView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER));
            addView(mIconView);
        }

        public ToolbarIcon setToolbarIcon(int icon) {
            mIconView.setImageResource(icon);
            return this;
        }

        public ToolbarIcon setOnClick(org.app.material.widget.OnClickListener listener) {
            tabIconClickListener = listener;

            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tabIconClickListener != null) {
                        tabIconClickListener.onClick();
                    }
                }
            });

            return this;
        }
    }

    public Toolbar addIcons(ToolbarIcon... toolbarIcon) {
        for (ToolbarIcon icon : toolbarIcon) {
            mToolbarIcons.add(0, icon);
        }

        for (ToolbarIcon icons : mToolbarIcons) {
            ToolbarIcons.addView(icons, LayoutHelper.makeLinear(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END));
        }

        return this;
    }

    public Toolbar removeAllIcons() {
        mToolbarIcons.clear();
        return this;
    }

    public Toolbar removeIconById(int id) {
        mToolbarIcons.remove(id);
        return this;
    }

    public Toolbar setSubtitle(String text) {
        createSubTitle();
        mSubtitleTextView.setText(text);
        return this;
    }

    public Toolbar setSubtitle(@StringRes int resId) {
        createSubTitle();
        mSubtitleTextView.setText(getContext().getResources().getText(resId));
        return this;
    }

    private void createSubTitle() {
        mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        mTitleTextView.setPadding(AndroidUtilities.dp(getContext(), 16), AndroidUtilities.dp(getContext(), 8), 0, 0);
        if (AndroidUtilities.isLandscape(getContext())) {
            mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            mTitleTextView.setPadding(AndroidUtilities.dp(getContext(), 16), AndroidUtilities.dp(getContext(), 13), 0, 0);
        }
        mTitleTextView.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.TOP));

        if (mSubtitleTextView != null) {
            return;
        }
        mSubtitleTextView = new TextView(getContext());
        mSubtitleTextView.setSingleLine(true);
        mSubtitleTextView.setTextColor(0xFFFFFFFF);
        mSubtitleTextView.setPadding(AndroidUtilities.dp(getContext(), 16), 0, 0, AndroidUtilities.dp(getContext(), 8));
        mSubtitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        if (AndroidUtilities.isLandscape(getContext())) {
            mSubtitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
            mSubtitleTextView.setPadding(AndroidUtilities.dp(getContext(), 16), 0, 0, AndroidUtilities.dp(getContext(), 13));
        }
        mSubtitleTextView.setEllipsize(TextUtils.TruncateAt.END);
        mSubtitleTextView.setGravity(Gravity.START | Gravity.BOTTOM);
        mSubtitleTextView.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.BOTTOM));
        addView(mSubtitleTextView);
    }

    public void openSearchField() {
        EditText mSearchView = new EditText(getContext());
        mSearchView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        mSearchView.setHintTextColor(0x88ffffff);
        mSearchView.setTextColor(0xffffffff);
        mSearchView.setSingleLine(true);
        mSearchView.setBackgroundResource(0);
        mSearchView.setFocusableInTouchMode(true);
        mSearchView.setPadding(AndroidUtilities.dp(getContext(), 56), 0, AndroidUtilities.dp(getContext(), 56), 0);
        mSearchView.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL));
        addView(mSearchView);

        mTitleTextView.setVisibility(INVISIBLE);
        mSearchView.requestFocus();
    }

    public Toolbar setLogo(int image) {
        //this.mToolbarImage = image;
        return this;
    }

    public void hide() {
        hide(true);
    }

    public void show() {
        show(true);
    }

    public void hide(boolean animate) {
        setTranslationY(this.getHeight(), animate);
    }

    public void show(boolean animate) {
        setTranslationY(0, animate);
    }

    private void setTranslationY(int offset, boolean animate) {
        if (animate) {
            animateOffset(offset);
        } else {
            if (mTranslationAnimator != null) {
                mTranslationAnimator.cancel();
            }

            this.setTranslationY(offset);
        }
    }

    private void animateOffset(final int offset) {
        if (mTranslationAnimator == null) {
            mTranslationAnimator = ViewCompat.animate(this);
            mTranslationAnimator.setDuration(600);
            mTranslationAnimator.setInterpolator(new LinearOutSlowInInterpolator());
        } else {
            mTranslationAnimator.cancel();
        }

        mTranslationAnimator.translationY(offset).start();
    }
}