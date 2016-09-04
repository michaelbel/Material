package org.michaelbel.material.widget.ColorPicker;

public class Channel {

    private int mProgress = 0;
    private final int mMinValue;
    private final int mMaxValue;
    private final String mMode;
    private final ColorExtractor mExtractor;

    public Channel(String mode, int min, int max, ColorExtractor extractor) {
        this.mMode = mode;
        this.mMinValue = min;
        this.mMaxValue = max;
        this.mExtractor = extractor;
    }

    public interface ColorExtractor {
        int extract(int color);
    }

    public String getNameResourceId() {
        return mMode;
    }

    public int getMinValue() {
        return mMinValue;
    }

    public int getMaxValue() {
        return mMaxValue;
    }

    public ColorExtractor getColorExtractor() {
        return mExtractor;
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
    }
}