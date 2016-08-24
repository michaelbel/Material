package org.app.material.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import org.app.material.R;
import org.app.material.utils.AndroidUtilities;
import org.app.material.utils.Color;
import org.app.material.widget.ColorPicker.Channel;
import org.app.material.widget.ColorPicker.ChannelView;
import org.app.material.widget.ColorPicker.ColorMode;
import org.app.material.widget.ColorPicker.IndicatorMode;
import org.app.material.widget.ColorPicker.OnProgressChangedListener;

import java.util.ArrayList;
import java.util.List;

public class ColorView extends FrameLayout {

    public static int DEFAULT_COLOR;


    private final ColorMode colorMode;
    private IndicatorMode indicatorMode;
    private @ColorInt static int currentColor;
    public final static ColorMode DEFAULT_MODE = ColorMode.RGB;
    public final static IndicatorMode DEFAULT_INDICATOR = IndicatorMode.DECIMAL;

    public ColorView(Context context) {
        this(DEFAULT_COLOR, DEFAULT_MODE, DEFAULT_INDICATOR, context);
    }

    public ColorView(@ColorInt int initialColor, final ColorMode colorMode, IndicatorMode indicatorMode, Context context) {
        super(context);
        AndroidUtilities.bind(context);

        DEFAULT_COLOR = Color.getThemeColor(context, R.attr.colorAccent);




        this.indicatorMode = indicatorMode;
        this.colorMode = colorMode;
        currentColor = initialColor;

        DEFAULT_COLOR = AndroidUtilities.getThemeColor(R.attr.colorAccent);

        inflate(getContext(), R.layout.views, this);
        setClipToPadding(false);

        final View colorView = findViewById(R.id.color_view);
        colorView.setBackgroundColor(currentColor);

        List<Channel> channels = colorMode.getColorMode().getChannels();

        final List<ChannelView> channelViews = new ArrayList<>();

        for (Channel c : channels) {
            channelViews.add(new ChannelView(c, currentColor, indicatorMode, getContext()));
        }

        OnProgressChangedListener seekBarChangeListener = new OnProgressChangedListener() {
            @Override
            public void onProgressChanged() {
                List<Channel> channels = new ArrayList<>();

                for (ChannelView chan : channelViews) {
                    channels.add(chan.getChannel());
                }

                currentColor = colorMode.getColorMode().evaluateColor(channels);
                colorView.setBackgroundColor(currentColor);
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

    public ColorMode getColorMode() {
        return colorMode;
    }

    public static int getCurrentColor() {
        return currentColor;
    }

    public IndicatorMode getIndicatorMode() {
        return indicatorMode;
    }
}