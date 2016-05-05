package org.app.material.widget;

import android.annotation.TargetApi;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.app.material.R;

import java.util.ArrayList;
import java.util.List;

public class ColorPickerView extends View {

	private static final float STROKE_RATIO = 2f;
	private Bitmap colorWheel;
	private Canvas colorWheelCanvas;
	private int density = 10;
	private float lightness = 1;
	private float alpha = 1;
	private Integer initialColors[] = new Integer[]{null, null, null, null, null};
	private int colorSelection = 0;
	private Integer initialColor;
	private Integer pickerTextColor;
	private Paint colorWheelFill = PaintBuilder.newPaint().color(0).build();
	private Paint selectorStroke1 = PaintBuilder.newPaint().color(0xffffffff).build();
	private Paint selectorStroke2 = PaintBuilder.newPaint().color(0xff000000).build();
	private Paint alphaPatternPaint = PaintBuilder.newPaint().build();
	private ColorCircle currentColorCircle;
	private ArrayList<OnColorSelectedListener> listeners = new ArrayList<OnColorSelectedListener>();
	//private LightnessSlider lightnessSlider;
	//private AlphaSlider alphaSlider;
	private EditText colorEdit;

	private TextWatcher colorTextChange = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                int color = Color.parseColor(s.toString());
                setColor(color, false);
            } catch (Exception ignored) {}
		}

		@Override
		public void afterTextChanged(Editable s) {}
	};

	private LinearLayout colorPreview;
	private ColorWheelRenderer renderer;
	private int alphaSliderViewId, lightnessSliderViewId;

	public ColorPickerView(Context context) {
		super(context);
		initWith(context, null);
	}

	public ColorPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWith(context, attrs);
	}

	public ColorPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initWith(context, attrs);
	}

	@TargetApi(21)
	public ColorPickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initWith(context, attrs);
	}

	private void initWith(Context context, AttributeSet attrs) {
		final TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerPreference);
		density = attr.getInt(R.styleable.ColorPickerPreference_density, 10);
		initialColor = attr.getInt(R.styleable.ColorPickerPreference_initialColor, 0xffffffff);
		pickerTextColor = attr.getInt(R.styleable.ColorPickerPreference_pickerColorEditTextColor, 0xffffffff);
		WHEEL_TYPE wheelType = WHEEL_TYPE.indexOf(attr.getInt(R.styleable.ColorPickerPreference_wheelType, 0));
		ColorWheelRenderer renderer = ColorWheelRendererBuilder.getRenderer(wheelType);
		alphaSliderViewId = attr.getResourceId(R.styleable.ColorPickerPreference_alphaSliderView, 0);
		lightnessSliderViewId = attr.getResourceId(R.styleable.ColorPickerPreference_lightnessSliderView, 0);
		setRenderer(renderer);
		setDensity(density);
		setInitialColor(initialColor, true);
		attr.recycle();
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		//if (alphaSliderViewId != 0) {
          //  setAlphaSlider((AlphaSlider) getRootView().findViewById(alphaSliderViewId));
        //}

		//if (lightnessSliderViewId != 0) {
          //  setLightnessSlider((LightnessSlider) getRootView().findViewById(lightnessSliderViewId));
       // }
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		updateColorWheel();
		currentColorCircle = findNearestByColor(initialColor);
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

		if (colorWheel == null) {
			colorWheel = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
			colorWheelCanvas = new Canvas(colorWheel);
			alphaPatternPaint.setShader(PaintBuilder.createAlphaPatternShader(8));
		}
		drawColorWheel();
		invalidate();
	}

	private void drawColorWheel() {
		colorWheelCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

		if (renderer == null) return;

		float half = colorWheelCanvas.getWidth() / 2f;
		float strokeWidth = STROKE_RATIO * (1f + ColorWheelRenderer.GAP_PERCENTAGE);
		float maxRadius = half - strokeWidth - half / density;
		float cSize = maxRadius / (density - 1) / 2;

		ColorWheelRenderOption colorWheelRenderOption = renderer.getRenderOption();
		colorWheelRenderOption.density = this.density;
		colorWheelRenderOption.maxRadius = maxRadius;
		colorWheelRenderOption.cSize = cSize;
		colorWheelRenderOption.strokeWidth = strokeWidth;
		colorWheelRenderOption.alpha = alpha;
		colorWheelRenderOption.lightness = lightness;
		colorWheelRenderOption.targetCanvas = colorWheelCanvas;

		renderer.initWith(colorWheelRenderOption);
		renderer.draw();
	}

	public interface OnColorSelectedListener {
		void onColorSelected(int selectedColor);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int width = 0;
		if (widthMode == MeasureSpec.UNSPECIFIED)
			width = widthMeasureSpec;
		else if (widthMode == MeasureSpec.AT_MOST)
			width = MeasureSpec.getSize(widthMeasureSpec);
		else if (widthMode == MeasureSpec.EXACTLY)
			width = MeasureSpec.getSize(widthMeasureSpec);

		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int height = 0;
		if (heightMode == MeasureSpec.UNSPECIFIED)
			height = widthMeasureSpec;
		else if (heightMode == MeasureSpec.AT_MOST)
			height = MeasureSpec.getSize(heightMeasureSpec);
		else if (widthMode == MeasureSpec.EXACTLY)
			height = MeasureSpec.getSize(heightMeasureSpec);
		int squareDimen = width;
		if (height < width)
			squareDimen = height;
		setMeasuredDimension(squareDimen, squareDimen);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE: {
				currentColorCircle = findNearestByPosition(event.getX(), event.getY());
				int selectedColor = getSelectedColor();
				initialColor = selectedColor;
				setColorToSliders(selectedColor);
				invalidate();
				break;
			}
			case MotionEvent.ACTION_UP: {
				int selectedColor = getSelectedColor();
				if (listeners != null) {
					for (OnColorSelectedListener listener : listeners) {
						try {
							listener.onColorSelected(selectedColor);
						} catch (Exception e) {
							//Squash individual listener exceptions
						}
					}
				}
				setColorToSliders(selectedColor);
				setColorText(selectedColor);
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
		if (colorWheel != null)
			canvas.drawBitmap(colorWheel, 0, 0, null);
		if (currentColorCircle != null) {
			float maxRadius = canvas.getWidth() / 2f - STROKE_RATIO * (1f + ColorWheelRenderer.GAP_PERCENTAGE);
			float size = maxRadius / density / 2;
			colorWheelFill.setColor(Color.HSVToColor(currentColorCircle.getHsvWithLightness(this.lightness)));
			colorWheelFill.setAlpha((int) (alpha * 0xff));
			canvas.drawCircle(currentColorCircle.getX(), currentColorCircle.getY(), size * STROKE_RATIO, selectorStroke1);
			canvas.drawCircle(currentColorCircle.getX(), currentColorCircle.getY(), size * (1 + (STROKE_RATIO - 1) / 2), selectorStroke2);
			canvas.drawCircle(currentColorCircle.getX(), currentColorCircle.getY(), size, alphaPatternPaint);
			canvas.drawCircle(currentColorCircle.getX(), currentColorCircle.getY(), size, colorWheelFill);
		}
	}

	private ColorCircle findNearestByPosition(float x, float y) {
		ColorCircle near = null;
		double minDist = Double.MAX_VALUE;

		for (ColorCircle colorCircle : renderer.getColorCircleList()) {
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

		for (ColorCircle colorCircle : renderer.getColorCircleList()) {
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

		if (currentColorCircle != null)
			color = Color.HSVToColor(currentColorCircle.getHsvWithLightness(this.lightness));

		return Utils.adjustAlpha(this.alpha, color);
	}

	public Integer[] getAllColors() {
		return initialColors;
	}

	public void setInitialColors(Integer[] colors, int selectedColor) {
		this.initialColors = colors;
		this.colorSelection = selectedColor;
		Integer initialColor = this.initialColors[this.colorSelection];
		if (initialColor == null) initialColor = 0xffffffff;
		setInitialColor(initialColor, true);
	}

	public void setInitialColor(int color, boolean updateText) {
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);

		this.alpha = Utils.getAlphaPercent(color);
		this.lightness = hsv[2];
		this.initialColors[this.colorSelection] = color;
		this.initialColor = color;
		setColorPreviewColor(color);
		setColorToSliders(color);

		if (this.colorEdit != null && updateText) {
            setColorText(color);
        }

		if (renderer.getColorCircleList() != null) {
            currentColorCircle = findNearestByColor(color);
        }
	}

	public void setLightness(float lightness) {
		this.lightness = lightness;
		this.initialColor = Color.HSVToColor(Utils.alphaValueAsInt(this.alpha), currentColorCircle.getHsvWithLightness(lightness));
		if (this.colorEdit != null)
			this.colorEdit.setText("#" + Integer.toHexString(this.initialColor).toUpperCase());
		//if (this.alphaSlider != null && this.initialColor != null)
		//	this.alphaSlider.setColor(this.initialColor);
		updateColorWheel();
		invalidate();
	}

	public void setColor(int color, boolean updateText) {
		setInitialColor(color, updateText);
		updateColorWheel();
		invalidate();
	}

	public void setAlphaValue(float alpha) {
		this.alpha = alpha;
		this.initialColor = Color.HSVToColor(Utils.alphaValueAsInt(this.alpha), currentColorCircle.getHsvWithLightness(this.lightness));
		if (this.colorEdit != null)
			this.colorEdit.setText("#" + Integer.toHexString(this.initialColor).toUpperCase());
		//if (this.lightnessSlider != null && this.initialColor != null)
		//	this.lightnessSlider.setColor(this.initialColor);
		updateColorWheel();
		invalidate();
	}

	public void addOnColorSelectedListener(OnColorSelectedListener listener) {
		this.listeners.add(listener);
	}

	//public void setLightnessSlider(LightnessSlider lightnessSlider) {
		//this.lightnessSlider = lightnessSlider;
	//	if (lightnessSlider != null) {
		//	this.lightnessSlider.setColorPicker(this);
		//	this.lightnessSlider.setColor(getSelectedColor());
	//	}
	//}

	//public void setAlphaSlider(AlphaSlider alphaSlider) {
		//this.alphaSlider = alphaSlider;
	//	if (alphaSlider != null) {
		//	this.alphaSlider.setColorPicker(this);
		//	this.alphaSlider.setColor(getSelectedColor());
	//	}
	//}

	public void setColorEdit(EditText colorEdit) {
		this.colorEdit = colorEdit;
		if (this.colorEdit != null) {
			this.colorEdit.setVisibility(View.VISIBLE);
			this.colorEdit.addTextChangedListener(colorTextChange);
			setColorEditTextColor(pickerTextColor);
		}
	}

    public void setColorEditTextColor(int argb) {
        this.pickerTextColor = argb;
        if (colorEdit != null)
            colorEdit.setTextColor(argb);
    }

	public void setDensity(int density) {
		this.density = Math.max(2, density);
		invalidate();
	}

	public void setRenderer(ColorWheelRenderer renderer) {
		this.renderer = renderer;
		invalidate();
	}

	public void setColorPreview(LinearLayout colorPreview, Integer selectedColor) {
		if (colorPreview == null)
			return;
		this.colorPreview = colorPreview;
		if (selectedColor == null)
			selectedColor = 0;
		int children = colorPreview.getChildCount();
		if (children == 0 || colorPreview.getVisibility() != View.VISIBLE)
			return;

		for (int i = 0; i < children; i++) {
			View childView = colorPreview.getChildAt(i);
			if (!(childView instanceof LinearLayout))
				continue;
			LinearLayout childLayout = (LinearLayout) childView;
			if (i == selectedColor) {
				childLayout.setBackgroundColor(Color.WHITE);
			}
			ImageView childImage = (ImageView) childLayout.findViewById(R.id.image_preview);
			childImage.setClickable(true);
			childImage.setTag(i);
			childImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (v == null)
						return;
					Object tag = v.getTag();
					if (tag == null || !(tag instanceof Integer))
						return;
					setSelectedColor((int) tag);
				}
			});
		}
	}

	public void setSelectedColor(int previewNumber) {
		if (initialColors == null || initialColors.length < previewNumber)
			return;
		this.colorSelection = previewNumber;
		setHighlightedColor(previewNumber);
		Integer color = initialColors[previewNumber];
		if (color == null)
			return;
		setColor(color, true);
	}

	private void setHighlightedColor(int previewNumber) {
		int children = colorPreview.getChildCount();
		if (children == 0 || colorPreview.getVisibility() != View.VISIBLE)
			return;

		for (int i = 0; i < children; i++) {
			View childView = colorPreview.getChildAt(i);
			if (!(childView instanceof LinearLayout))
				continue;
			LinearLayout childLayout = (LinearLayout) childView;
			if (i == previewNumber) {
				childLayout.setBackgroundColor(Color.WHITE);
			} else {
				childLayout.setBackgroundColor(Color.TRANSPARENT);
			}
		}
	}

	private void setColorPreviewColor(int newColor) {
		if (colorPreview == null || initialColors == null || colorSelection > initialColors.length || initialColors[colorSelection] == null)
			return;

		int children = colorPreview.getChildCount();
		if (children == 0 || colorPreview.getVisibility() != View.VISIBLE)
			return;

		View childView = colorPreview.getChildAt(colorSelection);
		if (!(childView instanceof LinearLayout))
			return;
		LinearLayout childLayout = (LinearLayout) childView;
		ImageView childImage = (ImageView) childLayout.findViewById(R.id.image_preview);
		childImage.setImageDrawable(new CircleColorDrawable(newColor));
	}

	private void setColorText(int argb) {
		if (colorEdit == null)
			return;
		colorEdit.setText("#" + Integer.toHexString(argb));
	}

	private void setColorToSliders(int selectedColor) {
		//if (lightnessSlider != null)
		//	lightnessSlider.setColor(selectedColor);
		//if (alphaSlider != null)
		//	alphaSlider.setColor(selectedColor);
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
        private Paint strokePaint = PaintBuilder.newPaint().style(Paint.Style.STROKE).stroke(strokeWidth).color(0xffffffff).build();
        private Paint fillPaint = PaintBuilder.newPaint().style(Paint.Style.FILL).color(0).build();
        private Paint fillBackPaint = PaintBuilder.newPaint().shader(PaintBuilder.createAlphaPatternShader(16)).build();

        public CircleColorDrawable() {
            super();
        }

        public CircleColorDrawable(int color) {
            super(color);
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawColor(0);

            int width = canvas.getWidth();
            float radius = width / 2f;
            strokeWidth = radius / 12f;

            this.strokePaint.setStrokeWidth(strokeWidth);
            this.fillPaint.setColor(getColor());
            canvas.drawCircle(radius, radius, radius - strokeWidth * 1.5f, fillBackPaint);
            canvas.drawCircle(radius, radius, radius - strokeWidth * 1.5f, fillPaint);
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
            switch (wheelType) {
                case CIRCLE:
                    return new SimpleColorWheelRenderer();
                case FLOWER:
                    return new FlowerColorWheelRenderer();
            }
            throw new IllegalArgumentException("wrong WHEEL_TYPE");
        }

        public static class SimpleColorWheelRenderer extends AbsColorWheelRenderer {

            private Paint selectorFill = PaintBuilder.newPaint().build();
            private float[] hsv = new float[3];

            @Override
            public void draw() {
                final int setSize = colorCircleList.size();
                int currentCount = 0;
                float half = colorWheelRenderOption.targetCanvas.getWidth() / 2f;
                int density = colorWheelRenderOption.density;
                float maxRadius = colorWheelRenderOption.maxRadius;

                for (int i = 0; i < density; i++) {
                    float p = (float) i / (density - 1); // 0~1
                    float radius = maxRadius * p;
                    float size = colorWheelRenderOption.cSize;
                    int total = calcTotalCount(radius, size);

                    for (int j = 0; j < total; j++) {
                        double angle = Math.PI * 2 * j / total + (Math.PI / total) * ((i + 1) % 2);
                        float x = half + (float) (radius * Math.cos(angle));
                        float y = half + (float) (radius * Math.sin(angle));
                        hsv[0] = (float) (angle * 180 / Math.PI);
                        hsv[1] = radius / maxRadius;
                        hsv[2] = colorWheelRenderOption.lightness;
                        selectorFill.setColor(Color.HSVToColor(hsv));
                        selectorFill.setAlpha(getAlphaValueAsInt());

                        colorWheelRenderOption.targetCanvas.drawCircle(x, y, size - colorWheelRenderOption.strokeWidth, selectorFill);

                        if (currentCount >= setSize) {
                            colorCircleList.add(new ColorCircle(x, y, hsv));
                        } else {
                            colorCircleList.get(currentCount).set(x, y, hsv);
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
                final int setSize = colorCircleList.size();
                int currentCount = 0;
                float half = colorWheelRenderOption.targetCanvas.getWidth() / 2f;
                int density = colorWheelRenderOption.density;
                float strokeWidth = colorWheelRenderOption.strokeWidth;
                float maxRadius = colorWheelRenderOption.maxRadius;
                float cSize = colorWheelRenderOption.cSize;

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
                        hsv[2] = colorWheelRenderOption.lightness;
                        selectorFill.setColor(Color.HSVToColor(hsv));
                        selectorFill.setAlpha(getAlphaValueAsInt());
                        colorWheelRenderOption.targetCanvas.drawCircle(x, y, size - strokeWidth, selectorFill);

                        if (currentCount >= setSize) {
                            colorCircleList.add(new ColorCircle(x, y, hsv));
                        } else {
                            colorCircleList.get(currentCount).set(x, y, hsv);
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

            public PaintHolder antiAlias(boolean flag) {
                this.paint.setAntiAlias(flag);
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

            public PaintHolder xPerMode(PorterDuff.Mode mode) {
                this.paint.setXfermode(new PorterDuffXfermode(mode));
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
            int s = Math.round(size / 2f);
            for (int i = 0; i < 2; i++)
                for (int j = 0; j < 2; j++) {
                    if ((i + j) % 2 == 0) alphaPatternPaint.setColor(0xffffffff);
                    else alphaPatternPaint.setColor(0xffd0d0d0);
                    c.drawRect(i * s, j * s, (i + 1) * s, (j + 1) * s, alphaPatternPaint);
                }
            return bm;
        }
    }

    public static class Utils {

        public static float getAlphaPercent(int argb) {
            return Color.alpha(argb) / 255f;
        }

        public static int alphaValueAsInt(float alpha) {
            return Math.round(alpha * 255);
        }

        public static int adjustAlpha(float alpha, int color) {
            return alphaValueAsInt(alpha) << 24 | (0x00ffffff & color);
        }

        public static int colorAtLightness(int color, float lightness) {
            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            hsv[2] = lightness;
            return Color.HSVToColor(hsv);
        }

        public static float lightnessOfColor(int color) {
            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            return hsv[2];
        }
    }

    public static abstract class AbsColorWheelRenderer implements ColorWheelRenderer {

        protected ColorWheelRenderOption colorWheelRenderOption;
        protected List<ColorCircle> colorCircleList;

        public void initWith(ColorWheelRenderOption colorWheelRenderOption) {
            this.colorWheelRenderOption = colorWheelRenderOption;
            this.colorCircleList = new ArrayList<>();
        }

        @Override
        public ColorWheelRenderOption getRenderOption() {
            if (colorWheelRenderOption == null) colorWheelRenderOption = new ColorWheelRenderOption();
            return colorWheelRenderOption;
        }

        public List<ColorCircle> getColorCircleList() {
            return colorCircleList;
        }

        protected int getAlphaValueAsInt() {
            return Math.round(colorWheelRenderOption.alpha * 255);
        }

        protected int calcTotalCount(float radius, float size) {
            return Math.max(1, (int) ((1f - GAP_PERCENTAGE) * Math.PI / (Math.asin(size / radius)) + 0.5f));
        }
    }

    public interface ColorWheelRenderer {
        float GAP_PERCENTAGE = 0.025f;

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