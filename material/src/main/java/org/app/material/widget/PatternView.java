package org.app.material.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import org.app.material.AndroidUtilities;

import java.util.ArrayList;
import java.util.List;

public class PatternView extends View {

    private static final int LOCK_SIZE = 3;
    private static final int MATRIX_SIZE = LOCK_SIZE * LOCK_SIZE;
    private static final int MILLIS_PER_CIRCLE_ANIMATING = 700;
    private static final float DRAG_THRESHHOLD = 0.0f;
    private static final boolean PROFILE_DRAWING = false;

    private final int mDotSize;
    private final int mDotSizeActivated;
    private final int mPathWidth;
    private float mHitFactor = 0.6f;
    private boolean mInputEnabled = true;

    private final CellState[][] mCellStates;
    private final Path mCurrentPath = new Path();
    private final Rect mInvalidate = new Rect();
    private final Rect mTmpInvalidateRect = new Rect();
    private Paint mPaint = new Paint();
    private Paint mPathPaint = new Paint();
    private OnPatternListener mOnPatternListener;
    private ArrayList<Cell> mPattern = new ArrayList<>(MATRIX_SIZE);
    private DisplayMode mPatternDisplayMode = DisplayMode.Correct;
    private Interpolator mFastOutSlowInInterpolator;
    private Interpolator mLinearOutSlowInInterpolator;










    public static class Cell implements Parcelable {

        public final int row, column;
        static Cell[][] sCells = new Cell[LOCK_SIZE][LOCK_SIZE];

        static {
            for (int i = 0; i < LOCK_SIZE; i++) {
                for (int j = 0; j < LOCK_SIZE; j++) {
                    sCells[i][j] = new Cell(i, j);
                }
            }
        }

        private Cell(int row, int column) {
            checkRange(row, column);
            this.row = row;
            this.column = column;
        }

        public int getId() {
            return row * LOCK_SIZE + column;
        }

        public static synchronized Cell of(int row, int column) {
            checkRange(row, column);
            return sCells[row][column];
        }

        public static synchronized Cell of(int id) {
            return of(id / LOCK_SIZE, id % LOCK_SIZE);
        }

        private static void checkRange(int row, int column) {
            if (row < 0 || row > LOCK_SIZE - 1) {
                throw new IllegalArgumentException("row must be in range 0-" + (LOCK_SIZE - 1));
            }

            if (column < 0 || column > LOCK_SIZE - 1) {
                throw new IllegalArgumentException("column must be in range 0-" + (LOCK_SIZE - 1));
            }
        }

        @Override
        public String toString() {
            return "(ROW=" + row + ",COL=" + column + ")";
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof Cell) {
                return column == ((Cell) object).column && row == ((Cell) object).row;
            }

            return super.equals(object);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(column);
            dest.writeInt(row);
        }

        public static final Creator<Cell> CREATOR = new Creator<Cell>() {

            public Cell createFromParcel(Parcel in) {
                return new Cell(in);
            }

            public Cell[] newArray(int size) {
                return new Cell[size];
            }
        };

