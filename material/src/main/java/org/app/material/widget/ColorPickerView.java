package org.app.material.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.app.material.R;

import java.util.ArrayList;
import java.util.List;

public class ColorPickerView extends View {

    public static final int FLOWER = 1;
    public static final int CIRCLE = 2;

    @IntDef({FLOWER, CIRCLE})
    public @interface TYPE {}

    private int mDensity = 10;
    private int mColorSelection = 0;
    private float mLightness = 1;
    private float mAlpha = 1;
    private static final float STROKE_RATIO = 2f;
    private Paint mColorWheelFill = PaintBuilder.newPaint().color(0).build();
    private Paint mSelectorStroke1 = PaintBuilder.newPaint().color(0xffffffff).build();
    private Paint mSelectorStroke2 = PaintBuilder.newPaint().color(0xff000000).build();
    private Paint mAlphaPatternPaint = PaintBuilder.newPaint().build();
    private Bitmap mColorWheel;
    private Canvas mColorWheelCanvas;
    private Integer mInitialColor;
    private Integer initialColors[] = new Integer[]{null, null, null, null, null};
    private ColorCircle mCurrentColorCircle;
    private LinearLayout mColorPreview;
    private ColorWheelRenderer mRenderer;
	private ArrayList<OnColorSelectedListener> mListeners = new ArrayList<>();

	public ColorPickerView(Context context) {
		super(context);
		init(context, null);
	}

