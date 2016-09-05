package org.michaelbel.material.widget.ColorPicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.michaelbel.material.R;

@SuppressLint("ViewConstructor")
public class ChannelView extends RelativeLayout {

    private final Channel channel;
    private final IndicatorMode indicatorMode;
    private OnProgressChangedListener listener;

    public ChannelView(Channel c, @ColorInt int color, IndicatorMode indicatorMode, Context context) {
        super(context);

        this.channel = c;
        this.indicatorMode = indicatorMode;

        channel.setProgress(channel.getColorExtractor().extract(color));

        View rootView = inflate(context, R.layout.channel_row, this);

        TextView label = (TextView) rootView.findViewById(R.id.label);
        label.setText(channel.getNameResourceId());
        final TextView progressView = (TextView) rootView.findViewById(R.id.progress_text);
        setProgress(progressView, channel.getProgress());
        SeekBar seekbar = (SeekBar) rootView.findViewById(R.id.seekbar);
        seekbar.setMax(channel.getMaxValue());
        seekbar.setProgress(channel.getProgress());

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                channel.setProgress(progress);
                setProgress(progressView, progress);

                if (listener != null) {
                    listener.onProgressChanged();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    private void setProgress(TextView view, int progress) {
        view.setText(indicatorMode == IndicatorMode.HEX ? Integer.toHexString(progress) : String.valueOf(progress));
    }

    public void registerListener(OnProgressChangedListener listener) {
        this.listener = listener;
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        listener = null;
    }
}