        private Cell(Parcel in) {
            column = in.readInt();
            row = in.readInt();
        }
    }

    public enum DisplayMode {
        Correct,
        Animate,
        Wrong
    }

    public static abstract class OnPatternListener {

        public void onPatternStart() {}

        public void onPatternCleared() {}

        public void onPatternCellAdded(List<Cell> pattern, String SimplePattern) {}

        public void onPatternDetected(List<Cell> pattern, String SimplePattern) {}
    }


    private boolean mDrawingProfilingStarted = false;
    private boolean[][] mPatternDrawLookup = new boolean[LOCK_SIZE][LOCK_SIZE];
    private float mInProgressX = -1;
    private float mInProgressY = -1;
    private long mAnimatingPeriodStart;

    private boolean mInStealthMode = false;
    private boolean mEnableHapticFeedback = true;
    private boolean mPatternInProgress = false;
    private float mSquareWidth;
    private float mSquareHeight;
    private int mRegularColor;
    private int mErrorColor;
    private int mSuccessColor;

    public static class CellState {
        public float scale = 1.0f;
        public float translateY = 0.0f;
        public float alpha = 1.0f;
        public float size;
        public float lineEndX = Float.MIN_VALUE;
        public float lineEndY = Float.MIN_VALUE;
        public ValueAnimator lineAnimator;
    }

    public PatternView(Context context, AttributeSet attrs) {
        super(context, attrs);

        AndroidUtilities.bind(context);

        setClickable(true);
        mPathPaint.setAntiAlias(true);
        mPathPaint.setDither(true);

        mRegularColor = 0xFFFFFFFF;
        mErrorColor = 0xFFFF5252;
        mSuccessColor = 0xFF388E3C;

        mPathPaint.setColor(mRegularColor);
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setStrokeJoin(Paint.Join.ROUND);
        mPathPaint.setStrokeCap(Paint.Cap.ROUND);

        mPathWidth = AndroidUtilities.dp(3);
        mPathPaint.setStrokeWidth(mPathWidth);
        mDotSize = AndroidUtilities.dp(13);
        mDotSizeActivated = AndroidUtilities.dp(16);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        mCellStates = new CellState[LOCK_SIZE][LOCK_SIZE];

        for (int i = 0; i < LOCK_SIZE; i++) {
            for (int j = 0; j < LOCK_SIZE; j++) {
                mCellStates[i][j] = new CellState();
                mCellStates[i][j].size = mDotSize;
            }
        }

        if (Build.VERSION.SDK_INT >= 21 && !isInEditMode()) {
            mFastOutSlowInInterpolator = AnimationUtils.loadInterpolator(context, android.R.interpolator.fast_out_slow_in);
            mLinearOutSlowInInterpolator = AnimationUtils.loadInterpolator(context, android.R.interpolator.linear_out_slow_in);
        }
    }

    public CellState[][] getCellStates() {
        return mCellStates;
    }

    public boolean isInStealthMode() {
        return mInStealthMode;
    }

    public boolean isTactileFeedbackEnabled() {
        return mEnableHapticFeedback;
    }

    public void setInStealthMode(boolean inStealthMode) {
        mInStealthMode = inStealthMode;
    }

    public void setTactileFeedbackEnabled(boolean tactileFeedbackEnabled) {
        mEnableHapticFeedback = tactileFeedbackEnabled;
    }

    public void setOnPatternListener(OnPatternListener onPatternListener) {
        mOnPatternListener = onPatternListener;
    }

    @SuppressWarnings("unchecked")
    public List<Cell> getPattern() {
        return (List<Cell>) mPattern.clone();
    }

    public void setPattern(DisplayMode displayMode, List<Cell> pattern) {
        mPattern.clear();
        mPattern.addAll(pattern);
        clearPatternDrawLookup();

        for (Cell cell : pattern) {
            mPatternDrawLookup[cell.row][cell.column] = true;
        }

        setDisplayMode(displayMode);
    }

    public DisplayMode getDisplayMode() {
        return mPatternDisplayMode;
    }

    public void setDisplayMode(DisplayMode displayMode) {
        mPatternDisplayMode = displayMode;

        if (displayMode == DisplayMode.Animate) {
            if (mPattern.size() == 0) {
                throw new IllegalStateException("you must have a pattern to animate if you want to set the display mode to animate");
            }

            mAnimatingPeriodStart = SystemClock.elapsedRealtime();
            final Cell first = mPattern.get(0);
            mInProgressX = getCenterXForColumn(first.column);
            mInProgressY = getCenterYForRow(first.row);
            clearPatternDrawLookup();
        }
        invalidate();
    }

    private String getSimplePattern(List<Cell> pattern) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Cell cell : pattern) {
            stringBuilder.append(getSipmleCellPosition(cell));
        }

        return stringBuilder.toString();
    }

    private String getSipmleCellPosition(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.row) {
            case 0:
                switch (cell.column) {
                    case 0:
                        return "1";
                    case 1:
                        return "2";
                    case 2:
                        return "3";
                }
                break;
            case 1:
                switch (cell.column) {
                    case 0:
                        return "4";
                    case 1:
                        return "5";
                    case 2:
                        return "6";
                }
                break;
            case 2:
                switch (cell.column) {
                    case 0:
                        return "7";
                    case 1:
                        return "8";
                    case 2:
                        return "9";
                }
                break;

        }
        return "";
    }

    private void notifyCellAdded() {
        if (mOnPatternListener != null) {
            mOnPatternListener.onPatternCellAdded(mPattern, getSimplePattern(mPattern));
        }
    }

    private void notifyPatternStarted() {
        if (mOnPatternListener != null) {
            mOnPatternListener.onPatternStart();
        }
    }

    private void notifyPatternDetected() {
        if (mOnPatternListener != null) {
            mOnPatternListener.onPatternDetected(mPattern, getSimplePattern(mPattern));
        }
    }

    private void notifyPatternCleared() {
        if (mOnPatternListener != null) {
            mOnPatternListener.onPatternCleared();
        }
    }

    public void clearPattern() {
        resetPattern();
    }

    private void resetPattern() {
        mPattern.clear();
        clearPatternDrawLookup();
        mPatternDisplayMode = DisplayMode.Correct;
        invalidate();
    }

    private void clearPatternDrawLookup() {
        for (int i = 0; i < LOCK_SIZE; i++) {
            for (int j = 0; j < LOCK_SIZE; j++) {
                mPatternDrawLookup[i][j] = false;
            }
        }
    }

    public void setEnableInput(boolean state) {
        mInputEnabled = state;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        final int width = w - getPaddingLeft() - getPaddingRight();
        mSquareWidth = width / (float) LOCK_SIZE;
        final int height = h - getPaddingTop() - getPaddingBottom();
        mSquareHeight = height / (float) LOCK_SIZE;
    }

    private int resolveMeasured(int measureSpec, int desired) {
        int result;
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.UNSPECIFIED:
                result = desired;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.max(specSize, desired);
                break;
            case MeasureSpec.EXACTLY:
            default:
                result = specSize;
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int minimumWidth = getSuggestedMinimumWidth();
        final int minimumHeight = getSuggestedMinimumHeight();
        int viewWidth = resolveMeasured(widthMeasureSpec, minimumWidth);
        int viewHeight = resolveMeasured(heightMeasureSpec, minimumHeight);
        viewWidth = viewHeight = Math.min(viewWidth, viewHeight);
        setMeasuredDimension(viewWidth, viewHeight);
    }

    private Cell detectAndAddHit(float x, float y) {
        final Cell cell = checkForNewHit(x, y);

        if (cell != null) {
            Cell fillInGapCell = null;
            final ArrayList<Cell> pattern = mPattern;

            if (!pattern.isEmpty()) {
                final Cell lastCell = pattern.get(pattern.size() - 1);
                int dRow = cell.row - lastCell.row;
                int dColumn = cell.column - lastCell.column;
                int fillInRow = lastCell.row;
                int fillInColumn = lastCell.column;

                if (Math.abs(dRow) == 2 && Math.abs(dColumn) != 1) {
                    fillInRow = lastCell.row + ((dRow > 0) ? 1 : -1);
                }

                if (Math.abs(dColumn) == 2 && Math.abs(dRow) != 1) {
                    fillInColumn = lastCell.column + ((dColumn > 0) ? 1 : -1);
                }

                fillInGapCell = Cell.of(fillInRow, fillInColumn);
            }

            if (fillInGapCell != null && !mPatternDrawLookup[fillInGapCell.row][fillInGapCell.column]) {
                addCellToPattern(fillInGapCell);
            }

            addCellToPattern(cell);

            if (mEnableHapticFeedback) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR)
                    performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING | HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
            }

            return cell;
        }

        return null;
    }

    private void addCellToPattern(Cell newCell) {
        mPatternDrawLookup[newCell.row][newCell.column] = true;
        mPattern.add(newCell);

        if (!mInStealthMode) {
            startCellActivatedAnimation(newCell);
        }

        notifyCellAdded();
    }

    private void startCellActivatedAnimation(Cell cell) {
        final CellState cellState = mCellStates[cell.row][cell.column];

        startSizeAnimation(mDotSize, mDotSizeActivated, 96, mLinearOutSlowInInterpolator, cellState, new Runnable() {
            @Override
            public void run() {
                startSizeAnimation(mDotSizeActivated, mDotSize, 192, mFastOutSlowInInterpolator, cellState, null);
            }
        });

        startLineEndAnimation(cellState, mInProgressX, mInProgressY, getCenterXForColumn(cell.column), getCenterYForRow(cell.row));
    }

    private void startLineEndAnimation(final CellState state, final float startX, final float startY, final float targetX, final float targetY) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return;
        }

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float t = (Float) animation.getAnimatedValue();
                state.lineEndX = (1 - t) * startX + t * targetX;
                state.lineEndY = (1 - t) * startY + t * targetY;
                invalidate();
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                state.lineAnimator = null;
            }

        });

        valueAnimator.setInterpolator(mFastOutSlowInInterpolator);
        valueAnimator.setDuration(150);
        valueAnimator.start();
        state.lineAnimator = valueAnimator;
    }

    private void startSizeAnimation(float start, float end, long duration, Interpolator interpolator, final CellState state, final Runnable endRunnable) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            FloatAnimator animator = new FloatAnimator(start, end, duration);

            animator.addEventListener(new FloatAnimator.SimpleEventListener() {
                @Override
                public void onAnimationUpdate(FloatAnimator animator) {
                    state.size = animator.getAnimatedValue();
                    invalidate();
                }

                @Override
                public void onAnimationEnd(FloatAnimator animator) {
                    if (endRunnable != null)
                        endRunnable.run();
                }

            });

            animator.start();
        } else {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, end);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    state.size = (Float) animation.getAnimatedValue();
                    invalidate();
                }
            });

            if (endRunnable != null) {
                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        endRunnable.run();
                    }
                });
            }

            valueAnimator.setInterpolator(interpolator);
            valueAnimator.setDuration(duration);
            valueAnimator.start();
        }
    }

    private Cell checkForNewHit(float x, float y) {
        final int rowHit = getRowHit(y);

        if (rowHit < 0) {
            return null;
        }

        final int columnHit = getColumnHit(x);

        if (columnHit < 0) {
            return null;
        }

        if (mPatternDrawLookup[rowHit][columnHit]) {
            return null;
        }

        return Cell.of(rowHit, columnHit);
    }

    private int getRowHit(float y) {
        final float squareHeight = mSquareHeight;
        float hitSize = squareHeight * mHitFactor;
        float offset = getPaddingTop() + (squareHeight - hitSize) / 2f;

        for (int i = 0; i < LOCK_SIZE; i++) {
            final float hitTop = offset + squareHeight * i;

            if (y >= hitTop && y <= hitTop + hitSize) {
                return i;
            }
        }

        return -1;
    }

    private int getColumnHit(float x) {
        final float squareWidth = mSquareWidth;
        float hitSize = squareWidth * mHitFactor;
        float offset = getPaddingLeft() + (squareWidth - hitSize) / 2f;

        for (int i = 0; i < LOCK_SIZE; i++) {
            final float hitLeft = offset + squareWidth * i;

            if (x >= hitLeft && x <= hitLeft + hitSize) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public boolean onHoverEvent(MotionEvent event) {
        if (((AccessibilityManager) getContext().getSystemService(Context.ACCESSIBILITY_SERVICE)).isTouchExplorationEnabled()) {
            final int action = event.getAction();

            switch (action) {
                case MotionEvent.ACTION_HOVER_ENTER:
                    event.setAction(MotionEvent.ACTION_DOWN);
                    break;
                case MotionEvent.ACTION_HOVER_MOVE:
                    event.setAction(MotionEvent.ACTION_MOVE);
                    break;
                case MotionEvent.ACTION_HOVER_EXIT:
                    event.setAction(MotionEvent.ACTION_UP);
                    break;
            }

            onTouchEvent(event);
            event.setAction(action);
        }

        return super.onHoverEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mInputEnabled || !isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handleActionDown(event);
                return true;
            case MotionEvent.ACTION_UP:
                handleActionUp(event);
                return true;
            case MotionEvent.ACTION_MOVE:
                handleActionMove(event);
                return true;
            case MotionEvent.ACTION_CANCEL:
                mPatternInProgress = false;
                resetPattern();
                notifyPatternCleared();

                if (PROFILE_DRAWING) {
                    if (mDrawingProfilingStarted) {
                        Debug.stopMethodTracing();
                        mDrawingProfilingStarted = false;
                    }
                }

                return true;
        }

        return false;
    }

    private void handleActionMove(MotionEvent event) {
        final float radius = mPathWidth;
        final int historySize = event.getHistorySize();
        mTmpInvalidateRect.setEmpty();
        boolean invalidateNow = false;

        for (int i = 0; i < historySize + 1; i++) {
            final float x = i < historySize ? event.getHistoricalX(i) : event.getX();
            final float y = i < historySize ? event.getHistoricalY(i) : event.getY();
            Cell hitCell = detectAndAddHit(x, y);
            final int patternSize = mPattern.size();

            if (hitCell != null && patternSize == 1) {
                mPatternInProgress = true;
                notifyPatternStarted();
            }

            final float dx = Math.abs(x - mInProgressX);
            final float dy = Math.abs(y - mInProgressY);

            if (dx > DRAG_THRESHHOLD || dy > DRAG_THRESHHOLD) {
                invalidateNow = true;
            }

            if (mPatternInProgress && patternSize > 0) {
                final ArrayList<Cell> pattern = mPattern;
                final Cell lastCell = pattern.get(patternSize - 1);
                float lastCellCenterX = getCenterXForColumn(lastCell.column);
                float lastCellCenterY = getCenterYForRow(lastCell.row);
                float left = Math.min(lastCellCenterX, x) - radius;
                float right = Math.max(lastCellCenterX, x) + radius;
                float top = Math.min(lastCellCenterY, y) - radius;
                float bottom = Math.max(lastCellCenterY, y) + radius;

                if (hitCell != null) {
                    final float width = mSquareWidth * 0.5f;
                    final float height = mSquareHeight * 0.5f;
                    final float hitCellCenterX = getCenterXForColumn(hitCell.column);
                    final float hitCellCenterY = getCenterYForRow(hitCell.row);

                    left = Math.min(hitCellCenterX - width, left);
                    right = Math.max(hitCellCenterX + width, right);
                    top = Math.min(hitCellCenterY - height, top);
                    bottom = Math.max(hitCellCenterY + height, bottom);
                }

                mTmpInvalidateRect.union(Math.round(left), Math.round(top), Math.round(right), Math.round(bottom));
            }
        }
        mInProgressX = event.getX();
        mInProgressY = event.getY();

        if (invalidateNow) {
            mInvalidate.union(mTmpInvalidateRect);
            invalidate(mInvalidate);
            mInvalidate.set(mTmpInvalidateRect);
        }
    }

    private void sendAccessEvent(int resId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setContentDescription(getContext().getString(resId));
            sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED);
            setContentDescription(null);
        } else {
            announceForAccessibility(getContext().getString(resId));
        }
    }

    private void handleActionUp(MotionEvent event) {
        if (!mPattern.isEmpty()) {
            mPatternInProgress = false;
            cancelLineAnimations();
            notifyPatternDetected();
            invalidate();
        }

        if (PROFILE_DRAWING) {
            if (mDrawingProfilingStarted) {
                Debug.stopMethodTracing();
                mDrawingProfilingStarted = false;
            }
        }
    }

    private void cancelLineAnimations() {
        for (int i = 0; i < LOCK_SIZE; i++) {
            for (int j = 0; j < LOCK_SIZE; j++) {
                CellState state = mCellStates[i][j];

                if (state.lineAnimator != null) {
                    state.lineAnimator.cancel();
                    state.lineEndX = Float.MIN_VALUE;
                    state.lineEndY = Float.MIN_VALUE;
                }
            }
        }
    }

    private void handleActionDown(MotionEvent event) {
        resetPattern();

        final float x = event.getX();
        final float y = event.getY();
        final Cell hitCell = detectAndAddHit(x, y);

        if (hitCell != null) {
            mPatternInProgress = true;
            mPatternDisplayMode = DisplayMode.Correct;
            notifyPatternStarted();
        } else {
            mPatternInProgress = false;
            notifyPatternCleared();
        }

        if (hitCell != null) {
            final float startX = getCenterXForColumn(hitCell.column);
            final float startY = getCenterYForRow(hitCell.row);
            final float widthOffset = mSquareWidth / 2f;
            final float heightOffset = mSquareHeight / 2f;

            invalidate((int) (startX - widthOffset), (int) (startY - heightOffset), (int) (startX + widthOffset), (int) (startY + heightOffset));
        }
        mInProgressX = x;
        mInProgressY = y;

        if (PROFILE_DRAWING) {
            if (!mDrawingProfilingStarted) {
                Debug.startMethodTracing("LockPatternDrawing");
                mDrawingProfilingStarted = true;
            }
        }
    }

    private float getCenterXForColumn(int column) {
        return getPaddingLeft() + column * mSquareWidth + mSquareWidth / 2f;
    }

    private float getCenterYForRow(int row) {
        return getPaddingTop() + row * mSquareHeight + mSquareHeight / 2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final ArrayList<Cell> pattern = mPattern;
        final int count = pattern.size();
        final boolean[][] drawLookup = mPatternDrawLookup;

        if (mPatternDisplayMode == DisplayMode.Animate) {
            final int oneCycle = (count + 1) * MILLIS_PER_CIRCLE_ANIMATING;
            final int spotInCycle = (int) (SystemClock.elapsedRealtime() - mAnimatingPeriodStart) % oneCycle;
            final int numCircles = spotInCycle / MILLIS_PER_CIRCLE_ANIMATING;

            clearPatternDrawLookup();

            for (int i = 0; i < numCircles; i++) {
                final Cell cell = pattern.get(i);
                drawLookup[cell.row][cell.column] = true;
            }

            final boolean needToUpdateInProgressPoint = numCircles > 0 && numCircles < count;

            if (needToUpdateInProgressPoint) {
                final float percentageOfNextCircle = ((float) (spotInCycle % MILLIS_PER_CIRCLE_ANIMATING)) / MILLIS_PER_CIRCLE_ANIMATING;

                final Cell currentCell = pattern.get(numCircles - 1);
                final float centerX = getCenterXForColumn(currentCell.column);
                final float centerY = getCenterYForRow(currentCell.row);

                final Cell nextCell = pattern.get(numCircles);
                final float dx = percentageOfNextCircle * (getCenterXForColumn(nextCell.column) - centerX);
                final float dy = percentageOfNextCircle * (getCenterYForRow(nextCell.row) - centerY);
                mInProgressX = centerX + dx;
                mInProgressY = centerY + dy;
            }

            invalidate();
        }

        final Path currentPath = mCurrentPath;
        currentPath.rewind();

        for (int i = 0; i < LOCK_SIZE; i++) {
            float centerY = getCenterYForRow(i);

            for (int j = 0; j < LOCK_SIZE; j++) {
                CellState cellState = mCellStates[i][j];
                float centerX = getCenterXForColumn(j);
                float size = cellState.size * cellState.scale;
                float translationY = cellState.translateY;
                drawCircle(canvas, (int) centerX, (int) centerY + translationY, size, drawLookup[i][j], cellState.alpha);
            }
        }

        final boolean drawPath = !mInStealthMode;

        if (drawPath) {
            mPathPaint.setColor(getCurrentColor(true));

            boolean anyCircles = false;
            float lastX = 0f;
            float lastY = 0f;
            for (int i = 0; i < count; i++) {
                Cell cell = pattern.get(i);

                if (!drawLookup[cell.row][cell.column]) {
                    break;
                }

                anyCircles = true;

                float centerX = getCenterXForColumn(cell.column);
                float centerY = getCenterYForRow(cell.row);

                if (i != 0) {
                    CellState state = mCellStates[cell.row][cell.column];
                    currentPath.rewind();
                    currentPath.moveTo(lastX, lastY);

                    if (state.lineEndX != Float.MIN_VALUE && state.lineEndY != Float.MIN_VALUE) {
                        currentPath.lineTo(state.lineEndX, state.lineEndY);
                    } else {
                        currentPath.lineTo(centerX, centerY);
                    }

                    canvas.drawPath(currentPath, mPathPaint);
                }

                lastX = centerX;
                lastY = centerY;
            }

            if ((mPatternInProgress || mPatternDisplayMode == DisplayMode.Animate) && anyCircles) {
                currentPath.rewind();
                currentPath.moveTo(lastX, lastY);
                currentPath.lineTo(mInProgressX, mInProgressY);
                mPathPaint.setAlpha((int) (calculateLastSegmentAlpha(mInProgressX, mInProgressY, lastX, lastY) * 255f));
                canvas.drawPath(currentPath, mPathPaint);
            }
        }
    }

    private float calculateLastSegmentAlpha(float x, float y, float lastX, float lastY) {
        float diffX = x - lastX;
        float diffY = y - lastY;
        float dist = (float) Math.sqrt(diffX * diffX + diffY * diffY);
        float frac = dist / mSquareWidth;
        return Math.min(1f, Math.max(0f, (frac - 0.3f) * 4f));
    }

    private int getCurrentColor(boolean partOfPattern) {
        if (!partOfPattern || mInStealthMode || mPatternInProgress) {
            return mRegularColor;
        } else if (mPatternDisplayMode == DisplayMode.Wrong) {
            return mErrorColor;
        } else if (mPatternDisplayMode == DisplayMode.Correct || mPatternDisplayMode == DisplayMode.Animate) {
            return mSuccessColor;
        } else {
            throw new IllegalStateException("unknown display mode " + mPatternDisplayMode);
        }
    }

    private void drawCircle(Canvas canvas, float centerX, float centerY, float size, boolean partOfPattern, float alpha) {
        mPaint.setColor(getCurrentColor(partOfPattern));
        mPaint.setAlpha((int) (alpha * 255));
        canvas.drawCircle(centerX, centerY, size / 2, mPaint);
    }

    private static class SavedState extends BaseSavedState {

        private final String mSerializedPattern;
        private final int mDisplayMode;
        private final boolean mInputEnabled;
        private final boolean mInStealthMode;
        private final boolean mTactileFeedbackEnabled;

        private SavedState(Parcelable superState, String serializedPattern, int displayMode, boolean inputEnabled, boolean inStealthMode, boolean tactileFeedbackEnabled) {
            super(superState);
            mSerializedPattern = serializedPattern;
            mDisplayMode = displayMode;
            mInputEnabled = inputEnabled;
            mInStealthMode = inStealthMode;
            mTactileFeedbackEnabled = tactileFeedbackEnabled;
        }

        private SavedState(Parcel in) {
            super(in);
            mSerializedPattern = in.readString();
            mDisplayMode = in.readInt();
            mInputEnabled = (Boolean) in.readValue(null);
            mInStealthMode = (Boolean) in.readValue(null);
            mTactileFeedbackEnabled = (Boolean) in.readValue(null);
        }

        public String getSerializedPattern() {
            return mSerializedPattern;
        }

        public int getDisplayMode() {
            return mDisplayMode;
        }

        public boolean isInputEnabled() {
            return mInputEnabled;
        }

        public boolean isInStealthMode() {
            return mInStealthMode;
        }

        public boolean isTactileFeedbackEnabled() {
            return mTactileFeedbackEnabled;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(mSerializedPattern);
            dest.writeInt(mDisplayMode);
            dest.writeValue(mInputEnabled);
            dest.writeValue(mInStealthMode);
            dest.writeValue(mTactileFeedbackEnabled);
        }

        @SuppressWarnings("unused")
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public static class FloatAnimator {

        public interface EventListener {

            void onAnimationStart(@NonNull FloatAnimator animator);

            void onAnimationUpdate(@NonNull FloatAnimator animator);

            void onAnimationCancel(@NonNull FloatAnimator animator);

            void onAnimationEnd(@NonNull FloatAnimator animator);
        }

        public static class SimpleEventListener implements EventListener {

            @Override
            public void onAnimationStart(@NonNull FloatAnimator animator) {}

            @Override
            public void onAnimationUpdate(@NonNull FloatAnimator animator) {}

            @Override
            public void onAnimationCancel(@NonNull FloatAnimator animator) {}

            @Override
            public void onAnimationEnd(@NonNull FloatAnimator animator) {}
        }

        private static final long ANIMATION_DELAY = 1;
        private final float mStartValue, mEndValue;
        private final long mDuration;
        private float mAnimatedValue;
        private List<EventListener> mEventListeners;
        private Handler mHandler;
        private long mStartTime;

        public FloatAnimator(float start, float end, long duration) {
            mStartValue = start;
            mEndValue = end;
            mDuration = duration;
            mAnimatedValue = mStartValue;
        }

        public void addEventListener(@Nullable EventListener listener) {
            if (listener == null) {
                return;
            }

            mEventListeners.add(listener);
        }

        public float getAnimatedValue() {
            return mAnimatedValue;
        }

        public void start() {
            if (mHandler != null) {
                return;
            }

            notifyAnimationStart();

            mStartTime = System.currentTimeMillis();

            mHandler = new Handler();
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    final Handler handler = mHandler;

                    if (handler == null) {
                        return;
                    }

                    final long elapsedTime = System.currentTimeMillis() - mStartTime;

                    if (elapsedTime > mDuration) {
                        mHandler = null;
                        notifyAnimationEnd();
                    } else {
                        float fraction = mDuration > 0 ? (float) (elapsedTime) / mDuration : 1f;
                        float delta = mEndValue - mStartValue;
                        mAnimatedValue = mStartValue + delta * fraction;
                        notifyAnimationUpdate();
                        handler.postDelayed(this, ANIMATION_DELAY);
                    }
                }
            });
        }

        public void cancel() {
            if (mHandler == null) {
                return;
            }

            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;

            notifyAnimationCancel();
            notifyAnimationEnd();
        }

        protected void notifyAnimationStart() {
            final List<EventListener> listeners = mEventListeners;

            if (listeners != null) {
                for (EventListener listener : listeners) {
                    listener.onAnimationStart(this);
                }
            }
        }

        protected void notifyAnimationUpdate() {
            final List<EventListener> listeners = mEventListeners;

            if (listeners != null) {
                for (EventListener listener : listeners) {
                    listener.onAnimationUpdate(this);
                }
            }
        }

        protected void notifyAnimationCancel() {
            final List<EventListener> listeners = mEventListeners;

            if (listeners != null) {
                for (EventListener listener : listeners) {
                    listener.onAnimationCancel(this);
                }
            }
        }

        protected void notifyAnimationEnd() {
            final List<EventListener> listeners = mEventListeners;

            if (listeners != null) {
                for (EventListener listener : listeners) {
                    listener.onAnimationEnd(this);
                }
            }
        }
    }
}