	public ColorPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public ColorPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		final TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerView);
		mDensity = attr.getInt(R.styleable.ColorPickerView_density, 10);
		mInitialColor = attr.getInt(R.styleable.ColorPickerView_initialColor, 0xFFFFFFFF);
		WHEEL_TYPE wheelType = WHEEL_TYPE.indexOf(attr.getInt(R.styleable.ColorPickerView_wheelType, 0));
		ColorWheelRenderer renderer = ColorWheelRendererBuilder.getRenderer(wheelType);
		setRenderer(renderer);
		setDensity(mDensity);
		setInitialColor(mInitialColor);
		attr.recycle();
	}

    public ColorPickerView setDensity(int density) {
        this.mDensity = Math.max(2, density);
        invalidate();
        return this;
    }

    public ColorPickerView setType(@TYPE int type) {
        if (type == CIRCLE) {
            ColorPickerView.ColorWheelRenderer renderer = ColorPickerView.ColorWheelRendererBuilder.getRenderer(ColorPickerView.WHEEL_TYPE.CIRCLE);
            setRenderer(renderer);
        } else if (type == FLOWER) {
            ColorPickerView.ColorWheelRenderer renderer = ColorPickerView.ColorWheelRendererBuilder.getRenderer(WHEEL_TYPE.FLOWER);
            setRenderer(renderer);
        }

        return this;
    }

    public void setInitialColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);

        this.mAlpha = Utils.getAlphaPercent(color);
        this.mLightness = hsv[2];
        this.initialColors[this.mColorSelection] = color;
        this.mInitialColor = color;
        setColorPreviewColor(color);

        if (mRenderer.getColorCircleList() != null) {
            mCurrentColorCircle = findNearestByColor(color);
        }
    }

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		updateColorWheel();
		mCurrentColorCircle = findNearestByColor(mInitialColor);
	}

	private void updateColorWheel() {
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();

		if (height < width) {
            width = height;
        }

		if (width <= 0) {
            return;
        }

		if (mColorWheel == null) {
			mColorWheel = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
			mColorWheelCanvas = new Canvas(mColorWheel);
			mAlphaPatternPaint.setShader(PaintBuilder.createAlphaPatternShader(8));
		}

		drawColorWheel();
		invalidate();
	}

	private void drawColorWheel() {
		mColorWheelCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

		if (mRenderer == null) {
            return;
        }

		float half = mColorWheelCanvas.getWidth() / 2f;
		float strokeWidth = STROKE_RATIO * (1f + ColorWheelRenderer.GAP_PERCENTAGE);
		float maxRadius = half - strokeWidth - half / mDensity;
		float cSize = maxRadius / (mDensity - 1) / 2;

		ColorWheelRenderOption colorWheelRenderOption = mRenderer.getRenderOption();
		colorWheelRenderOption.density = this.mDensity;
		colorWheelRenderOption.maxRadius = maxRadius;
		colorWheelRenderOption.cSize = cSize;
		colorWheelRenderOption.strokeWidth = strokeWidth;
		colorWheelRenderOption.alpha = mAlpha;
		colorWheelRenderOption.lightness = mLightness;
		colorWheelRenderOption.targetCanvas = mColorWheelCanvas;
		mRenderer.initWith(colorWheelRenderOption);
		mRenderer.draw();
	}

	public interface OnColorSelectedListener {
		void onColorSelected(int selectedColor);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int width = 0;

		if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = widthMeasureSpec;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        } else if (widthMode == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }

		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int height = 0;

		if (heightMode == MeasureSpec.UNSPECIFIED) {
            height = widthMeasureSpec;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else if (widthMode == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }

		int squareDimen = width;

		if (height < width) {
            squareDimen = height;
        }

		setMeasuredDimension(squareDimen, squareDimen);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE: {
				mCurrentColorCircle = findNearestByPosition(event.getX(), event.getY());
                mInitialColor = getSelectedColor();
				invalidate();
				break;
			}
			case MotionEvent.ACTION_UP: {
				int selectedColor = getSelectedColor();

				if (mListeners != null) {
					for (OnColorSelectedListener listener : mListeners) {
						try {
							listener.onColorSelected(selectedColor);
						} catch (Exception ignored) {}
					}
				}

				setColorPreviewColor(selectedColor);
				invalidate();
				break;
			}
		}

		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int backgroundColor = 0x00000000;
		canvas.drawColor(backgroundColor);

		if (mColorWheel != null) {
            canvas.drawBitmap(mColorWheel, 0, 0, null);
        }

		if (mCurrentColorCircle != null) {
			float maxRadius = canvas.getWidth() / 2f - STROKE_RATIO * (1f + ColorWheelRenderer.GAP_PERCENTAGE);
			float size = maxRadius / mDensity / 2;
			mColorWheelFill.setColor(Color.HSVToColor(mCurrentColorCircle.getHsvWithLightness(this.mLightness)));
			mColorWheelFill.setAlpha((int) (mAlpha * 0xff));
			canvas.drawCircle(mCurrentColorCircle.getX(), mCurrentColorCircle.getY(), size * STROKE_RATIO, mSelectorStroke1);
			canvas.drawCircle(mCurrentColorCircle.getX(), mCurrentColorCircle.getY(), size * (1 + (STROKE_RATIO - 1) / 2), mSelectorStroke2);
			canvas.drawCircle(mCurrentColorCircle.getX(), mCurrentColorCircle.getY(), size, mAlphaPatternPaint);
			canvas.drawCircle(mCurrentColorCircle.getX(), mCurrentColorCircle.getY(), size, mColorWheelFill);
		}
	}

	private ColorCircle findNearestByPosition(float x, float y) {
		ColorCircle near = null;
		double minDist = Double.MAX_VALUE;

		for (ColorCircle colorCircle : mRenderer.getColorCircleList()) {
			double dist = colorCircle.sqDist(x, y);

			if (minDist > dist) {
				minDist = dist;
				near = colorCircle;
			}
		}

		return near;
	}

	private ColorCircle findNearestByColor(int color) {
		float[] hsv = new float[3];

        Color.colorToHSV(color, hsv);
		ColorCircle near = null;

		double minDiff = Double.MAX_VALUE;
		double x = hsv[1] * Math.cos(hsv[0] * Math.PI / 180);
		double y = hsv[1] * Math.sin(hsv[0] * Math.PI / 180);

		for (ColorCircle colorCircle : mRenderer.getColorCircleList()) {
			float[] hsv1 = colorCircle.getHsv();
			double x1 = hsv1[1] * Math.cos(hsv1[0] * Math.PI / 180);
			double y1 = hsv1[1] * Math.sin(hsv1[0] * Math.PI / 180);
			double dx = x - x1;
			double dy = y - y1;
			double dist = dx * dx + dy * dy;

			if (dist < minDiff) {
				minDiff = dist;
				near = colorCircle;
			}
		}

		return near;
	}

	public int getSelectedColor() {
		int color = 0;

		if (mCurrentColorCircle != null) {
            color = Color.HSVToColor(mCurrentColorCircle.getHsvWithLightness(this.mLightness));
        }

		return Utils.adjustAlpha(this.mAlpha, color);
	}

	public Integer[] getAllColors() {
		return initialColors;
	}

	public void setInitialColors(Integer[] colors, int selectedColor) {
		this.initialColors = colors;
		this.mColorSelection = selectedColor;
		Integer initialColor = this.initialColors[this.mColorSelection];

		if (initialColor == null) {
            initialColor = 0xFFFFFFFF;
        }

		setInitialColor(initialColor);
	}

	public void setLightness(float mLightness) {
		this.mLightness = mLightness;
		this.mInitialColor = Color.HSVToColor(Utils.alphaValueAsInt(this.mAlpha), mCurrentColorCircle.getHsvWithLightness(mLightness));
		updateColorWheel();
		invalidate();
	}

	public void setColor(int color) {
		setInitialColor(color);
		updateColorWheel();
		invalidate();
	}

	public void setAlphaValue(float alpha) {
		this.mAlpha = alpha;
		this.mInitialColor = Color.HSVToColor(Utils.alphaValueAsInt(this.mAlpha), mCurrentColorCircle.getHsvWithLightness(this.mLightness));
        updateColorWheel();
		invalidate();
	}

	public void addOnColorSelectedListener(OnColorSelectedListener listener) {
		this.mListeners.add(listener);
	}

	private void setRenderer(ColorWheelRenderer mRenderer) {
		this.mRenderer = mRenderer;
		invalidate();
	}

	public void setColorPreview(LinearLayout colorPreview, Integer selectedColor) {
		if (colorPreview == null) {
            return;
        }

		this.mColorPreview = colorPreview;

		if (selectedColor == null) {
            selectedColor = 0;
        }

        int children = colorPreview.getChildCount();

        if (children == 0 || colorPreview.getVisibility() != View.VISIBLE) {
            return;
        }

		for (int i = 0; i < children; i++) {
			View childView = colorPreview.getChildAt(i);

			if (!(childView instanceof LinearLayout)) {
                continue;
            }

			LinearLayout childLayout = (LinearLayout) childView;

			if (i == selectedColor) {
				childLayout.setBackgroundColor(0xFFFFFFFF);
			}

			ImageView childImage = (ImageView) childLayout.findViewById(R.id.image_preview);
			childImage.setClickable(true);
			childImage.setTag(i);
			childImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (v == null) {
                        return;
                    }

					Object tag = v.getTag();

                    if (tag == null || !(tag instanceof Integer)) {
                        return;
                    }

					setSelectedColor((int) tag);
				}
			});
		}
	}

	public void setSelectedColor(int previewNumber) {
		if (initialColors == null || initialColors.length < previewNumber) {
            return;
        }

		this.mColorSelection = previewNumber;
		setHighlightedColor(previewNumber);
		Integer color = initialColors[previewNumber];

		if (color == null) {
            return;
        }

		setColor(color);
	}

	private void setHighlightedColor(int previewNumber) {
		int children = mColorPreview.getChildCount();

		if (children == 0 || mColorPreview.getVisibility() != View.VISIBLE) {
            return;
        }

		for (int i = 0; i < children; i++) {
			View childView = mColorPreview.getChildAt(i);

			if (!(childView instanceof LinearLayout)) {
                continue;
            }

            LinearLayout childLayout = (LinearLayout) childView;

			if (i == previewNumber) {
				childLayout.setBackgroundColor(Color.WHITE);
			} else {
				childLayout.setBackgroundColor(Color.TRANSPARENT);
			}
		}
	}

	private void setColorPreviewColor(int newColor) {
		if (mColorPreview == null || initialColors == null || mColorSelection > initialColors.length || initialColors[mColorSelection] == null) {
			return;
        }

		int children = mColorPreview.getChildCount();

		if (children == 0 || mColorPreview.getVisibility() != View.VISIBLE) {
            return;
        }

		View childView = mColorPreview.getChildAt(mColorSelection);

		if (!(childView instanceof LinearLayout)) {
            return;
        }

		LinearLayout childLayout = (LinearLayout) childView;
		ImageView childImage = (ImageView) childLayout.findViewById(R.id.image_preview);
		childImage.setImageDrawable(new CircleColorDrawable(newColor));
	}

	public enum WHEEL_TYPE {
		FLOWER, CIRCLE;

		public static WHEEL_TYPE indexOf(int index) {
			switch (index) {
				case 0:
					return FLOWER;
				case 1:
					return CIRCLE;
			}

			return FLOWER;
		}
	}

    public class CircleColorDrawable extends ColorDrawable {

        private float strokeWidth;
        private Paint strokePaint = PaintBuilder.newPaint().style(Paint.Style.STROKE).stroke(strokeWidth).color(0xFFFFFFFF).build();
        private Paint fillPaint = PaintBuilder.newPaint().style(Paint.Style.FILL).color(0).build();
        private Paint fillBackPaint = PaintBuilder.newPaint().shader(PaintBuilder.createAlphaPatternShader(16)).build();

        public CircleColorDrawable(int color) {
            super(color);
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawColor(0);
            int width = canvas.getWidth();
            float radius = width / 2F;
            strokeWidth = radius / 12F;
            this.strokePaint.setStrokeWidth(strokeWidth);
            this.fillPaint.setColor(getColor());
            canvas.drawCircle(radius, radius, radius - strokeWidth * 1.5F, fillBackPaint);
            canvas.drawCircle(radius, radius, radius - strokeWidth * 1.5F, fillPaint);
            canvas.drawCircle(radius, radius, radius - strokeWidth, strokePaint);
        }

        @Override
        public void setColor(int color) {
            super.setColor(color);
            invalidateSelf();
        }
    }

    public static class ColorWheelRendererBuilder {

        public static ColorWheelRenderer getRenderer(ColorPickerView.WHEEL_TYPE wheelType) {
            if (wheelType == WHEEL_TYPE.CIRCLE) {
                return new SimpleColorWheelRenderer();
            } else if (wheelType == WHEEL_TYPE.FLOWER) {
                return new FlowerColorWheelRenderer();
            }

            throw new IllegalArgumentException("wrong WHEEL_TYPE");
        }

        public static class SimpleColorWheelRenderer extends AbsColorWheelRenderer {

            private Paint selectorFill = PaintBuilder.newPaint().build();
            private float[] hsv = new float[3];

            @Override
            public void draw() {
                final int setSize = mColorCircleList.size();
                int currentCount = 0;
                float half = mColorWheelRenderOption.targetCanvas.getWidth() / 2f;
                int density = mColorWheelRenderOption.density;
                float maxRadius = mColorWheelRenderOption.maxRadius;

                for (int i = 0; i < density; i++) {
                    float p = (float) i / (density - 1);
                    float radius = maxRadius * p;
                    float size = mColorWheelRenderOption.cSize;
                    int total = calcTotalCount(radius, size);

                    for (int j = 0; j < total; j++) {
                        double angle = Math.PI * 2 * j / total + (Math.PI / total) * ((i + 1) % 2);
                        float x = half + (float) (radius * Math.cos(angle));
                        float y = half + (float) (radius * Math.sin(angle));

                        hsv[0] = (float) (angle * 180 / Math.PI);
                        hsv[1] = radius / maxRadius;
                        hsv[2] = mColorWheelRenderOption.lightness;
                        selectorFill.setColor(Color.HSVToColor(hsv));
                        selectorFill.setAlpha(getAlphaValueAsInt());
                        mColorWheelRenderOption.targetCanvas.drawCircle(x, y, size - mColorWheelRenderOption.strokeWidth, selectorFill);

                        if (currentCount >= setSize) {
                            mColorCircleList.add(new ColorCircle(x, y, hsv));
                        } else {
                            mColorCircleList.get(currentCount).set(x, y, hsv);
                        }

                        currentCount++;
                    }
                }
            }
        }

        public static class FlowerColorWheelRenderer extends AbsColorWheelRenderer {

            private Paint selectorFill = PaintBuilder.newPaint().build();
            private float[] hsv = new float[3];

            @Override
            public void draw() {
                final int setSize = mColorCircleList.size();
                int currentCount = 0;
                float half = mColorWheelRenderOption.targetCanvas.getWidth() / 2f;
                int density = mColorWheelRenderOption.density;
                float strokeWidth = mColorWheelRenderOption.strokeWidth;
                float maxRadius = mColorWheelRenderOption.maxRadius;
                float cSize = mColorWheelRenderOption.cSize;

                for (int i = 0; i < density; i++) {
                    float p = (float) i / (density - 1);
                    float jitter = (i - density / 2f) / density;
                    float radius = maxRadius * p;
                    float sizeJitter = 1.2f;
                    float size = Math.max(1.5f + strokeWidth, cSize + (i == 0 ? 0 : cSize * sizeJitter * jitter));
                    int total = Math.min(calcTotalCount(radius, size), density * 2);

                    for (int j = 0; j < total; j++) {
                        double angle = Math.PI * 2 * j / total + (Math.PI / total) * ((i + 1) % 2);
                        float x = half + (float) (radius * Math.cos(angle));
                        float y = half + (float) (radius * Math.sin(angle));
                        hsv[0] = (float) (angle * 180 / Math.PI);
                        hsv[1] = radius / maxRadius;
                        hsv[2] = mColorWheelRenderOption.lightness;
                        selectorFill.setColor(Color.HSVToColor(hsv));
                        selectorFill.setAlpha(getAlphaValueAsInt());
                        mColorWheelRenderOption.targetCanvas.drawCircle(x, y, size - strokeWidth, selectorFill);

                        if (currentCount >= setSize) {
                            mColorCircleList.add(new ColorCircle(x, y, hsv));
                        } else {
                            mColorCircleList.get(currentCount).set(x, y, hsv);
                        }

                        currentCount++;
                    }
                }
            }
        }
    }

    public static class PaintBuilder {

        public static PaintHolder newPaint() {
            return new PaintHolder();
        }

        public static class PaintHolder {

            private Paint paint;

            private PaintHolder() {
                this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            }

            public PaintHolder color(int color) {
                this.paint.setColor(color);
                return this;
            }

            public PaintHolder style(Paint.Style style) {
                this.paint.setStyle(style);
                return this;
            }

            public PaintHolder mode(PorterDuff.Mode mode) {
                this.paint.setXfermode(new PorterDuffXfermode(mode));
                return this;
            }

            public PaintHolder stroke(float width) {
                this.paint.setStrokeWidth(width);
                return this;
            }

            public PaintHolder shader(Shader shader) {
                this.paint.setShader(shader);
                return this;
            }

            public Paint build() {
                return this.paint;
            }
        }

        public static Shader createAlphaPatternShader(int size) {
            size /= 2;
            size = Math.max(8, size * 2);
            return new BitmapShader(createAlphaBackgroundPattern(size), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        }

        private static Bitmap createAlphaBackgroundPattern(int size) {
            Paint alphaPatternPaint = PaintBuilder.newPaint().build();
            Bitmap bm = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bm);
            int s = Math.round(size / 2F);

            for (int i = 0; i < 2; i++)
                for (int j = 0; j < 2; j++) {
                    if ((i + j) % 2 == 0) {
                        alphaPatternPaint.setColor(0xFFFFFFFF);
                    } else {
                        alphaPatternPaint.setColor(0xFFD0D0D0);
                    }

                    c.drawRect(i * s, j * s, (i + 1) * s, (j + 1) * s, alphaPatternPaint);
                }

            return bm;
        }
    }

    public static class Utils {

        public static float getAlphaPercent(int argb) {
            return Color.alpha(argb) / 255F;
        }

        public static int alphaValueAsInt(float alpha) {
            return Math.round(alpha * 255);
        }

        public static int adjustAlpha(float alpha, int color) {
            return alphaValueAsInt(alpha) << 24 | (0x00FFFFFF & color);
        }
    }

    public static abstract class AbsColorWheelRenderer implements ColorWheelRenderer {

        protected ColorWheelRenderOption mColorWheelRenderOption;
        protected List<ColorCircle> mColorCircleList;

        public void initWith(ColorWheelRenderOption colorWheelRenderOption) {
            this.mColorWheelRenderOption = colorWheelRenderOption;
            this.mColorCircleList = new ArrayList<>();
        }

        @Override
        public ColorWheelRenderOption getRenderOption() {

            if (mColorWheelRenderOption == null) {
                mColorWheelRenderOption = new ColorWheelRenderOption();
            }

            return mColorWheelRenderOption;
        }

        public List<ColorCircle> getColorCircleList() {
            return mColorCircleList;
        }

        protected int getAlphaValueAsInt() {
            return Math.round(mColorWheelRenderOption.alpha * 255);
        }

        protected int calcTotalCount(float radius, float size) {
            return Math.max(1, (int) ((1f - GAP_PERCENTAGE) * Math.PI / (Math.asin(size / radius)) + 0.5f));
        }
    }

    public interface ColorWheelRenderer {
        float GAP_PERCENTAGE = 0.025F;

        void draw();

        ColorWheelRenderOption getRenderOption();

        void initWith(ColorWheelRenderOption colorWheelRenderOption);

        List<ColorCircle> getColorCircleList();
    }

    public static class ColorWheelRenderOption {
        public int density;
        public float maxRadius;
        public float cSize, strokeWidth, alpha, lightness;
        public Canvas targetCanvas;
    }

    public static class ColorCircle {

        private float x, y;
        private float[] hsv = new float[3];
        private float[] hsvClone;
        private int color;

        public ColorCircle(float x, float y, float[] hsv) {
            set(x, y, hsv);
        }

        public double sqDist(float x, float y) {
            double dx = this.x - x;
            double dy = this.y - y;
            return dx * dx + dy * dy;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float[] getHsv() {
            return hsv;
        }

        public float[] getHsvWithLightness(float lightness) {
            if (hsvClone == null) {
                hsvClone = hsv.clone();
            }

            hsvClone[0] = hsv[0];
            hsvClone[1] = hsv[1];
            hsvClone[2] = lightness;
            return hsvClone;
        }

        public void set(float x, float y, float[] hsv) {
            this.x = x;
            this.y = y;
            this.hsv[0] = hsv[0];
            this.hsv[1] = hsv[1];
            this.hsv[2] = hsv[2];
            this.color = Color.HSVToColor(this.hsv);
        }

        public int getColor() {
            return color;
        }
    }
}