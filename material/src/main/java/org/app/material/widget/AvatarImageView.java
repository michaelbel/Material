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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.widget.ImageView;

import org.app.material.R;

public class AvatarImageView extends ImageView {

    private Paint mMaskedPaint;
    private Rect mBounds;
    private RectF mBoundsF;
    private Drawable mMaskDrawable;
    private Bitmap mCacheBitmap;

    private int mCachedWidth;
    private int mCachedHeight;
    private int mImageShape;
    private boolean mCacheValid = false;

    public static final int CUSTOM = 0;
    public static final int CIRCLE = 1;
    public static final int ROUNDED = 2;

    public AvatarImageView(Context context) {
        super(context);

        prepareDrawables(mImageShape);
        setUpPaints();
    }

    private void prepareDrawables(int checkShape) {
        if (checkShape == CIRCLE) {
            mMaskDrawable = getResources().getDrawable(R.drawable.image_circle, null);

            if (mMaskDrawable != null) {
                mMaskDrawable.setCallback(this);
            }
        } else if (checkShape == ROUNDED) {
            mMaskDrawable = getResources().getDrawable(R.drawable.image_rect, null);

            if (mMaskDrawable != null) {
                mMaskDrawable.setCallback(this);
            }
        }
    }

    private void setUpPaints() {
        Paint mBlackPaint = new Paint();
        mBlackPaint.setColor(0xFF000000);
        mMaskedPaint = new Paint();
        mMaskedPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mCacheBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        final boolean changed = super.setFrame(l, t, r, b);
        mBounds = new Rect(0, 0, r - l, b - t);
        mBoundsF = new RectF(mBounds);

        if (mMaskDrawable != null) {
            mMaskDrawable.setBounds(mBounds);
        }

        if (changed) {
            mCacheValid = false;
        }

        return changed;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBounds == null) {
            return;
        }

        int width = mBounds.width();
        int height = mBounds.height();

        if (width == 0 || height == 0) {
            return;
        }

        if (!mCacheValid || width != mCachedWidth || height != mCachedHeight) {
            if (width == mCachedWidth && height == mCachedHeight) {
                mCacheBitmap.eraseColor(0);
            } else {
                mCacheBitmap.recycle();
                mCacheBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                mCachedWidth = width;
                mCachedHeight = height;
            }

            Canvas cacheCanvas = new Canvas(mCacheBitmap);

            if (mMaskDrawable != null) {
                int sc = cacheCanvas.save();
                mMaskDrawable.draw(cacheCanvas);
                cacheCanvas.saveLayer(mBoundsF, mMaskedPaint, Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG);
                super.onDraw(cacheCanvas);
                cacheCanvas.restoreToCount(sc);
            } else {
                super.onDraw(cacheCanvas);
            }
        }

        canvas.drawBitmap(mCacheBitmap, mBounds.left, mBounds.top, null);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        if (mMaskDrawable != null && mMaskDrawable.isStateful()) {
            mMaskDrawable.setState(getDrawableState());
        }

        if (isDuplicateParentStateEnabled()) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public void invalidateDrawable(Drawable who) {
        if (who == mMaskDrawable) {
            invalidate();
        } else {
            super.invalidateDrawable(who);
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return who == mMaskDrawable || super.verifyDrawable(who);
    }

    public void setShapeDrawable(Drawable drawable) {
        this.mMaskDrawable = drawable;

        if (mMaskDrawable != null) {
            mMaskDrawable.setCallback(this);
        }

        setUpPaints();
    }

    public void setShapeDrawable(int drawable) {
        if (drawable != CUSTOM) {
            mImageShape = drawable;
            prepareDrawables(mImageShape);
            setUpPaints();
        }
    }
}