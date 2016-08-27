package org.app.material.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import org.app.material.R;
import org.app.material.utils.AndroidUtilities;
import org.app.material.widget.ColorPicker.Channel;
import org.app.material.widget.ColorPicker.ChannelView;
import org.app.material.widget.ColorPicker.ColorMode;
import org.app.material.widget.ColorPicker.IndicatorMode;
import org.app.material.widget.ColorPicker.OnProgressChangedListener;

import java.util.ArrayList;
import java.util.List;

public class ColorView extends FrameLayout {

    public static final int DEFAULT_COLOR = 0xFFFF5252;
    public static final ColorMode DEFAULT_MODE = ColorMode.RGB;
    public static final IndicatorMode DEFAULT_INDICATOR = IndicatorMode.DECIMAL;

    private ColorMode colorMode;
    private IndicatorMode indicatorMode;
    private @ColorInt int initialColor;

    private View colorView;
    private List<ChannelView> channelViews;

    public ColorView(Context context) {
        this(context, DEFAULT_COLOR, DEFAULT_MODE, DEFAULT_INDICATOR);
    }

    public ColorView(@NonNull Context context, @ColorInt int initColor,
                     @NonNull final ColorMode colorMode, @NonNull IndicatorMode indicatorMode) {
        super(context);
        AndroidUtilities.bind(context);

        this.setClipToPadding(false);

        this.colorMode = colorMode;
        this.indicatorMode = indicatorMode;
        this.initialColor = initColor;

        inflate(getContext(), R.layout.views, this);

        colorView = findViewById(R.id.color_view);
        colorView.setBackgroundColor(initialColor);

        List<Channel> channels = colorMode.getColorMode().getChannels();

        channelViews = new ArrayList<>();
        for (Channel c : channels) {
            channelViews.add(new ChannelView(c, initialColor, indicatorMode, context));
        }

        OnProgressChangedListener seekBarChangeListener = new OnProgressChangedListener() {
            @Override
            public void onProgressChanged() {
                List<Channel> channels = new ArrayList<>();

                for (ChannelView chan : channelViews) {
                    channels.add(chan.getChannel());
                }

                initialColor = colorMode.getColorMode().evaluateColor(channels);
                colorView.setBackgroundColor(initialColor);
            }
        };

        ViewGroup channelContainer = (ViewGroup) findViewById(R.id.channel_container);

        for (ChannelView c : channelViews) {
            channelContainer.addView(c);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) c.getLayoutParams();
            params.topMargin = AndroidUtilities.dp(16);
            params.bottomMargin = AndroidUtilities.dp(4);

            c.registerListener(seekBarChangeListener);
        }
    }

    @ColorInt
    public int getInitialColor() {
        return initialColor;
    }

    @NonNull
    public ColorMode getColorMode() {
        return colorMode;
    }

    @NonNull
    public IndicatorMode getIndicatorMode() {
        return indicatorMode;
    }
}