package org.app.material;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public final class RippleManager implements View.OnClickListener{

	private View.OnClickListener mClickListener;
    private boolean mClickScheduled = false;
		
	public RippleManager(){}

    /**
     * Should be called in the construction method of view to create a RippleDrawable.
     * @param v
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
	public void onCreate(View v, Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
		if(v.isInEditMode())
			return;

		RippleDrawableW drawable = null;

		if(drawable != null)
            setBackground(v, drawable);
	}

	public static void setBackground(View v, Drawable drawable){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
			v.setBackground(drawable);
		else
			v.setBackgroundDrawable(drawable);
	}

    private Drawable getBackground(View v){
        Drawable background = v.getBackground();
        if(background == null)
            return null;

        if(background instanceof RippleDrawableW)
            return ((RippleDrawableW)background).getBackgroundDrawable();

        return background;
    }
		
	public void setOnClickListener(View.OnClickListener l) {
		mClickListener = l;
	}

	public boolean onTouchEvent(View v, MotionEvent event){
		Drawable background = v.getBackground();
        return background != null && background instanceof RippleDrawableW && ((RippleDrawableW) background).onTouch(v, event);
    }
	
	@Override
	public void onClick(View v) {
		Drawable background = v.getBackground();
		long delay = 0;

		if(background != null) {
			if (background instanceof RippleDrawableW)
				delay = ((RippleDrawableW) background).getClickDelayTime();
		}
			
		if(delay > 0 && v.getHandler() != null && !mClickScheduled) {
            mClickScheduled = true;
            v.getHandler().postDelayed(new ClickRunnable(v), delay);
        }
		else
			dispatchClickEvent(v);
	}

	private void dispatchClickEvent(View v){
		if(mClickListener != null)
			mClickListener.onClick(v);
	}

    /**
     * Cancel the ripple effect of this view and all of it's children.
     * @param v
     */
	public static void cancelRipple(View v){
		Drawable background = v.getBackground();
		if(background instanceof RippleDrawableW)
			((RippleDrawableW)background).cancel();
		
		if(v instanceof ViewGroup){
			ViewGroup vg = (ViewGroup)v;
			for(int i = 0, count = vg.getChildCount(); i < count; i++)
				RippleManager.cancelRipple(vg.getChildAt(i));
		}
	}

	class ClickRunnable implements Runnable{
		View mView;

		public ClickRunnable(View v){
			mView = v;
		}

		@Override
		public void run() {
			mClickScheduled = false;
			dispatchClickEvent(mView);
		}
	}
	
}
